package com.epam.traing.gitcl.component;

import com.epam.traing.gitcl.interactor.authenticate.AuthenticatorModule;
import com.epam.traing.gitcl.presentation.presenter.LoginModule;
import com.epam.traing.gitcl.presentation.ui.LoginActivity;

import dagger.Subcomponent;

/**
 * Created by Yahor_Fralou on 1/27/2017 6:25 PM.
 */

@Subcomponent(modules = {
        LoginModule.class,
        AuthenticatorModule.class
})
@LoginScope
public interface LoginComponent {
    void inject(LoginActivity loginActivity);
}
