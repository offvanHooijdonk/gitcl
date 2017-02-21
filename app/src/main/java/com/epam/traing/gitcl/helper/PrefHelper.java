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
    private static final String TOKEN_TYPE = "pref_token_type";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String ACCOUNT_LAST_UPDATE_TIME = "account_last_update_time";


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
        getSharedPreference().edit().putString(PREF_LOGGED_IN_ACCOUNT_ID, value).apply();
    }

    public String getTokenType() {
        return getSharedPreference().getString(TOKEN_TYPE, null);
    }

    public void setTokenType(String value) {
        getSharedPreference().edit().putString(TOKEN_TYPE, String.valueOf(value)).apply();
    }
    public void setAccessToken(String value) {
        getSharedPreference().edit().putString(ACCESS_TOKEN, String.valueOf(value)).apply();
    }

    public String getAccessToken() {
        return getSharedPreference().getString(ACCESS_TOKEN, null);
    }

    public long getAccountLastUpdateTime() {
        return getSharedPreference().getLong(ACCOUNT_LAST_UPDATE_TIME, 0);
    }

    public void setAccountLastUpdateTime(long time) {
        getSharedPreference().edit().putLong(ACCOUNT_LAST_UPDATE_TIME, time).apply();
    }

}
