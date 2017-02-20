package com.epam.traing.gitcl.di.main;

import com.epam.traing.gitcl.db.dao.AccountDao;
import com.epam.traing.gitcl.db.dao.IAccountDao;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 2/20/2017 11:43 AM.
 */

@Module
public class AccountModule {

    @Provides
    @Singleton
    IAccountDao provideAccountDao(StorIOSQLite sqLite) {
        return new AccountDao(sqLite);
    }

}
