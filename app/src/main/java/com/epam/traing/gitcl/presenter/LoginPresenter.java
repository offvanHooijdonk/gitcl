package com.epam.traing.gitcl.presenter;

import com.epam.traing.gitcl.ui.ILoginView;

/**
 * Created by Yahor_Fralou on 1/25/2017 5:12 PM.
 */

public class LoginPresenter implements ILoginPresenter {
    private ILoginView loginView;

    @Override
    public void setView(ILoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void onSkipLoginSelected() {
        loginView.startMainViewAsAnon();
    }

    @Override
    public void onLoginSelected() {
        loginView.startMainViewAsLogged();
    }
}
