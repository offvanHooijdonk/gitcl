package com.epam.traing.gitcl.data.interactor.authenticate;

/**
 * Created by Yahor_Fralou on 1/31/2017 12:03 PM.
 */

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
