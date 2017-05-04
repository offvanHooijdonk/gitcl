package com.epam.traing.gitcl.di.util;

import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.data.interactor.account.AccountInteractor;
import com.epam.traing.gitcl.data.interactor.account.IAccountInteractor;
import com.epam.traing.gitcl.db.dao.IAccountDao;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.helper.SessionHelper;
import com.epam.traing.gitcl.network.GitHubUserClient;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 5/4/2017 4:20 PM.
 */

@Module
public class AccountInteractorModule {

    @Provides
    @UtilsScope
    public IAccountInteractor provideAccountInteractor(GitHubUserClient userClient,
                                                       IAccountDao accountDao,
                                                       SessionHelper sessionHelper,
                                                       PrefHelper prefHelper,
                                                       ModelConverter modelConverter) {
        return new AccountInteractor(userClient, accountDao, sessionHelper, prefHelper, modelConverter);
    }
}
