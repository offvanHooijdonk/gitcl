package com.epam.traing.gitcl.di;

import android.content.Context;

import com.epam.traing.gitcl.di.application.AppComponent;
import com.epam.traing.gitcl.di.application.AppModule;
import com.epam.traing.gitcl.di.application.DBModule;
import com.epam.traing.gitcl.di.application.DaggerAppComponent;
import com.epam.traing.gitcl.di.application.NetworkModule;
import com.epam.traing.gitcl.di.presentation.login.AuthenticatorModule;
import com.epam.traing.gitcl.di.presentation.login.LoginScreenComponent;
import com.epam.traing.gitcl.di.presentation.login.LoginScreenModule;
import com.epam.traing.gitcl.di.presentation.main.MainScreenComponent;
import com.epam.traing.gitcl.di.presentation.main.MainScreenModule;
import com.epam.traing.gitcl.di.presentation.repositories.RepoScreenComponent;
import com.epam.traing.gitcl.di.presentation.repositories.RepoScreenModule;
import com.epam.traing.gitcl.di.util.AccountInteractorModule;
import com.epam.traing.gitcl.di.util.AccountModule;
import com.epam.traing.gitcl.di.util.AuthModule;
import com.epam.traing.gitcl.di.util.RepoInteractorModule;
import com.epam.traing.gitcl.di.util.RepoModule;

/**
 * Created by Yahor_Fralou on 4/20/2017 12:42 PM.
 */

public class DependencyManager {
    private static AppComponent appComponent;
    private static LoginScreenComponent loginScreenComponent;
    private static MainScreenComponent mainScreenComponent;
    private static RepoScreenComponent repoScreenComponent;

    public static void initAppComponent(Context ctx) {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(ctx))
                    .networkModule(new NetworkModule())
                    .dBModule(new DBModule())
                    .build();
        }
    }


    private static AppComponent getAppComponent() {
        if (appComponent == null) {
            throw new RuntimeException("DI not initialized!"); // TODO custom exception
        }
        return appComponent;
    }

    public static LoginScreenComponent getLoginScreenComponent() {
        if (loginScreenComponent == null) {
            loginScreenComponent = getAppComponent()
                    .plusAuthenticateComponent(new AuthModule(), new AccountModule())
                    .plusLoginScreenComponent(new LoginScreenModule(), new AuthenticatorModule());
        }

        return loginScreenComponent;
    }

    public static MainScreenComponent getMainScreenComponent() {
        if (mainScreenComponent == null) {
            mainScreenComponent = getAppComponent()
                    .plusAccountComponent(new AccountModule(), new AccountInteractorModule())
                    .plusMainScreenComponent(new MainScreenModule());
        }

        return mainScreenComponent;
    }

    public static RepoScreenComponent getRepoScreenComponent() {
        if (repoScreenComponent == null) {
            repoScreenComponent = getAppComponent()
                    .plusRepoComponent(new RepoModule(), new RepoInteractorModule(), new AccountModule(), new AccountInteractorModule())
                    .plusRepoScreenComponent(new RepoScreenModule());
        }

        return repoScreenComponent;
    }
}
