package com.epam.traing.gitcl.di.repositories;

import com.epam.traing.gitcl.data.interactor.repositories.IRepositoriesInteractor;
import com.epam.traing.gitcl.data.interactor.repositories.RepositoriesInteractor;
import com.epam.traing.gitcl.network.GitHubRepoClient;
import com.epam.traing.gitcl.presentation.presenter.IRepoListPresenter;
import com.epam.traing.gitcl.presentation.presenter.RepoListPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Yahor_Fralou on 2/22/2017 12:03 PM.
 */

@Module
public class RepositoryModule {

    @Provides
    @RepositoryScope
    IRepoListPresenter provideRepoListPresenter(IRepositoriesInteractor repositoriesInteractor) {
        return new RepoListPresenter(repositoriesInteractor);
    }

    @Provides
    @RepositoryScope
    IRepositoriesInteractor provideRepositoriesInteractor(GitHubRepoClient repoClient) {
        return new RepositoriesInteractor(repoClient);
    }

    @Provides
    @RepositoryScope
    GitHubRepoClient provideRepoClient(@Named("apiRetrofit") Retrofit apiRetrofit) {
        return apiRetrofit.create(GitHubRepoClient.class);
    }

}
