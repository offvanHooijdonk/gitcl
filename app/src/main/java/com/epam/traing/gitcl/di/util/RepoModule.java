package com.epam.traing.gitcl.di.util;

import com.epam.traing.gitcl.db.dao.IRepoDao;
import com.epam.traing.gitcl.db.dao.RepoDao;
import com.epam.traing.gitcl.di.application.NetworkModule;
import com.epam.traing.gitcl.network.GitHubRepoClient;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Yahor_Fralou on 5/3/2017 5:57 PM.
 */

@Module
public class RepoModule {
    @Provides
    @UtilsScope
    GitHubRepoClient provideRepoClient(@Named(NetworkModule.RETROFIT_API) Retrofit apiRetrofit) {
        return apiRetrofit.create(GitHubRepoClient.class);
    }

    @Provides
    @UtilsScope
    IRepoDao provideRepoDao(StorIOSQLite sqLite) {
        return new RepoDao(sqLite);
    }
}
