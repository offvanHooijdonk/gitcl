package com.epam.traing.gitcl.presentation.presenter;

import com.epam.traing.gitcl.presentation.ui.IRepoListView;

/**
 * Created by Yahor_Fralou on 2/22/2017 11:56 AM.
 */

public interface IRepoListPresenter {
    void attachView(IRepoListView view);

    void onViewShows();

    void onRefreshTriggered();

    void detachView();
}
