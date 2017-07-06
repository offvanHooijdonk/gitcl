package com.epam.traing.gitcl.presentation.presenter;

import android.util.Log;

import com.epam.traing.gitcl.app.GitClientApplication;
import com.epam.traing.gitcl.data.interactor.search.ISearchInteractor;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.model.HistoryModel;
import com.epam.traing.gitcl.db.model.RepoModel;
import com.epam.traing.gitcl.presentation.ui.view.search.ISearchView;
import com.epam.traing.gitcl.presentation.ui.view.search.SearchListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscription;

/**
 * Created by Yahor_Fralou on 5/4/2017 7:07 PM.
 */

public class SearchPresenter extends AbstractSubscribePresenter implements ISearchPresenter {
    private static final int HISTORY_SHOW_MAX = 5;

    private ISearchView searchView;
    private ISearchInteractor searchInteractor;

    private List<SearchListAdapter.ItemWrapper> searchResults = new ArrayList<>();
    private Subscription subscrFullEntry;
    private Subscription subscrLiveEntry;

    public SearchPresenter(ISearchInteractor searchInteractor) {
        this.searchInteractor = searchInteractor;
    }

    @Override
    public void attachView(ISearchView searchView) {
        this.searchView = searchView;
    }

    @Override
    public void subscribeFullQuery(Observable<String> observableFullQuery) {
        // TODO pagination
        // Such approach shows a page of Repos and a page of accounts, regardless the score difference between Repos and Accounts
        // Search API does not let set page size, or limit score vales. Therefore, for omni-search should consider search cache.
        // Or search Accounts and Repos separately
        collectSubscription(

                observableFullQuery
                        .doOnNext(this::saveHistoryEntry)
                        .doOnNext(s -> stopQueriesSubscriptions())
                        .doOnNext(s -> searchResults.clear())
                        .subscribe(this::searchFull, this::handleError)

        );
    }

    @Override
    public void subscribeLiveQuery(Observable<String> observableLiveQuery) {
        collectSubscription(

                observableLiveQuery
                        .doOnNext(s -> stopQueriesSubscriptions())
                        .doOnNext(s -> searchResults.clear())
                        .subscribe(this::searchLive, this::handleError)
        );
    }

    @Override
    public void detachView() {
        unsubscribeAll();
        stopQueriesSubscriptions();

        this.searchView = null;
    }

    private void searchFull(String text) {
        subscrFullEntry = searchInteractor.searchRepositoriesOnApi(text, 1)
                .compose(handleSearchResults())
                .mergeWith(searchInteractor.searchAccountsOnApi(text, 1)
                        .compose(handleSearchResults()))
                .subscribe(itemWrappers -> {
                }, this::handleError, this::onFullSearchFinished);
    }

    private void searchLive(String text) {

        subscrLiveEntry = searchInteractor.findHistoryEntries(text, HISTORY_SHOW_MAX)
                .compose(handleSearchResults())
                .mergeWith(searchInteractor.findReposLocal(text)
                        .compose(handleSearchResults()))
                .mergeWith(searchInteractor.findAccountsLocal(text)
                        .compose(handleSearchResults()))

                .subscribe(itemWrappers -> {
                }, this::handleError, this::onLiveSearchFinished);
    }

    private void stopQueriesSubscriptions() {
        if (subscrLiveEntry != null && !subscrLiveEntry.isUnsubscribed()) {
            subscrLiveEntry.unsubscribe();
        }
        if (subscrFullEntry != null && !subscrFullEntry.isUnsubscribed()) {
            subscrFullEntry.unsubscribe();
        }
    }

    private void saveHistoryEntry(String s) {
        HistoryModel historyModel = new HistoryModel();
        historyModel.setText(s);
        historyModel.setSearchDate(new Date().getTime());

        searchInteractor.saveHistoryEntry(historyModel);
    }

    private <T> Observable.Transformer<List<?>, List<SearchListAdapter.ItemWrapper>> handleSearchResults() {
        return observable -> observable.map(this::collectSearchResults);
    }

    private List<SearchListAdapter.ItemWrapper> collectSearchResults(List<?> historyModels) {
        Observable.from(historyModels)
                .map(model -> {
                    int type = SearchListAdapter.ItemWrapper.HISTORY;
                    float score = 0;
                    if (model instanceof AccountModel) {
                        type = SearchListAdapter.ItemWrapper.ACCOUNT;
                        score = ((AccountModel) model).getSearchScore();
                    } else if (model instanceof RepoModel) {
                        type = SearchListAdapter.ItemWrapper.REPOSITORY;
                        score = ((RepoModel) model).getSearchScore();
                    }
                    return new SearchListAdapter.ItemWrapper(type, model, score);
                })
                .doOnNext(searchResults::add)
                .subscribe(itemWrapper -> {
                }, throwable -> {
                }, () -> Log.i("gitcl", "Results collected: " + searchResults.size()));

        return searchResults;
    }

    private void onLiveSearchFinished() {
        Collections.sort(searchResults);
        Log.i("gitcl", "Live results: " + searchResults.size());
        searchView.updateSearchResults(searchResults);
    }

    private void onFullSearchFinished() {
        Collections.sort(searchResults, (o1, o2) -> {
            int result = o1.getSearchScore().compareTo(o2.getSearchScore());
            result = -result; // We need bigger score go first
            return result != 0 ? result : o1.compareTo(o2);
        });
        searchView.updateSearchResults(searchResults);
    }

    private void handleError(Throwable th) {
        Log.e(GitClientApplication.LOG, "Error.", th);
        searchView.showError(th);
    }
}
