package com.epam.traing.gitcl.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by off on 22.01.2017.
 */

public class PrefHelper {
    private static final String FLAG_SHOW_LOGIN = "flag_first_start";
    private static final String PREF_LOGGED_IN_ACCOUNT_ID = "pref_logged_in_account_id";
    private static final String PREF_TOKEN_TYPE = "pref_token_type";

    private Context ctx;

    public PrefHelper(Context context) {
        this.ctx = context;
    }

    private SharedPreferences getSharedPreference() {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public boolean isShowLogin() {
        return getSharedPreference().getBoolean(FLAG_SHOW_LOGIN, true);
    }

    public void setShowLogin(Boolean show) {
        getSharedPreference().edit().putBoolean(FLAG_SHOW_LOGIN, show).apply();
    }

    public String getLoggedAccountName() {
        return getSharedPreference().getString(PREF_LOGGED_IN_ACCOUNT_ID, null);
    }

    public void setLoggedAccountName(String value) {
        getSharedPreference().edit().putString(PREF_LOGGED_IN_ACCOUNT_ID, String.valueOf(value)).apply();
    }

    public String getTokenType() {
        return getSharedPreference().getString(PREF_TOKEN_TYPE, null);
    }

    public void setTokenType(String value) {
        getSharedPreference().edit().putString(PREF_TOKEN_TYPE, String.valueOf(value)).apply();
    }
}
