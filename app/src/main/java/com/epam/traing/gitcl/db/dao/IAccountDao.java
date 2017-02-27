package com.epam.traing.gitcl.db.dao;

import com.epam.traing.gitcl.db.model.AccountModel;

import rx.Observable;

/**
 * Created by off on 19.02.2017.
 */

public interface IAccountDao {
    Observable<AccountModel> findAccountByName(String accountName);

    void saveAccount(AccountModel accountModel);
}
