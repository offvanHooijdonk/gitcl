package com.epam.traing.gitcl.di;

import android.content.Context;

import com.epam.traing.gitcl.di.login.AuthApiModule;
import com.epam.traing.gitcl.di.login.AuthenticatorModule;
import com.epam.traing.gitcl.di.login.LoginComponent;
import com.epam.traing.gitcl.di.login.LoginModule;
import com.epam.traing.gitcl.di.main.AccountModule;
import com.epam.traing.gitcl.di.main.MainFrameComponent;
import com.epam.traing.gitcl.di.main.MainFrameModule;
import com.epam.traing.gitcl.di.repositories.RepositoryComponent;
import com.epam.traing.gitcl.di.repositories.RepositoryModule;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.helper.SessionHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Yahor_Fralou on 1/26/2017 11:40 AM.
 */

@Component(modules = {
        AppModule.class,
        DBModule.class,
        NetworkModule.class,
        AccountModule.class
})
@Singleton
public interface AppComponent {

    Context getContext();
    PrefHelper getPreferenceHelper();
    SessionHelper getSession();

    LoginComponent plusLoginComponent(LoginModule loginModule,
                                      AuthenticatorModule authenticatorModule,
                                      AuthApiModule authApiModule);

    MainFrameComponent plusMainFrameComponent(MainFrameModule mainFrameModule);

    RepositoryComponent plusRepositoryComponent(RepositoryModule repositoryModule);
}
