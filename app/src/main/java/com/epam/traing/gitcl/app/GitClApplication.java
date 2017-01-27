package com.epam.traing.gitcl.app;

import android.app.Application;
import android.preference.PreferenceManager;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.component.AppComponent;
import com.epam.traing.gitcl.component.DaggerAppComponent;

/**
 * Created by off on 22.01.2017.
 */

public class GitClApplication extends Application {
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerAppComponent.create();

        PreferenceManager.setDefaultValues(this, R.xml.pref, false);
    }

    public static AppComponent getComponent() {
        return component;
    }
}
