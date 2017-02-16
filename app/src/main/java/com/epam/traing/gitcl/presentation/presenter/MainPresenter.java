package com.epam.traing.gitcl.presentation.presenter;

import com.epam.traing.gitcl.presentation.ui.IMainView;

/**
 * Created by Yahor_Fralou on 2/7/2017 12:24 PM.
 */

public class MainPresenter implements IMainPresenter {
    private IMainView view;

    @Override
    public void attachView(IMainView mainView) {
        this.view = mainView;
    }
}
