package com.epam.traing.gitcl.presentation.presenter;

import android.util.Log;

import com.epam.traing.gitcl.data.interactor.account.IAccountInteractor;
import com.epam.traing.gitcl.data.interactor.search.ISearchIntercator;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.model.HistoryModel;
import com.epam.traing.gitcl.db.model.RepoModel;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.network.Constants;
import com.epam.traing.gitcl.presentation.ui.IMainView;
import com.epam.traing.gitcl.presentation.ui.view.search.SearchDialogFragment;
import com.epam.traing.gitcl.presentation.ui.view.search.SearchListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Yahor_Fralou on 2/7/2017 12:24 PM.
 */

public class MainPresenter implements IMainPresenter {
    private IMainView view;
    // TODO handle all onErrors !!!
    private IAccountInteractor accountInteractor;
    private PrefHelper prefHelper;
    private ISearchIntercator searchIntercator;
    private List<SearchListAdapter.ItemWrapper> searchResults = new ArrayList<>();

    @Inject
    public MainPresenter(IAccountInteractor accountInteractor, PrefHelper prefHelper, ISearchIntercator searchIntercator) {
        this.accountInteractor = accountInteractor;
        this.prefHelper = prefHelper;
        this.searchIntercator = searchIntercator;
    }

    @Override
    public void attachView(IMainView mainView) {
        this.view = mainView;
    }

    @Override
    public void onViewPrepare() {
        updateAccountInfo();
    }

    @Override
    public void onLogoutSelected() {
        view.showLogoutDialog(true);
    }

    @Override
    public void onLogoutConfirmed() {
        accountInteractor.logOutAccount().subscribe(o -> {
        }, th -> {
        }, () -> {
            prefHelper.setShowLogin(true);
            view.showLogoutDialog(false);
            view.startLoginActivity();
        });
    }

    @Override
    public void onLogoutCanceled() {
        view.showLogoutDialog(false);
    }

    @Override
    public void subscribeFullQuery(Observable<String> observableFullQuery) {
        // TODO pagination
        observableFullQuery
                .doOnNext(this::saveHistoryEntry)
                .doOnNext(s -> searchResults.clear())
                .flatMap(s -> searchIntercator.searchRepositoriesOnApi(s, 1))
                .map(this::collectSearchResults)
                .mergeWith(observableFullQuery
                        .flatMap(s -> searchIntercator.searchAccountsOnApi(s, 1))
                        .map(this::collectSearchResults))
                .subscribe(itemWrappers -> onFullSearchFinished());
    }

    @Override
    public void subscribeLiveQuery(Observable<String> observableLiveQuery) {
        observableLiveQuery
                .doOnNext(s -> searchResults.clear())
                .flatMap(s -> searchIntercator.findHistoryEntries(s, SearchDialogFragment.HISTORY_SHOW_MAX).flatMapObservable(Observable::just))
                .map(this::collectSearchResults)
                .mergeWith(observableLiveQuery
                        .flatMap(s -> searchIntercator.findReposLocal(s))
                        .map(this::collectSearchResults)
                )
                .mergeWith(observableLiveQuery
                        .flatMap(s -> searchIntercator.findAccountsLocal(s))
                        .map(this::collectSearchResults))
                .subscribe(itemWrappers -> onLiveSearchFinished());
    }

    private void updateAccountInfo() {
        if (hasTimePassed(prefHelper.getAccountLastUpdateTime(), Constants.Refresh.REFRESH_ACCOUNT_MILLS)) {
            requestAccountInfo();
        }
    }


    private void requestAccountInfo() {
        accountInteractor.reloadCurrentAccount()
                .subscribe(accountModel -> view.updateAccountInfo());
    }

    private boolean hasTimePassed(long timeFrom, int timePass) {
        return new Date().getTime() - timeFrom >= timePass;
    }

    private void saveHistoryEntry(String s) {
        HistoryModel historyModel = new HistoryModel();
        historyModel.setText(s);
        historyModel.setSearchDate(new Date().getTime());

        searchIntercator.saveHistoryEntry(historyModel);
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
        view.updateSearchResults(searchResults);
    }

    private void onFullSearchFinished() {
        Collections.sort(searchResults, (o1, o2) -> {
            int result = o1.getSearchScore().compareTo(o2.getSearchScore());
            result = -result; // We need bigger score go first
            return result != 0 ? result : o1.compareTo(o2);
        });
        view.updateSearchResults(searchResults);
    }

}
