package com.epam.traing.gitcl.interactor;

import android.os.Handler;

import com.epam.traing.gitcl.model.AccountModel;

/**
 * Created by Yahor_Fralou on 1/27/2017 5:59 PM.
 */

public class GitAuthenticator implements IAuthenticator {
    private AuthenticationListener listener;

    @Override
    public void setListener(AuthenticationListener listener) {
        this.listener = listener;
    }

    @Override
    public void authenticate() {
        final AccountModel accountModel = new AccountModel();
        accountModel.setId(42L);
        accountModel.setFirstName("John");
        accountModel.setLastName("Doe");
        accountModel.setAccountName("goJohnyGo");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onSuccess(accountModel);
                }
            }
        }, 2000);
    }
}
