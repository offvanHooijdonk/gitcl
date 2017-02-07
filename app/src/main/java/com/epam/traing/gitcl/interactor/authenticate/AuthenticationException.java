package com.epam.traing.gitcl.interactor.authenticate;

import java.io.IOException;

/**
 * Created by Yahor_Fralou on 2/7/2017 6:12 PM.
 */

public class AuthenticationException extends IOException {
    public AuthenticationException(String message) {
        super(message);
    }
}
