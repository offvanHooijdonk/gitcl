package com.epam.traing.gitcl.presentation.presenter;

import com.epam.traing.gitcl.interactor.authenticate.GitAuthenticator;
import com.epam.traing.gitcl.interactor.authenticate.LoginScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 1/27/2017 12:08 PM.
 */

@Module()
public class PresenterModule {

    @Provides
    @LoginScope
    //@Singleton
    ILoginPresenter provideLoginPresenter(GitAuthenticator authenticator) {
        return new LoginPresenter(authenticator);
    }
}
