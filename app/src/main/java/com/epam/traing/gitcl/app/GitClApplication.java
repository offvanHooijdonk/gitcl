package com.epam.traing.gitcl.app;

import android.app.Application;
import android.preference.PreferenceManager;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.component.AppComponent;
import com.epam.traing.gitcl.component.DaggerAppComponent;
import com.epam.traing.gitcl.component.LoginComponent;
import com.epam.traing.gitcl.db.DBModule;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.interactor.authenticate.AuthenticatorModule;
import com.epam.traing.gitcl.network.NetworkModule;
import com.epam.traing.gitcl.presentation.presenter.LoginModule;

/**
 * Created by off on 22.01.2017.
 */

public class GitClApplication extends Application {
    public static final String LOG = "githubcl";
    private static AppComponent appComponent;
    private static LoginComponent loginComponent;
    /*private static AuthenticatorComponent authenticatorComponent;*/

    private static AccountModel accountModel;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .dBModule(new DBModule())
                .build();
        /*getLoginComponent();
        getAuthenticatorComponent();*/


        PreferenceManager.setDefaultValues(this, R.xml.pref, false);
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public static LoginComponent getLoginComponent() {
        if (loginComponent == null) {
            loginComponent = getAppComponent()
                    .plusLoginComponent(new LoginModule(), new AuthenticatorModule());
        }

        return loginComponent;
    }

    /*public static AuthenticatorComponent getAuthenticatorComponent() {
        if (authenticatorComponent == null) {
            authenticatorComponent = DaggerAuthenticatorComponent.builder()
                    .appComponent(getAppComponent())
                    .authenticatorModule(new AuthenticatorModule())
                    .networkModule(new NetworkModule())
                    .dBModule(new DBModule())
                    .build();
        }

        return authenticatorComponent;
    }*/

    public static void setAccount(AccountModel account) {
        accountModel = account;
    }

    public static AccountModel getAccount() {
        return accountModel;
    }
}
