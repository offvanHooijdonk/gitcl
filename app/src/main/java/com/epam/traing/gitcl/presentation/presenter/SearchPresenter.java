package com.epam.traing.gitcl.presentation.presenter;

import com.epam.traing.gitcl.data.interactor.search.ISearchInteractor;
import com.epam.traing.gitcl.presentation.ui.view.search.ISearchView;

import javax.inject.Inject;

/**
 * Created by Yahor_Fralou on 5/4/2017 7:07 PM.
 */

public class SearchPresenter implements ISearchPresenter {

    private ISearchView searchView;
    private ISearchInteractor searchInteractor;

    @Inject
    public SearchPresenter(ISearchInteractor searchInteractor) {
        this.searchInteractor = searchInteractor;
    }

    @Override
    public void attachView(ISearchView searchView) {
        this.searchView = searchView;
    }

    @Override
    public void detachView() {
        // TODO stop subscriptions
        this.searchView = null;
    }
}
