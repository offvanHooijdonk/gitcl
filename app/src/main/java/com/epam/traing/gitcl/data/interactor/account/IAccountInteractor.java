package com.epam.traing.gitcl.data.interactor.account;

import com.epam.traing.gitcl.db.model.AccountModel;

import rx.Observable;

/**
 * Created by off on 19.02.2017.
 */

public interface IAccountInteractor {
    Observable<AccountModel> reloadAccount(String accountName);

    Observable<AccountModel> subscribeCurrentAccountChange();

    Observable<AccountModel> reloadCurrentAccount();

    Observable logOutAccount();
}
