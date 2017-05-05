package com.epam.traing.gitcl.presentation.presenter;

import com.epam.traing.gitcl.presentation.ui.view.search.ISearchView;

import rx.Observable;

/**
 * Created by Yahor_Fralou on 5/4/2017 7:06 PM.
 */

public interface ISearchPresenter {
    void attachView(ISearchView searchView);

    void subscribeFullQuery(Observable<String> observableFullQuery);

    void subscribeLiveQuery(Observable<String> observableLiveQuery);

    void detachView();
}
