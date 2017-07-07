package com.epam.traing.gitcl.data.interactor.account;

import com.epam.traing.gitcl.model.AccountModel;

import rx.Observable;

/**
 * Created by off on 19.02.2017.
 */

public interface IAccountInteractor {
    Observable<AccountModel> loadAccountInfo(String accountName);

    Observable<AccountModel> reloadCurrentAccount();

    Observable<Void> logOutAccount();
}
