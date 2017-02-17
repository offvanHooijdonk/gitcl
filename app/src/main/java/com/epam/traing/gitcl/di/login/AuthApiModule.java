package com.epam.traing.gitcl.di.login;

import com.epam.traing.gitcl.network.GitHubTokenClient;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Yahor_Fralou on 2/17/2017 12:39 PM.
 */

@Module
public class AuthApiModule {

    @Provides
    @LoginScope
    public GitHubTokenClient getTokenClient(@Named("oauthRetrofit") Retrofit oauthRetrofit) {
        return oauthRetrofit.create(GitHubTokenClient.class);
    }
}
