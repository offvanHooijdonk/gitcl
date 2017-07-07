package com.epam.traing.gitcl.data.interactor.search;

import android.util.Log;

import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.data.interactor.Interactors;
import com.epam.traing.gitcl.db.dao.IAccountDao;
import com.epam.traing.gitcl.db.dao.IHistoryDao;
import com.epam.traing.gitcl.db.dao.IRepoDao;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.model.HistoryModel;
import com.epam.traing.gitcl.db.model.RepoModel;
import com.epam.traing.gitcl.db.model.search.SearchResultItem;
import com.epam.traing.gitcl.network.GitHubRepoClient;
import com.epam.traing.gitcl.network.GitHubUserClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.subjects.PublishSubject;

/**
 * Created by Yahor_Fralou on 3/20/2017 7:07 PM.
 */

public class SearchInteractor implements ISearchInteractor { // TODO split the class and interface into simple search and its 'Composite' Adapter

    private IHistoryDao historyDao;
    private IRepoDao repoDao;
    private IAccountDao accountDao;
    private GitHubRepoClient repoClient;
    private GitHubUserClient userClient;
    private ModelConverter modelConverter;

    private List<SearchResultItem> searchResults = new ArrayList<>();

    private Subscription subscrSearch;

    private PublishSubject<List<SearchResultItem>> subjectSearch;

    private Observable.Transformer<List<?>, List<SearchResultItem>> resultsTransformer =
            observable -> observable.map(this::collectSearchResults);

    public SearchInteractor(IHistoryDao historyDao, IRepoDao repoDao, IAccountDao accountDao,
                            GitHubRepoClient repoClient, GitHubUserClient userClient, ModelConverter modelConverter) {
        this.historyDao = historyDao;
        this.repoDao = repoDao;
        this.accountDao = accountDao;
        this.repoClient = repoClient;
        this.userClient = userClient;
        this.modelConverter = modelConverter;
    }

    @Override
    public Observable<List<SearchResultItem>> searchLive(String text, int maxHistory) {
        stopQueriesSubscriptions();
        searchResults.clear();
        subjectSearch = PublishSubject.create();

        subscrSearch = findHistoryEntries(text, maxHistory)
                .compose(handleSearchResults())
                .mergeWith(findReposLocal(text)
                        .compose(handleSearchResults()))
                .mergeWith(findAccountsLocal(text)
                        .compose(handleSearchResults()))
                .subscribe(searchResultItems -> {
                }, Observable::error, this::onLiveSearchComplete);

        return subjectSearch;
    }

    @Override
    public Observable<List<SearchResultItem>> searchRemote(String text) {
        // TODO pagination
        // Such approach shows a page of Repos and a page of accounts, regardless the score difference between Repos and Accounts
        // Search API does not let set page size, or limit score vales. Therefore, for omni-search should consider search cache.
        // Or search Accounts and Repos separately
        stopQueriesSubscriptions();
        searchResults.clear();
        saveSearchText(text)
                .subscribe(o -> {
                }, Observable::error);
        subjectSearch = PublishSubject.create();

        subscrSearch = searchRepositoriesOnApi(text, 1)
                .compose(handleSearchResults())
                .mergeWith(searchAccountsOnApi(text, 1)
                        .compose(handleSearchResults()))
                .subscribe(searchResultItems -> {
                }, Observable::error, this::onRemoteSearchComplete);

        return subjectSearch;
    }

    @Override
    public Observable<?> saveSearchText(String searchText) {
        HistoryModel historyModel = new HistoryModel();
        historyModel.setText(searchText);
        historyModel.setSearchDate(new Date().getTime());

        return historyDao.save(historyModel)
                .toObservable()
                .compose(Interactors.applySchedulersIO());
    }

    @Override
    public Observable<List<HistoryModel>> findHistoryEntries(String queryText, int limit) {
        return historyDao.findWithText(queryText, limit)
                .toObservable()
                .compose(Interactors.applySchedulersIO());
    }

    @Override
    public Observable<List<RepoModel>> findReposLocal(String queryText) {
        if (queryText == null || queryText.isEmpty()) {
            return Observable.empty();
        } else {
            return repoDao.findRepos(queryText)
                    .toObservable()
                    .compose(Interactors.applySchedulersIO());
        }
    }

    // TODO conseder switch to Single
    @Override
    public Observable<List<AccountModel>> findAccountsLocal(String queryText) {
        if (queryText == null || queryText.isEmpty()) {
            return Observable.empty();
        } else {
            return accountDao.findAccounts(queryText)
                    .toObservable()
                    .compose(Interactors.applySchedulersIO());
        }
    }

    @Override
    public Observable<List<AccountModel>> searchAccountsOnApi(String queryText, int page) {
        return userClient.searchUsers(queryText, page)
                .map(searchResults -> modelConverter.toAccountModelList(searchResults.getItems()))
                .compose(Interactors.applySchedulersIO());
    }

    @Override
    public Observable<List<RepoModel>> searchRepositoriesOnApi(String queryText, int page) {
        return repoClient.searchRepositories(queryText, page)
                .map(searchResults -> modelConverter.toRepoModelList(searchResults.getItems()))
                .compose(Interactors.applySchedulersIO());
    }

    private void stopQueriesSubscriptions() {
        if (subscrSearch != null && !subscrSearch.isUnsubscribed()) {
            subscrSearch.unsubscribe();
        }
    }

    private <T> Observable.Transformer<List<?>, List<SearchResultItem>> handleSearchResults() {
        return resultsTransformer;
    }

    private List<SearchResultItem> collectSearchResults(List<?> historyModels) {
        Observable.from(historyModels)
                .map(model -> {
                    int type = SearchResultItem.HISTORY;
                    float score = 0;
                    if (model instanceof AccountModel) {
                        type = SearchResultItem.ACCOUNT;
                        score = ((AccountModel) model).getSearchScore();
                    } else if (model instanceof RepoModel) {
                        type = SearchResultItem.REPOSITORY;
                        score = ((RepoModel) model).getSearchScore();
                    }
                    return new SearchResultItem(type, model, score);
                })
                .doOnNext(searchResults::add)
                .subscribe(itemWrapper -> {
                }, throwable -> {
                }, () -> Log.i("gitcl", "Results collected: " + searchResults.size()));

        return searchResults;
    }

    private void onLiveSearchComplete() {
        Log.i("gitcl", "Local search complete");
        Collections.sort(searchResults);

        subjectSearch.onNext(searchResults);
        subjectSearch.onCompleted();
    }

    private void onRemoteSearchComplete() {
        Log.i("gitcl", "Remote search complete");
        Collections.sort(searchResults, (o1, o2) -> {
            int result = o1.getSearchScore().compareTo(o2.getSearchScore());
            result = -result; // We need bigger score go first
            return result != 0 ? result : o1.compareTo(o2);
        });

        subjectSearch.onNext(searchResults);
        subjectSearch.onCompleted();
    }

}
