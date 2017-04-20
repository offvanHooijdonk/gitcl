package com.epam.traing.gitcl.di;

import android.content.Context;

import com.epam.traing.gitcl.di.login.AuthApiModule;
import com.epam.traing.gitcl.di.login.AuthenticatorModule;
import com.epam.traing.gitcl.di.login.DaggerLoginComponent;
import com.epam.traing.gitcl.di.login.LoginComponent;
import com.epam.traing.gitcl.di.login.LoginModule;
import com.epam.traing.gitcl.di.main.AccountModule;
import com.epam.traing.gitcl.di.main.MainFrameComponent;
import com.epam.traing.gitcl.di.main.MainFrameModule;
import com.epam.traing.gitcl.di.repositories.RepositoryComponent;
import com.epam.traing.gitcl.di.repositories.RepositoryModule;

/**
 * Created by Yahor_Fralou on 4/20/2017 12:42 PM.
 */

public class DependencyManager {
    private static AppComponent appComponent;
    private static LoginComponent loginComponent;
    private static MainFrameComponent mainFrameComponent;
    private static RepositoryComponent repositoryComponent;

    public static AppComponent getAppComponent(Context ctx) {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(ctx))
                    .networkModule(new NetworkModule())
                    .dBModule(new DBModule())
                    .accountModule(new AccountModule())
                    .searchModule(new SearchModule())
                    .build();
        }
        return appComponent;
    }

    public static LoginComponent getLoginComponent() {
        if (loginComponent == null) {
            loginComponent = DaggerLoginComponent.builder()
                    .loginModule(new LoginModule())
                    .authenticatorModule(new AuthenticatorModule())
                    .authApiModule(new AuthApiModule()).build();
        }

        return loginComponent;
    }

    public static MainFrameComponent getMainFrameComponent(Context ctx) {
        if (mainFrameComponent == null) {
            mainFrameComponent = getAppComponent(ctx)
                    .plusMainFrameComponent(new MainFrameModule());
        }

        return mainFrameComponent;
    }

    public static RepositoryComponent getRepositoryComponent(Context ctx) {
        if (repositoryComponent == null) {
            repositoryComponent = getMainFrameComponent(ctx)
                    .plusRepositoryComponent(new RepositoryModule());
        }

        return repositoryComponent;
    }
}
