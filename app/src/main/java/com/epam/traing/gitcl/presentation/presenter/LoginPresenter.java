package com.epam.traing.gitcl.presentation.presenter;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.epam.traing.gitcl.app.Application;
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
        if (uri != null && isCallbackUrl(uri.toString())) {
            Log.d(Application.LOG, "Uri received: " + uri.toString());

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

    private void onLoginSuccess(AccountModel accountModel) {
        Log.d(Application.LOG, "Login Success");
        Log.d(Application.LOG, new Gson().toJson(accountModel));
        authenticator.setShowLogin(false);
        loginView.showLoginProgress(false);
        loginView.startMainView();
    }


    private void onLoginFail(Throwable th) {
        // TODO show error message
        loginView.showLoginProgress(false);
        loginView.showAuthErrorMessage(th);
    }
}
