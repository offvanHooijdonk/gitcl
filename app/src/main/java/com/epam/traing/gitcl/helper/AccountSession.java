package com.epam.traing.gitcl.helper;

import com.epam.traing.gitcl.db.model.AccountModel;

/**
 * Created by Yahor_Fralou on 2/16/2017 1:00 PM.
 */

public class AccountSession {
    private AccountModel account;

    public AccountModel getAccount() {
        return account;
    }

    public void setAccount(AccountModel account) {
        this.account = account;
    }
}
