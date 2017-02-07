package com.epam.traing.gitcl.presentation.presenter;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.epam.traing.gitcl.app.Constants;
import com.epam.traing.gitcl.app.GitClApplication;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.interactor.authenticate.IAuthenticator;
import com.epam.traing.gitcl.presentation.ui.ILoginView;
import com.google.gson.Gson;

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
        loginView.startMainView();
    }

    @Override
    public void onLoginSelected() {
        loginView.startWebViewForOAuth(authenticator.composeOAuthUrl());
    }

    private boolean isCallbackUrl(String s) {
        return s != null && s.startsWith(Constants.Api.OAUTH_CALLBACK_URL);
    }

    @Override
    public void onActivityResume(Intent intent) {
        Uri uri = intent.getData();

        Log.d(GitClApplication.LOG, "onResume data: " + (uri != null ? uri.toString() : "null"));
        if (uri != null && isCallbackUrl(uri.toString())) {
            Log.d(GitClApplication.LOG, "Uri received: " + uri.toString());

            onOAuthCallback(uri.toString());
        } else {
            authenticator.getShowLogin().subscribe(show -> {
                if (show) {
                    loginView.showLoginScreen();
                } else {
                    doLoginWithCurrentAccount();
                }
            });
        }
    }

    private void onOAuthCallback(String url) {
        // TODO hide logo here to make layout blank?
        loginView.startLoginProgress(true);

        authenticator.authorizeFromCallback(url)
                .first()
                .subscribe(this::onLoginSuccess, this::onLoginFail);
    }

    private void doLoginWithCurrentAccount() {
        authenticator.prepareOnLoginData()
                .first()
                .subscribe(this::onLoginSuccess,
                        this::onLoginFail);
    }

    private void onLoginSuccess(AccountModel accountModel) {
        Log.d(GitClApplication.LOG, "Login Success");
        Log.d(GitClApplication.LOG, new Gson().toJson(accountModel));
        authenticator.setShowLogin(false);
        loginView.startLoginProgress(false);
        loginView.startMainView();
    }


    private void onLoginFail(Throwable th) {
        // TODO show error message
        loginView.startLoginProgress(false);
        loginView.showAuthErrorMessage(th);
    }
}
