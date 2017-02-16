package com.epam.traing.gitcl.interactor.authenticate;

import com.epam.traing.gitcl.component.LoginScope;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.helper.SessionHelper;
import com.epam.traing.gitcl.network.GitHubTokenClient;
import com.epam.traing.gitcl.network.GitHubUserClient;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 1/27/2017 6:26 PM.
 */

@Module
public class AuthenticatorModule {

    @Provides
    @LoginScope
    IAuthenticator provideAuthenticator(StorIOSQLite storIOSQLite, PrefHelper prefHelper, GitHubTokenClient tokenClient, GitHubUserClient userClient, SessionHelper session) {
        return new GitAuthenticator(storIOSQLite, prefHelper, tokenClient, userClient, session);
    }
}
