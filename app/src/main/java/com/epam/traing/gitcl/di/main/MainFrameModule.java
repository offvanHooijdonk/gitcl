package com.epam.traing.gitcl.di.main;

import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.data.interactor.account.AccountInteractor;
import com.epam.traing.gitcl.data.interactor.account.IAccountInteractor;
import com.epam.traing.gitcl.db.dao.IAccountDao;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.helper.SessionHelper;
import com.epam.traing.gitcl.network.GitHubUserClient;
import com.epam.traing.gitcl.presentation.presenter.IMainPresenter;
import com.epam.traing.gitcl.presentation.presenter.MainPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 2/16/2017 6:26 PM.
 */

@Module
public class MainFrameModule {
    @Provides
    @MainFrameScope
    IMainPresenter provideMainPresenter(IAccountInteractor accountInteractor, PrefHelper prefHelper) {
        return new MainPresenter(accountInteractor, prefHelper);
    }

    @Provides
    @MainFrameScope
    public IAccountInteractor provideAccountInteractor(GitHubUserClient userClient,
                                                       IAccountDao accountDao,
                                                       SessionHelper sessionHelper,
                                                       ModelConverter modelConverter) {
        return new AccountInteractor(userClient, accountDao, sessionHelper, modelConverter);
    }
}
