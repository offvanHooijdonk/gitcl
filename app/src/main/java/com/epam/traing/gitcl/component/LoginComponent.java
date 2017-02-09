package com.epam.traing.gitcl.component;

import com.epam.traing.gitcl.db.DBModule;
import com.epam.traing.gitcl.interactor.authenticate.AuthenticatorModule;
import com.epam.traing.gitcl.interactor.authenticate.LoginScope;
import com.epam.traing.gitcl.network.NetworkModule;
import com.epam.traing.gitcl.presentation.presenter.PresenterModule;
import com.epam.traing.gitcl.presentation.ui.LoginActivity;

import dagger.Component;

/**
 * Created by Yahor_Fralou on 1/27/2017 6:25 PM.
 */

@Component(modules = {
        PresenterModule.class,
        AuthenticatorModule.class,/*,
        DBModule.class,
        NetworkModule.class*/
        DBModule.class, NetworkModule.class
},
        dependencies = {AppComponent.class/*AuthenticatorComponent.class*/})
@LoginScope
public interface LoginComponent {
    void inject(LoginActivity loginActivity);
}
