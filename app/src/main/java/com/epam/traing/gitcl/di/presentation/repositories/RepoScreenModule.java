package com.epam.traing.gitcl.di.presentation.repositories;

import com.epam.traing.gitcl.data.interactor.account.IAccountInteractor;
import com.epam.traing.gitcl.data.interactor.repositories.IRepositoriesInteractor;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.presentation.presenter.IRepoListPresenter;
import com.epam.traing.gitcl.presentation.presenter.RepoListPresenter;
import com.epam.traing.gitcl.presentation.ui.IRepoInfoPresenter;
import com.epam.traing.gitcl.presentation.ui.RepoInfoPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 2/22/2017 12:03 PM.
 */

@Module
public class RepoScreenModule {

    @Provides
    @RepoScreenScope
    IRepoListPresenter provideRepoListPresenter(IRepositoriesInteractor repositoriesInteractor, PrefHelper prefHelper) {
        return new RepoListPresenter(repositoriesInteractor, prefHelper);
    }

    @Provides
    @RepoScreenScope
    IRepoInfoPresenter provideRepoInfoPresenter(IRepositoriesInteractor repositoriesInteractor, IAccountInteractor accountInteractor, PrefHelper prefHelper) {
        return new RepoInfoPresenter(repositoriesInteractor, accountInteractor, prefHelper);
    }

}
