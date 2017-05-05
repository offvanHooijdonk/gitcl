package com.epam.traing.gitcl.presentation.presenter;

import android.util.Log;

import com.epam.traing.gitcl.app.Application;
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

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Yahor_Fralou on 5/4/2017 7:07 PM.
 */

public class SearchPresenter extends AbstractSubscribePresenter implements ISearchPresenter {
    private static final int HISTORY_SHOW_MAX = 5;

    private ISearchView searchView;
    private ISearchInteractor searchInteractor;

    private List<SearchListAdapter.ItemWrapper> searchResults = new ArrayList<>();

    @Inject
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
        getCompositeSubscription().add(

                observableFullQuery
                        .doOnNext(this::saveHistoryEntry)
                        .doOnNext(s -> searchResults.clear())
                        .flatMap(s -> searchInteractor.searchRepositoriesOnApi(s, 1))
                        .map(this::collectSearchResults)
                        .mergeWith(observableFullQuery
                                .flatMap(s -> searchInteractor.searchAccountsOnApi(s, 1))
                                .map(this::collectSearchResults))
                        .subscribe(itemWrappers -> onFullSearchFinished(), this::handleError)
        );
    }

    @Override
    public void subscribeLiveQuery(Observable<String> observableLiveQuery) {
        getCompositeSubscription().add(

                observableLiveQuery
                        .doOnNext(s -> searchResults.clear())
                        .flatMap(s -> searchInteractor.findHistoryEntries(s, HISTORY_SHOW_MAX).flatMapObservable(Observable::just))
                        .map(this::collectSearchResults)
                        .mergeWith(observableLiveQuery
                                .flatMap(s -> searchInteractor.findReposLocal(s))
                                .map(this::collectSearchResults)
                        )
                        .mergeWith(observableLiveQuery
                                .flatMap(s -> searchInteractor.findAccountsLocal(s))
                                .map(this::collectSearchResults))
                        .subscribe(itemWrappers -> onLiveSearchFinished(), this::handleError)
        );
    }

    @Override
    public void detachView() {
        // TODO stop subscriptions
        unsubscribeAll();

        this.searchView = null;
    }

    private void saveHistoryEntry(String s) {
        HistoryModel historyModel = new HistoryModel();
        historyModel.setText(s);
        historyModel.setSearchDate(new Date().getTime());

        searchInteractor.saveHistoryEntry(historyModel);
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
                .doOnNext(searchResults::add).subscribe();
        Log.i("LOG", "Result items: " + searchResults.size());
        return searchResults;
    }

    private void onLiveSearchFinished() {
        Collections.sort(searchResults);
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
        Log.e(Application.LOG, "Error.", th);
        searchView.showError(th);
    }
}
