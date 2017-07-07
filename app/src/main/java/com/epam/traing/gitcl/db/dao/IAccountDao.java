package com.epam.traing.gitcl.db.dao;

import com.epam.traing.gitcl.model.AccountModel;

import java.util.List;

import rx.Observable;
import rx.Single;

/**
 * Created by off on 19.02.2017.
 */

public interface IAccountDao {
    Observable<AccountModel> findAccountByName(String accountName);

    void saveAccount(AccountModel accountModel);

    Single<List<AccountModel>> findAccounts(String queryText);
}
