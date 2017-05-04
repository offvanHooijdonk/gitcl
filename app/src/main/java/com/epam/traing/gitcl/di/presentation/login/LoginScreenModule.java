package com.epam.traing.gitcl.di.presentation.login;

import com.epam.traing.gitcl.data.interactor.authenticate.IAuthenticator;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.presentation.presenter.ILoginPresenter;
import com.epam.traing.gitcl.presentation.presenter.LoginPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 1/27/2017 12:08 PM.
 */

@Module()
public class LoginScreenModule {

    @Provides
    @LoginScreenScope
    ILoginPresenter provideLoginPresenter(IAuthenticator authenticator, PrefHelper prefHelper) {
        return new LoginPresenter(authenticator, prefHelper);
    }
}
