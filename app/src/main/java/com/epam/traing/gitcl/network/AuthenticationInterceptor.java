package com.epam.traing.gitcl.network;

import com.epam.traing.gitcl.helper.PrefHelper;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Yahor_Fralou on 2/8/2017 12:12 PM.
 */

public class AuthenticationInterceptor implements Interceptor {
    private PrefHelper prefHelper;

    @Inject
    public AuthenticationInterceptor(PrefHelper prefHelper) {
        this.prefHelper = prefHelper;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String accessToken = prefHelper.getAccessToken();
        String tokenType = prefHelper.getTokenType();

        Request request = chain.request();
        if (accessToken != null && tokenType != null) {
            request = request.newBuilder().addHeader(Constants.Api.OAUTH_AUTH_HEADER, tokenType + " " + accessToken).build();
        }

        return chain.proceed(request);
    }
}
