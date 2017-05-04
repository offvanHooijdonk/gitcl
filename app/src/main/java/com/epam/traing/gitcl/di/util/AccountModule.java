package com.epam.traing.gitcl.di.util;

import com.epam.traing.gitcl.db.dao.AccountDao;
import com.epam.traing.gitcl.db.dao.IAccountDao;
import com.epam.traing.gitcl.network.GitHubUserClient;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

import static com.epam.traing.gitcl.di.application.NetworkModule.RETROFIT_API;

/**
 * Created by Yahor_Fralou on 2/20/2017 11:43 AM.
 */

@Module
public class AccountModule {

    @Provides
    @UtilsScope
    IAccountDao provideAccountDao(StorIOSQLite sqLite) {
        return new AccountDao(sqLite);
    }

    @Provides
    @UtilsScope
    GitHubUserClient getUserClient(@Named(RETROFIT_API) Retrofit apiRetrofit) {
        return apiRetrofit.create(GitHubUserClient.class);
    }

}
