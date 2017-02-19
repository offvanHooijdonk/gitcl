package com.epam.traing.gitcl.db.dao;

import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.tables.AccountTable;
import com.pushtorefresh.storio.sqlite.Changes;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import rx.Observable;

/**
 * Created by Yahor_Fralou on 2/17/2017 3:18 PM.
 */

public class AccountDao implements IAccountDao {
    private StorIOSQLite storIOSQLite;

    public AccountDao(StorIOSQLite storIOSQLite) {
        this.storIOSQLite = storIOSQLite;
    }

    @Override
    public Observable<AccountModel> findAccountByName(String accountName) {
        return storIOSQLite.get().object(AccountModel.class)
                .withQuery(Query.builder()
                        .table(AccountTable.TABLE)
                        .where(String.format("%s='%s'", AccountTable.COLUMN_ACCOUNT_NAME, accountName)).build())
                .prepare().asRxObservable();
    }

    @Override
    public void saveAccount(AccountModel accountModel) {
        storIOSQLite.put().object(accountModel).prepare().executeAsBlocking();
    }

    @Override
    public Observable<Changes> subscribeAccountChange() {
        return storIOSQLite.observeChangesInTable(AccountTable.TABLE);
    }
}
