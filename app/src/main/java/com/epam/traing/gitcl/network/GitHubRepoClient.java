package com.epam.traing.gitcl.network;

import com.epam.traing.gitcl.network.json.RepoJson;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Yahor_Fralou on 2/22/2017 12:39 PM.
 */

public interface GitHubRepoClient {

    @GET("/user/repos")
    Observable<RepoJson> getAccountRepositories();
}
