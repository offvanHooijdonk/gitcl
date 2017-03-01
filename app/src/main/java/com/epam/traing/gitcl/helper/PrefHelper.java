package com.epam.traing.gitcl.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.app.Application;

/**
 * Created by off on 22.01.2017.
 */

public class PrefHelper {
    public static final int VALUE_NEVER = -1;

    private static final String FLAG_SHOW_LOGIN = "flag_first_start";
    private static final String PREF_LOGGED_IN_ACCOUNT_ID = "pref_logged_in_account_id";
    private static final String TOKEN_TYPE = "pref_token_type";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String ACCOUNT_LAST_UPDATE_TIME = "account_last_update_time";
    private static final String REPOS_LAST_UPDATE_TIME = "repos_last_update_time";
    private static final int REPO_UPDATE_INTERVAL_DEFAULT = 15;

    private Context ctx;

    public PrefHelper(Context context) {
        this.ctx = context;
    }

    private SharedPreferences getSharedPreference() {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public boolean isShowLoginScreen() {
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
        getSharedPreference().edit().putString(TOKEN_TYPE, value).apply();
    }
    public void setAccessToken(String value) {
        getSharedPreference().edit().putString(ACCESS_TOKEN, value).apply();
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

    public long getReposLastUpdateTime() {
        return getSharedPreference().getLong(REPOS_LAST_UPDATE_TIME, 0);
    }

    public void setReposLastUpdateTime(long time) {
        getSharedPreference().edit().putLong(REPOS_LAST_UPDATE_TIME, time).apply();
    }

    public int getRepoUpdateIntervalMins() {
        return parseIntValue(
                getSharedPreference().getString(ctx.getString(R.string.pref_repo_update_time_key), ctx.getString(R.string.pref_repo_update_time_default)),
                REPO_UPDATE_INTERVAL_DEFAULT);
    }

    private int parseIntValue(String value, int defaultValue) {
        int result;
        try {
            result = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            Log.e(Application.LOG, "Error getting int value of " + value, e);
            result = defaultValue;
        }

        return result;
    }
}
