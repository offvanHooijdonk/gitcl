package com.epam.traing.gitcl.di.util;

import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.data.interactor.search.CompositeSearchInteractor;
import com.epam.traing.gitcl.data.interactor.search.ICompositeSearchInteractor;
import com.epam.traing.gitcl.data.interactor.search.ISearchInteractor;
import com.epam.traing.gitcl.data.interactor.search.SearchInteractor;
import com.epam.traing.gitcl.db.dao.IAccountDao;
import com.epam.traing.gitcl.db.dao.IHistoryDao;
import com.epam.traing.gitcl.db.dao.IRepoDao;
import com.epam.traing.gitcl.network.GitHubRepoClient;
import com.epam.traing.gitcl.network.GitHubUserClient;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 5/4/2017 7:01 PM.
 */

@Module
public class SearchInteractorModule {
    @Provides
    @UtilsScope
    public ISearchInteractor provideSearchInteractor(IHistoryDao historyDao, IRepoDao repoDao, IAccountDao accountDao,
                                                     GitHubRepoClient repoClient, GitHubUserClient userClient, ModelConverter modelConverter) {
        return new SearchInteractor(historyDao, repoDao, accountDao, repoClient, userClient, modelConverter);
    }

    @Provides
    @UtilsScope
    public ICompositeSearchInteractor provideCompositeSearchInteractor(ISearchInteractor searchInteractor) {
        return new CompositeSearchInteractor(searchInteractor);
    }
}
