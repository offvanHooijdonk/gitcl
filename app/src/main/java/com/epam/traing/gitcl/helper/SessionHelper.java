package com.epam.traing.gitcl.helper;

import android.content.Context;

import com.epam.traing.gitcl.model.AccountModel;

/**
 * Created by Yahor_Fralou on 2/16/2017 1:00 PM.
 */

public class SessionHelper {
    private AccountModel account;
    private Context ctx;

    public SessionHelper(Context ctx) {
        this.ctx = ctx;
    }

    public AccountModel getCurrentAccount() {
        return account;
    }

    public void setCurrentAccount(AccountModel account) {
        this.account = account;
    }

    public boolean isLoggedIn() {
        return account != null;
    }
}
