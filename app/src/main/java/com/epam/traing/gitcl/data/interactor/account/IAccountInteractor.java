package com.epam.traing.gitcl.data.interactor.account;

import com.epam.traing.gitcl.db.model.AccountModel;

import rx.Observable;

/**
 * Created by off on 19.02.2017.
 */

public interface IAccountInteractor {
    Observable<AccountModel> updateAccount(String accountName);

    Observable<AccountModel> updateCurrentAccount();
}
