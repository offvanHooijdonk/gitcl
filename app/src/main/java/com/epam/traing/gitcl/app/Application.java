package com.epam.traing.gitcl.app;

import android.preference.PreferenceManager;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.di.AppComponent;
import com.epam.traing.gitcl.di.AppModule;
import com.epam.traing.gitcl.di.DBModule;
import com.epam.traing.gitcl.di.DaggerAppComponent;
import com.epam.traing.gitcl.di.NetworkModule;
import com.epam.traing.gitcl.di.login.AuthApiModule;
import com.epam.traing.gitcl.di.login.AuthenticatorModule;
import com.epam.traing.gitcl.di.login.LoginComponent;
import com.epam.traing.gitcl.di.login.LoginModule;
import com.epam.traing.gitcl.di.main.AccountModule;
import com.epam.traing.gitcl.di.main.MainFrameComponent;
import com.epam.traing.gitcl.di.main.MainFrameModule;

/**
 * Created by off on 22.01.2017.
 */

public class Application extends android.app.Application {
    public static final String LOG = "githubcl";
    private static AppComponent appComponent;
    private static LoginComponent loginComponent;
    private static MainFrameComponent mainFrameComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .dBModule(new DBModule())
                .accountModule(new AccountModule())
                .build();

        PreferenceManager.setDefaultValues(this, R.xml.pref, false);
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public static LoginComponent getLoginComponent() {
        if (loginComponent == null) {
            loginComponent = getAppComponent()
                    .plusLoginComponent(new LoginModule(),
                            new AuthenticatorModule(),
                            new AuthApiModule());
        }

        return loginComponent;
    }

    public static MainFrameComponent getMainFrameComponent() {
        if (mainFrameComponent == null) {
            mainFrameComponent = getAppComponent()
                    .plusMainFrameComponent(new MainFrameModule());
        }

        return mainFrameComponent;
    }
}
