package com.epam.traing.gitcl.di.presentation.main;

import com.epam.traing.gitcl.data.interactor.account.IAccountInteractor;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.presentation.presenter.AccountPresenter;
import com.epam.traing.gitcl.presentation.presenter.IAccountPresenter;
import com.epam.traing.gitcl.presentation.presenter.IMainPresenter;
import com.epam.traing.gitcl.presentation.presenter.MainPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 2/16/2017 6:26 PM.
 */

@Module
public class MainScreenModule {
    @Provides
    @MainScreenScope
    IMainPresenter provideMainPresenter(IAccountInteractor accountInteractor, PrefHelper prefHelper) {
        return new MainPresenter(accountInteractor, prefHelper);
    }

    @Provides
    @MainScreenScope
    IAccountPresenter provideAccountPresenter() {
        return new AccountPresenter();
    }

/*
    @Provides
    @MainScreenScope
    public ISearchIntercator provideSearchIntercator(IHistoryDao historyDao, IRepoDao repoDao, IAccountDao accountDao,
                                                     GitHubRepoClient repoClient, GitHubUserClient userClient, ModelConverter modelConverter) {
        return new SearchInteractor(historyDao, repoDao, accountDao, repoClient, userClient, modelConverter);
    }*/

}
