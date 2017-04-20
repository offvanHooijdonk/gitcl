package com.epam.traing.gitcl.di;

import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.network.AuthenticationInterceptor;
import com.epam.traing.gitcl.network.Constants;
import com.epam.traing.gitcl.network.GitHubUserClient;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yahor_Fralou on 2/3/2017 11:13 AM.
 */

@Module
@Singleton
public class NetworkModule {
    public static final String RETROFIT_OAUTH = "oauthRetrofit";
    public static final String RETROFIT_API = "apiRetrofit";

    @Provides
    @Singleton
    public AuthenticationInterceptor getAuthInterceptor(PrefHelper prefHelper) {
        return new AuthenticationInterceptor(prefHelper);
    }

    @Provides
    @Singleton
    public OkHttpClient getHttpClient(AuthenticationInterceptor authenticationInterceptor) {
        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addNetworkInterceptor(authenticationInterceptor)
                .build();
    }

    @Provides
    @Singleton
    @Named(RETROFIT_OAUTH)
    public Retrofit getOAuthTokenRetrofit(OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constants.Api.OAUTH_LOGIN_BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @Named(RETROFIT_API)
    public Retrofit getApiRetrofit(OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constants.Api.API_BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public GitHubUserClient getUserClient(@Named(RETROFIT_API) Retrofit apiRetrofit) {
        return apiRetrofit.create(GitHubUserClient.class);
    }

}
