package com.epam.traing.gitcl.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by off on 22.01.2017.
 */

public class PrefHelper {
    private static final String FLAG_FIRST_START = "flag_first_start";
    private static final String PREF_LOGGED_IN_ACCOUNT_ID = "pref_logged_in_account_id";

    private Context ctx;

    public PrefHelper(Context context) {
        this.ctx = context;
    }

    private SharedPreferences getSharedPreference() {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public boolean showLoginScreen() {
        return isFirstStart();
    }

    public boolean isFirstStart() {
        return getSharedPreference().getBoolean(FLAG_FIRST_START, true);
    }

    public String getLoggedAccountName() {
        return getSharedPreference().getString(PREF_LOGGED_IN_ACCOUNT_ID, null);
    }

    public void setLoggedAccountName(String value) {
        getSharedPreference().edit().putString(PREF_LOGGED_IN_ACCOUNT_ID, String.valueOf(value)).apply();
    }
}
