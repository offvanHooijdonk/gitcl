package com.epam.traing.gitcl.presentation.presenter;

import com.epam.traing.gitcl.data.interactor.search.ISearchInteractor;

import javax.inject.Inject;

/**
 * Created by Yahor_Fralou on 5/4/2017 7:07 PM.
 */

public class SearchPresenter implements ISearchPresenter {

    private ISearchInteractor searchInteractor;

    @Inject
    public SearchPresenter(ISearchInteractor searchInteractor) {
        this.searchInteractor = searchInteractor;
    }
}
