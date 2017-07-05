package com.epam.traing.gitcl.di;

import android.content.Context;

import com.epam.traing.gitcl.di.application.AppComponent;
import com.epam.traing.gitcl.di.application.AppModule;
import com.epam.traing.gitcl.di.application.DaggerAppComponent;
import com.epam.traing.gitcl.di.presentation.login.LoginScreenComponent;
import com.epam.traing.gitcl.di.presentation.login.LoginScreenModule;
import com.epam.traing.gitcl.di.presentation.main.MainScreenComponent;
import com.epam.traing.gitcl.di.presentation.repositories.RepoScreenComponent;
import com.epam.traing.gitcl.di.presentation.repositories.RepoScreenModule;
import com.epam.traing.gitcl.di.presentation.search.SearchScreenComponent;
import com.epam.traing.gitcl.di.presentation.search.SearchScreenModule;

/**
 * Created by Yahor_Fralou on 4/20/2017 12:42 PM.
 */

public class DependencyManager {
    private static AppComponent appComponent;
    private static LoginScreenComponent loginScreenComponent;
    private static MainScreenComponent mainScreenComponent;
    private static RepoScreenComponent repoScreenComponent;
    private static SearchScreenComponent searchScreenComponent;

    public static void initAppComponent(Context ctx) {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(ctx))
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
                    .plusAuthenticateComponent()
                    .plusLoginScreenComponent(new LoginScreenModule());
        }

        return loginScreenComponent;
    }

    public static void releaseLoginScreenComponent() {
        loginScreenComponent = null;
    }

    public static MainScreenComponent getMainScreenComponent() {
        if (mainScreenComponent == null) {
            mainScreenComponent = getAppComponent()
                    .plusAccountComponent()
                    .plusMainScreenComponent();
        }

        return mainScreenComponent;
    }

    public static void releaseMainScreenComponent() {
        mainScreenComponent = null;
    }

    public static RepoScreenComponent getRepoScreenComponent() {
        if (repoScreenComponent == null) {
            repoScreenComponent = getAppComponent()
                    .plusRepoComponent()
                    .plusRepoScreenComponent(new RepoScreenModule());
        }

        return repoScreenComponent;
    }

    public static void releaseRepoScreenComponent() {
        repoScreenComponent = null;
    }

    public static SearchScreenComponent getSearchScreenComponent() {
        if (searchScreenComponent == null) {
            searchScreenComponent = getAppComponent()
                    .plusSearchComponent()
                    .plusSearchScreenComponent(new SearchScreenModule());
        }

        return searchScreenComponent;
    }

    public static void releaseSearchScreenComponent() {
        searchScreenComponent = null;
    }

}
