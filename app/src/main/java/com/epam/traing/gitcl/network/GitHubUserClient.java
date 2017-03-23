package com.epam.traing.gitcl.network;

import com.epam.traing.gitcl.network.json.AccountJson;
import com.epam.traing.gitcl.network.json.AccountSearchResults;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Yahor_Fralou on 2/3/2017 12:21 PM.
 */

public interface GitHubUserClient {
    @GET("/user")
    Observable<AccountJson> getCurrentUserInfo();

    @GET("/users/{userName}")
    Observable<AccountJson> getUserInfo(@Path("userName") String userName);

    @GET("/search/users")
    Observable<AccountSearchResults> searchUsers(@Query("q") String queryText, @Query("page") int page);
}
