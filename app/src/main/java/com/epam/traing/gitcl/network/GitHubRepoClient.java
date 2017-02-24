package com.epam.traing.gitcl.network;

import com.epam.traing.gitcl.network.json.RepoJson;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Yahor_Fralou on 2/22/2017 12:39 PM.
 */

public interface GitHubRepoClient {

    @GET("/user/repos")
    Observable<List<RepoJson>> getAccountRepositories();
}
