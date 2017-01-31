package com.epam.traing.gitcl.presenter;

import com.epam.traing.gitcl.app.GitClApplication;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.interactor.authenticate.IAuthenticator;
import com.epam.traing.gitcl.ui.ILoginView;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
        loginView.showLoginProgress(true);

        authenticator.authenticate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .skipWhile(accountModel -> accountModel == null)
                .first()
                .subscribe(this::onLoginSuccess, this::onLoginFail);
    }


    public void onLoginSuccess(AccountModel accountModel) {
        loginView.showLoginProgress(false);
        loginView.startMainViewAsLogged();
    }


    public void onLoginFail(Throwable th) {
        loginView.showLoginProgress(false);
    }
}
