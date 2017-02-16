package com.epam.traing.gitcl.presentation.presenter;

import com.epam.traing.gitcl.component.LoginScope;
import com.epam.traing.gitcl.interactor.authenticate.IAuthenticator;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 1/27/2017 12:08 PM.
 */

@Module()
public class LoginModule {

    @Provides
    @LoginScope
    ILoginPresenter provideLoginPresenter(IAuthenticator authenticator) {
        return new LoginPresenter(authenticator);
    }
}
