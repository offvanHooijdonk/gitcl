package com.epam.traing.gitcl.di.util;

import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.data.interactor.authenticate.GitAuthenticator;
import com.epam.traing.gitcl.data.interactor.authenticate.IAuthenticator;
import com.epam.traing.gitcl.db.dao.IAccountDao;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.helper.SessionHelper;
import com.epam.traing.gitcl.network.GitHubTokenClient;
import com.epam.traing.gitcl.network.GitHubUserClient;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 1/27/2017 6:26 PM.
 */

@Module
public class AuthInteractorModule {

    @Provides
    @UtilsScope
    IAuthenticator provideAuthenticator(PrefHelper prefHelper,
                                        GitHubTokenClient tokenClient,
                                        GitHubUserClient userClient,
                                        IAccountDao accountDao,
                                        SessionHelper session,
                                        ModelConverter modelConverter) {
        return new GitAuthenticator(prefHelper, tokenClient, userClient, accountDao, session, modelConverter);
    }
}
