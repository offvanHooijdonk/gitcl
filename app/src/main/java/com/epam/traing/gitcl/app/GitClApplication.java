package com.epam.traing.gitcl.app;

import android.app.Application;
import android.preference.PreferenceManager;

import com.epam.traing.gitcl.R;

/**
 * Created by off on 22.01.2017.
 */

public class GitClApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        PreferenceManager.setDefaultValues(this, R.xml.pref, false);
    }
}
