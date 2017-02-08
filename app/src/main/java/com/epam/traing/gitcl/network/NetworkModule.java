package com.epam.traing.gitcl.network;

import com.epam.traing.gitcl.helper.PrefHelper;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;

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
public class NetworkModule {
// TODO setup Scopes

    @Provides
    public AuthenticationInterceptor getAuthInterceptor(PrefHelper prefHelper) {
        return new AuthenticationInterceptor(prefHelper);
    }

    @Provides
    public OkHttpClient getHttpClient(AuthenticationInterceptor authenticationInterceptor) {
        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addNetworkInterceptor(authenticationInterceptor)
                .build();
    }

    @Provides
    @Named("oauthRetrofit")
    public Retrofit getOAuthTokenRetrofit(OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constants.Api.OAUTH_LOGIN_BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Named("apiRetrofit")
    public Retrofit getApiRetrofit(OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constants.Api.API_BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    public GitHubTokenClient getTokenClient(@Named("oauthRetrofit") Retrofit oauthRetrofit) {
        return oauthRetrofit.create(GitHubTokenClient.class);
    }

    @Provides
    public GitHubUserClient getUserClient(@Named("apiRetrofit") Retrofit apiRetrofit) {
        return apiRetrofit.create(GitHubUserClient.class);
    }

}
