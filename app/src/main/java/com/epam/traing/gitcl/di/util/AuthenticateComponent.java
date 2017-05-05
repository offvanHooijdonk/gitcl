package com.epam.traing.gitcl.di.util;

import com.epam.traing.gitcl.di.presentation.login.LoginScreenComponent;
import com.epam.traing.gitcl.di.presentation.login.LoginScreenModule;

import dagger.Subcomponent;

/**
 * Created by Yahor_Fralou on 5/3/2017 6:14 PM.
 */

@Subcomponent(modules = {AccountModule.class, AuthModule.class, AuthInteractorModule.class})
@UtilsScope
public interface AuthenticateComponent {

    LoginScreenComponent plusLoginScreenComponent(LoginScreenModule loginScreenModule);
}
