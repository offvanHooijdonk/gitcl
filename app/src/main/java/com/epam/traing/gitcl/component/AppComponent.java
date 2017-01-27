package com.epam.traing.gitcl.component;

import com.epam.traing.gitcl.presenter.PresenterModule;
import com.epam.traing.gitcl.ui.LoginActivity;

import dagger.Component;

/**
 * Created by Yahor_Fralou on 1/26/2017 11:40 AM.
 */

@Component(modules = {PresenterModule.class})
public interface AppComponent {
    LoginActivity inject(LoginActivity loginActivity);
}
