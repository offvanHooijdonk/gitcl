package com.epam.traing.gitcl.di;

import android.content.Context;

import com.epam.traing.gitcl.di.login.AuthDaoModule;
import com.epam.traing.gitcl.di.login.AuthApiModule;
import com.epam.traing.gitcl.di.login.LoginComponent;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.helper.SessionHelper;
import com.epam.traing.gitcl.di.login.AuthenticatorModule;
import com.epam.traing.gitcl.di.login.LoginModule;
import com.epam.traing.gitcl.presentation.presenter.MainFrameModule;
import com.epam.traing.gitcl.presentation.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Yahor_Fralou on 1/26/2017 11:40 AM.
 */

@Component(modules = {
        AppModule.class,
        MainFrameModule.class,
        DBModule.class,
        NetworkModule.class})
@Singleton
public interface AppComponent {
    void inject(MainActivity mainActivity);

    Context getContext();
    PrefHelper getPreferenceHelper();
    SessionHelper getSession();

    LoginComponent plusLoginComponent(LoginModule loginModule,
                                      AuthenticatorModule authenticatorModule,
                                      AuthApiModule authApiModule,
                                      AuthDaoModule accountDaoModule);
}
