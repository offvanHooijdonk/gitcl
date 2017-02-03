package com.epam.traing.gitcl.presenter;

import com.epam.traing.gitcl.app.GitClApplication;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.interactor.authenticate.IAuthenticator;
import com.epam.traing.gitcl.ui.ILoginView;

import javax.inject.Inject;

/**
 * Created by Yahor_Fralou on 1/25/2017 5:12 PM.
 */

public class LoginPresenter implements ILoginPresenter {
    @Inject
    IAuthenticator authenticator;

    private ILoginView loginView;

    public LoginPresenter() {
        GitClApplication.getLoginComponent().inject(this);
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
        // TODO check first if account logged exists
        // if account if Prefs - load it. If now - start OAuth flow
        loginView.startWebViewForOAuth(authenticator.getOAuthUrl());
    }

    @Override
    public void onLoginCallback(String callbackUrl) {
        loginView.showLoginProgress(true);

        authenticator.authorizeFromCallback(callbackUrl)
                .first()
                .subscribe(this::onLoginSuccess, this::onLoginFail);
    }

    @Override
    public boolean isCallbackUrl(String s) {
        return s != null && s.startsWith(authenticator.getOAuthCallbackUrl());
    }

    public void onLoginSuccess(AccountModel accountModel) {
        loginView.showLoginProgress(false);
        loginView.startMainViewAsLogged();
    }


    public void onLoginFail(Throwable th) {
        // TODO show error message
        loginView.showLoginProgress(false);
        loginView.showAuthErrorMessage(th);
    }
}
