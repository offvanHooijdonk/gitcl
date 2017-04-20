package com.epam.traing.gitcl.di.login;

import com.epam.traing.gitcl.di.AppComponent;
import com.epam.traing.gitcl.presentation.ui.LoginActivity;

import dagger.Component;

/**
 * Created by Yahor_Fralou on 1/27/2017 6:25 PM.
 */

/*@Subcomponent(modules = {
        LoginModule.class,
        AuthenticatorModule.class,
        AuthApiModule.class
})*/
@Component(modules = {
        LoginModule.class,
        AuthenticatorModule.class,
        AuthApiModule.class
}, dependencies = {AppComponent.class})
@LoginScope
public interface LoginComponent {
    void inject(LoginActivity loginActivity);
}
