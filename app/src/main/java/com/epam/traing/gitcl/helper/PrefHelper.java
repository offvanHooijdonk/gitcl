package com.epam.traing.gitcl.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.epam.traing.gitcl.R;

/**
 * Created by off on 22.01.2017.
 */

public class PrefHelper {
    private static final String FLAG_FIRST_START = "flag_first_start";
    private static final String PREF_LOGGED_IN_ACCOUNT_ID = "pref_logged_in_account_id";

    public static SharedPreferences getSharedPreference(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static boolean showLoginScreen(Context ctx) {
        return isFirstStart(ctx);
    }

    public static boolean isFirstStart(Context ctx) {
        return getSharedPreference(ctx).getBoolean(FLAG_FIRST_START, true);
    }

    public static Long getLoggedAccountId(Context ctx) {
        String s = getSharedPreference(ctx).getString(PREF_LOGGED_IN_ACCOUNT_ID, null);
        return s != null ? Long.valueOf(s) : null;
    }

    public static void setLoggedAccountId(Context ctx, Long value) {
        getSharedPreference(ctx).edit().putString(PREF_LOGGED_IN_ACCOUNT_ID, String.valueOf(value)).apply();
    }
}
