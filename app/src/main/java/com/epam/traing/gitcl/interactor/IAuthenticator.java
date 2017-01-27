package com.epam.traing.gitcl.interactor;

import com.epam.traing.gitcl.model.AccountModel;

/**
 * Created by Yahor_Fralou on 1/25/2017 5:43 PM.
 */

public interface IAuthenticator {
    void setListener(AuthenticationListener listener);
    void authenticate();

    interface AuthenticationListener {
        void onSuccess(AccountModel accountModel);
        void onFail(String message);
    }
}
