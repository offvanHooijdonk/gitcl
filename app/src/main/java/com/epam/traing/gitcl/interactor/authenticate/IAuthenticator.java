package com.epam.traing.gitcl.interactor.authenticate;

import com.epam.traing.gitcl.db.model.AccountModel;

import rx.Observable;

/**
 * Created by Yahor_Fralou on 1/25/2017 5:43 PM.
 */

public interface IAuthenticator {
    Observable<AccountModel> authorizeFromCallback(String uri);
    Observable<Boolean> getShowLogin();
    String composeOAuthUrl();
    void setShowLogin(boolean show);
    Observable<AccountModel> prepareOnLoginData();
}
