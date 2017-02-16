package com.epam.traing.gitcl.component;

import android.content.Context;

import com.epam.traing.gitcl.app.AppModule;
import com.epam.traing.gitcl.db.DBModule;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.interactor.authenticate.AuthenticatorModule;
import com.epam.traing.gitcl.network.NetworkModule;
import com.epam.traing.gitcl.presentation.presenter.LoginModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Yahor_Fralou on 1/26/2017 11:40 AM.
 */

@Component(modules = {
        AppModule.class,
        DBModule.class,
        NetworkModule.class})
@Singleton
public interface AppComponent {
    Context getContext();
    PrefHelper getPreferenceHelper();

    LoginComponent plusLoginComponent(LoginModule loginModule, AuthenticatorModule authenticatorModule);
}
