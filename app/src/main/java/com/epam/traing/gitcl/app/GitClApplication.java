package com.epam.traing.gitcl.app;

import android.app.Application;
import android.preference.PreferenceManager;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.component.AppComponent;
import com.epam.traing.gitcl.component.DaggerAppComponent;
import com.epam.traing.gitcl.component.DaggerLoginPresenterComponent;
import com.epam.traing.gitcl.component.LoginPresenterComponent;
import com.epam.traing.gitcl.model.AccountModel;

/**
 * Created by off on 22.01.2017.
 */

public class GitClApplication extends Application {
    private static AppComponent appComponent;
    private static LoginPresenterComponent loginPresenterComponent;

    private static AccountModel accountModel;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.create();

        PreferenceManager.setDefaultValues(this, R.xml.pref, false);
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public static LoginPresenterComponent getLoginPresenterComponent() {
        if (loginPresenterComponent == null) {
            loginPresenterComponent = DaggerLoginPresenterComponent.create();
        }

        return loginPresenterComponent;
    }

    public static void setAccount(AccountModel account) {
        accountModel = account;
    }

    public static AccountModel getAccount() {
        return accountModel;
    }
}
