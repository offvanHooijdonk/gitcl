package com.epam.traing.gitcl.presenter;

import com.epam.traing.gitcl.app.GitClApplication;
import com.epam.traing.gitcl.interactor.IAuthenticator;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.ui.ILoginView;

import javax.inject.Inject;

/**
 * Created by Yahor_Fralou on 1/25/2017 5:12 PM.
 */

public class LoginPresenter implements ILoginPresenter, IAuthenticator.AuthenticationListener {
    @Inject
    IAuthenticator authenticator;

    private ILoginView loginView;

    public LoginPresenter() {
        GitClApplication.getLoginPresenterComponent().inject(this);

        authenticator.setListener(this);
    }

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
        loginView.showLoginProgress(true);

        authenticator.authenticate();
    }

    @Override
    public void onSuccess(AccountModel accountModel) {
        GitClApplication.setAccount(accountModel);

        loginView.showLoginProgress(false);
        loginView.startMainViewAsLogged();
    }

    @Override
    public void onFail(String message) {
        loginView.showLoginProgress(false);
        loginView.showLoginProgress(false);
    }
}
