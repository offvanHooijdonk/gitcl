package com.epam.traing.gitcl.network;

import com.epam.traing.gitcl.network.json.AccountJson;

import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

/**
 * Created by Yahor_Fralou on 2/3/2017 12:21 PM.
 */

public interface GitHubUserClient {
// TODO rename to a Login Client for it needs AccessToken passed, and other User interaction can go
// through another Client that has AccessToken already set
    @GET("/user")
    public Observable<AccountJson> getUserInfo(@Header("Authorization") String accessToken);
}
