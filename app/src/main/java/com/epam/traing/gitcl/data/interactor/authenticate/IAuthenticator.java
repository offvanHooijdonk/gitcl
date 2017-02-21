package com.epam.traing.gitcl.data.interactor.authenticate;

import com.epam.traing.gitcl.db.model.AccountModel;

import rx.Observable;

/**
 * Created by Yahor_Fralou on 1/25/2017 5:43 PM.
 */

public interface IAuthenticator {
    Observable<AccountModel> authorizeFromCallback(String uri);
    String composeOAuthUrl();
    Observable<AccountModel> prepareOnLoginData();
}
