package com.epam.traing.gitcl.ui;

/**
 * Created by Yahor_Fralou on 1/27/2017 3:07 PM.
 */

public interface ILoginView {

    void showLoginProgress(boolean show);

    void showAuthErrorMessage(String message);

    void startMainViewAsLogged();

    void startMainViewAsAnon();
}
