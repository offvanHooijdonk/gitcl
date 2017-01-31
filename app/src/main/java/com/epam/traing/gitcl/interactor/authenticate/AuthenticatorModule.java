package com.epam.traing.gitcl.interactor.authenticate;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 1/27/2017 6:26 PM.
 */

@Module
public class AuthenticatorModule {

    @Provides
    @LoginScope
    IAuthenticator provideAuthenticator() {
        return new GitAuthenticator();
    }
}
