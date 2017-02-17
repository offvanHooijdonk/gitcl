package com.epam.traing.gitcl.di.login;

import com.epam.traing.gitcl.db.dao.AccountDao;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.helper.SessionHelper;
import com.epam.traing.gitcl.interactor.authenticate.GitAuthenticator;
import com.epam.traing.gitcl.interactor.authenticate.IAuthenticator;
import com.epam.traing.gitcl.network.GitHubTokenClient;
import com.epam.traing.gitcl.network.GitHubUserClient;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 1/27/2017 6:26 PM.
 */

@Module
public class AuthenticatorModule {

    @Provides
    @LoginScope
    IAuthenticator provideAuthenticator(PrefHelper prefHelper,
                                        GitHubTokenClient tokenClient,
                                        GitHubUserClient userClient,
                                        AccountDao accountDao,
                                        SessionHelper session) {
        return new GitAuthenticator(prefHelper, tokenClient, userClient, accountDao, session);
    }
}
