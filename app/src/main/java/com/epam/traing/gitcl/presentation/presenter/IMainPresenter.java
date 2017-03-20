package com.epam.traing.gitcl.presentation.presenter;

import com.epam.traing.gitcl.presentation.ui.IMainView;

import rx.Observable;

/**
 * Created by Yahor_Fralou on 2/7/2017 12:24 PM.
 */

public interface IMainPresenter {
    void attachView(IMainView mainView);

    void onViewPrepare();

    void onLogoutSelected();

    void onLogoutConfirmed();

    void onLogoutCanceled();

    void subscribeFullQuery(Observable<String> observableFullQuery);

    void subscribeLiveQuery(Observable<String> observableLiveQuery);
}
