package com.epam.traing.gitcl.app;

import android.preference.PreferenceManager;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.di.DependencyManager;

/**
 * Created by off on 22.01.2017.
 */

public class Application extends android.app.Application {
    public static final String LOG = "githubcl";

    @Override
    public void onCreate() {
        super.onCreate();

        PreferenceManager.setDefaultValues(this, R.xml.pref, false);

        DependencyManager.initAppComponent(this);
    }

}
