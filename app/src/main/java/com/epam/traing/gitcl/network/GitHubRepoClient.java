package com.epam.traing.gitcl.network;

import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.network.json.RepoJson;
import com.epam.traing.gitcl.network.json.RepoSearchResults;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Yahor_Fralou on 2/22/2017 12:39 PM.
 */

public interface GitHubRepoClient {

    @GET("/user/repos")
    Observable<List<RepoJson>> getAccountRepositories();

    @GET("/repos/{accountName}/{repoName}/contributors")
    // TODO fix to AccountJson ?
    Observable<List<AccountModel>> getContributors(@Path("accountName") String accountName, @Path("repoName") String repoName);

    @GET("/repos/{accountName}/{repoName}")
    Observable<RepoJson> loadRepoInfo(@Path("accountName") String accountName, @Path("repoName") String repoName);

    @GET("/search/repositories")
    Observable<RepoSearchResults> searchRepositories(@Query("q") String queryText, @Query("page") int page);
}
