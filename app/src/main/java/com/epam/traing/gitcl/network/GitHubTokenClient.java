package com.epam.traing.gitcl.network;

import com.epam.traing.gitcl.network.json.AccessTokenJson;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Yahor_Fralou on 2/3/2017 11:03 AM.
 */


public interface GitHubTokenClient {

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("/login/oauth/access_token")
    Observable<AccessTokenJson> requestAccessToken(@Field("client_id") String appKey,
                                                   @Field("client_secret") String appSecret,
                                                   @Field("code") String accCode);
}
