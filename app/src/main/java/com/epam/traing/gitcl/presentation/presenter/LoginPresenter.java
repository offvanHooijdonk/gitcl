package com.epam.traing.gitcl.presentation.presenter;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.epam.traing.gitcl.app.Application;
import com.epam.traing.gitcl.data.interactor.authenticate.AccountNotFoundException;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.data.interactor.authenticate.IAuthenticator;
import com.epam.traing.gitcl.network.Constants;
import com.epam.traing.gitcl.presentation.ui.ILoginView;
import com.google.gson.Gson;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Yahor_Fralou on 1/25/2017 5:12 PM.
 */

public class LoginPresenter implements ILoginPresenter {
    private ILoginView loginView;

    private IAuthenticator authenticator;
    private PrefHelper prefHelper;

    @Inject
    public LoginPresenter(IAuthenticator authenticator, PrefHelper prefHelper) {
        this.authenticator = authenticator;
        this.prefHelper = prefHelper;
    }

    @Override
    public void attachView(ILoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void onSkipLoginSelected() {
        prefHelper.setShowLogin(false);
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

        Log.d(Application.LOG, "onResume data: " + (uri != null ? uri.toString() : "null"));
        // TODO handle reopen App after auth process, which causes excessive auth attempt
        if (uri != null && isCallbackUrl(uri.toString())) {
            Log.d(Application.LOG, "Uri received: " + uri.toString());

            onOAuthCallback(uri.toString());
        } else {
            proceedLogin();
        }
    }

    private void onOAuthCallback(String url) {
        loginView.showLoginProgress(true);

        authenticator.authorizeFromCallback(url)
                .first()
                .doOnNext(accountModel -> prefHelper.setAccountLastUpdateTime(new Date().getTime()))
                .subscribe(this::onLoginSuccess, this::onLoginFail);
    }

    private void doLoginWithCurrentAccount() {
        authenticator.prepareOnLoginData()
                .first()
                .subscribe(this::onLoginSuccess,
                        this::onLoginFail);
    }

    private void proceedLogin() {
        if (prefHelper.getLoggedAccountName() == null) {
            if (prefHelper.isShowLoginScreen()) {
                loginView.showLoginScreen();
            } else {
                onLoginSuccess(null);
            }
        } else {
            doLoginWithCurrentAccount();
        }
    }

    private void onLoginSuccess(AccountModel accountModel) {
        Log.d(Application.LOG, "Login Success");
        Log.d(Application.LOG, accountModel != null ? new Gson().toJson(accountModel) : "empty");
        prefHelper.setShowLogin(false);
        loginView.showLoginProgress(false);
        loginView.startMainView();
    }


    private void onLoginFail(Throwable th) {
        loginView.showLoginProgress(false);
        loginView.showAuthErrorMessage(th);
        if (th instanceof AccountNotFoundException) {
            // assume information lost and new auth required
            prefHelper.setLoggedAccountName(null);
            loginView.showAuthErrorMessage(th);
        }
    }
}
