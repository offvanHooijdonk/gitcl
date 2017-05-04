package com.epam.traing.gitcl.di.presentation.login;

import com.epam.traing.gitcl.presentation.ui.LoginActivity;

import dagger.Subcomponent;

/**
 * Created by Yahor_Fralou on 1/27/2017 6:25 PM.
 */

/*@Subcomponent(modules = {
        LoginScreenModule.class,
        AuthenticatorModule.class,
        AuthModule.class
})*/
@Subcomponent(modules = {
        LoginScreenModule.class,
        AuthenticatorModule.class
})
@LoginScreenScope
public interface LoginScreenComponent {
    void inject(LoginActivity loginActivity);
}
