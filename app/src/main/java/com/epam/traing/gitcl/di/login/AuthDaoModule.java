package com.epam.traing.gitcl.di.login;

import com.epam.traing.gitcl.db.dao.AccountDao;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 2/17/2017 3:21 PM.
 */

@Module
public class AuthDaoModule {

    @Provides
    @LoginScope
    AccountDao provideAccountDao(StorIOSQLite storIOSQLite) {
        return new AccountDao(storIOSQLite);
    }
}
