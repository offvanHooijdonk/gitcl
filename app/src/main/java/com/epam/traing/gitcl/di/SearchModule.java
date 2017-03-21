package com.epam.traing.gitcl.di;

import com.epam.traing.gitcl.db.dao.IRepoDao;
import com.epam.traing.gitcl.db.dao.RepoDao;
import com.epam.traing.gitcl.network.GitHubRepoClient;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Yahor_Fralou on 3/21/2017 6:40 PM.
 */

@Module
public class SearchModule {

    @Provides
    @Singleton
    GitHubRepoClient provideRepoClient(@Named("apiRetrofit") Retrofit apiRetrofit) {
        return apiRetrofit.create(GitHubRepoClient.class);
    }

    @Provides
    @Singleton
    IRepoDao provideRepoDao(StorIOSQLite sqLite) {
        return new RepoDao(sqLite);
    }
}
