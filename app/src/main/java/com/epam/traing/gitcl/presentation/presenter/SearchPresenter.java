package com.epam.traing.gitcl.presentation.presenter;

import android.util.Log;

import com.epam.traing.gitcl.app.GitClientApplication;
import com.epam.traing.gitcl.data.interactor.search.ISearchInteractor;
import com.epam.traing.gitcl.model.search.SearchResultItem;
import com.epam.traing.gitcl.presentation.ui.view.search.ISearchView;

import java.util.List;

import rx.Observable;

/**
 * Created by Yahor_Fralou on 5/4/2017 7:07 PM.
 */

public class SearchPresenter extends AbstractSubscribePresenter implements ISearchPresenter {
    private static final int HISTORY_SHOW_MAX = 5;

    private ISearchView searchView;
    private ISearchInteractor searchInteractor;

    public SearchPresenter(ISearchInteractor searchInteractor) {
        this.searchInteractor = searchInteractor;
    }

    @Override
    public void attachView(ISearchView searchView) {
        this.searchView = searchView;
    }

    @Override
    public void subscribeFullQuery(Observable<String> observableFullQuery) {
        collectSubscription(

                observableFullQuery
                        .flatMap(searchInteractor::searchRemote)
                        .subscribe(this::onFullSearchResults, this::handleError)
        );
    }

    @Override
    public void subscribeLiveQuery(Observable<String> observableLiveQuery) {
        collectSubscription(

                observableLiveQuery
                        .flatMap(s -> searchInteractor.searchLive(s, HISTORY_SHOW_MAX))
                        .subscribe(this::onLiveSearchResults, this::handleError)
        );
    }

    @Override
    public void detachView() {
        unsubscribeAll();

        this.searchView = null;
    }

    private void onLiveSearchResults(List<SearchResultItem> searchResults) {
        Log.i("gitcl", "Live results: " + searchResults.size());
        searchView.updateSearchResults(searchResults);
    }

    private void onFullSearchResults(List<SearchResultItem> searchResults) {
        Log.i("gitcl", "Full results: " + searchResults.size());
        searchView.updateSearchResults(searchResults);
    }

    private void handleError(Throwable th) {
        Log.e(GitClientApplication.LOG, "Error.", th);
        searchView.showError(th);
    }
}
