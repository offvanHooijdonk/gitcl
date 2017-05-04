package com.epam.traing.gitcl.di.util;

import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.data.interactor.repositories.IRepositoriesInteractor;
import com.epam.traing.gitcl.data.interactor.repositories.RepositoriesInteractor;
import com.epam.traing.gitcl.db.dao.IRepoDao;
import com.epam.traing.gitcl.network.GitHubRepoClient;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 5/4/2017 5:17 PM.
 */

@Module
public class RepoInteractorModule {
    @Provides
    @UtilsScope
    public IRepositoriesInteractor provideRepositoriesInteractor(GitHubRepoClient repoClient, IRepoDao repoDao, ModelConverter modelConverter) {
        return new RepositoriesInteractor(repoClient, repoDao, modelConverter);
    }
}
