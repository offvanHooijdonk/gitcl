package com.epam.traing.gitcl.data.interactor.search;

import android.util.Log;

import com.epam.traing.gitcl.model.AccountModel;
import com.epam.traing.gitcl.model.RepoModel;
import com.epam.traing.gitcl.model.search.SearchResultItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.subjects.PublishSubject;

/**
 * Created by Yahor_Fralou on 7/10/2017 10:55 AM.
 */

public class CompositeSearchInteractor implements ICompositeSearchInteractor {
    private ISearchInteractor searchInteractor;

    private List<SearchResultItem> searchResults = new ArrayList<>();
    private Subscription subscrSearch;
    private PublishSubject<List<SearchResultItem>> subjectSearch;
    private Observable.Transformer<List<?>, List<SearchResultItem>> resultsTransformer =
            observable -> observable.map(this::collectSearchResults);

    public CompositeSearchInteractor(ISearchInteractor searchInteractor) {
        this.searchInteractor = searchInteractor;
    }

    @Override
    public Observable<List<SearchResultItem>> searchLive(String text, int maxHistory) {
        stopQueriesSubscriptions();
        searchResults.clear();
        subjectSearch = PublishSubject.create();

        subscrSearch = searchInteractor.findHistoryEntries(text, maxHistory)
                .compose(handleSearchResults())
                .mergeWith(searchInteractor.findReposLocal(text)
                        .compose(handleSearchResults()))
                .mergeWith(searchInteractor.findAccountsLocal(text)
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
        searchInteractor.saveSearchText(text)
                .subscribe(o -> {
                }, Observable::error);
        subjectSearch = PublishSubject.create();

        subscrSearch = searchInteractor.searchRepositoriesOnApi(text, 1)
                .compose(handleSearchResults())
                .mergeWith(searchInteractor.searchAccountsOnApi(text, 1)
                        .compose(handleSearchResults()))
                .subscribe(searchResultItems -> {
                }, Observable::error, this::onRemoteSearchComplete);

        return subjectSearch;
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
