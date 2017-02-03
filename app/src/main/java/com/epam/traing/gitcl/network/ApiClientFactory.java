package com.epam.traing.gitcl.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yahor_Fralou on 2/3/2017 11:19 AM.
 */

public class ApiClientFactory {
    // TODO find proper way to store this
    private static final String OAUTH_LOGIN_BASE_URL = "https://github.com";
    private static final String API_BASE_URL = "https://api.github.com";

    private Retrofit oauthLoginRetrofit;
    private Retrofit apiRetrofit;

    protected Retrofit getOAuthTokenRetrofit() {
        if (oauthLoginRetrofit == null) {
            oauthLoginRetrofit = new Retrofit.Builder()
                    .baseUrl(OAUTH_LOGIN_BASE_URL)
                    .client(new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return oauthLoginRetrofit;
    }

    protected Retrofit getApiRetrofit() {
        if (apiRetrofit == null) {
            apiRetrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .client(new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return apiRetrofit;
    }

    public GitHubUserClient getUserClient() {
        return getApiRetrofit().create(GitHubUserClient.class);
    }

    public GitHubTokenClient getTokenClient() {
        return getOAuthTokenRetrofit().create(GitHubTokenClient.class);
    }

}
