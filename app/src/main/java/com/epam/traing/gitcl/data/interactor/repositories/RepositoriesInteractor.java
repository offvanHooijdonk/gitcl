package com.epam.traing.gitcl.data.interactor.repositories;

import com.epam.traing.gitcl.network.GitHubRepoClient;

import javax.inject.Inject;

/**
 * Created by Yahor_Fralou on 2/22/2017 12:31 PM.
 */

public class RepositoriesInteractor implements IRepositoriesInteractor {

    private GitHubRepoClient repoClient;

    @Inject
    public RepositoriesInteractor(GitHubRepoClient repoClient) {
        this.repoClient = repoClient;
    }
}
