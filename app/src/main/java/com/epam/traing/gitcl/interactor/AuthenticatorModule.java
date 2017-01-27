package com.epam.traing.gitcl.interactor;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 1/27/2017 6:26 PM.
 */

@Module
public class AuthenticatorModule {

    @Provides
    IAuthenticator provideAuthenticator() {
        return new GitAuthenticator();
    }
}