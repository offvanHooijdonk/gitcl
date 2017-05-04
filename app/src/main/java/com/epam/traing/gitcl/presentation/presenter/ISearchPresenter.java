package com.epam.traing.gitcl.presentation.presenter;

import com.epam.traing.gitcl.presentation.ui.view.search.ISearchView;

/**
 * Created by Yahor_Fralou on 5/4/2017 7:06 PM.
 */

public interface ISearchPresenter {
    void attachView(ISearchView searchView);
    void detachView();
}
