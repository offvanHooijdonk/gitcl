package com.epam.traing.gitcl.component;

import com.epam.traing.gitcl.interactor.AuthenticatorModule;
import com.epam.traing.gitcl.interactor.LoginScope;
import com.epam.traing.gitcl.presenter.LoginPresenter;

import dagger.Component;

/**
 * Created by Yahor_Fralou on 1/27/2017 6:25 PM.
 */

@Component(modules = {AuthenticatorModule.class})
@LoginScope
public interface LoginComponent {
    void inject(LoginPresenter loginPresenter);
}
