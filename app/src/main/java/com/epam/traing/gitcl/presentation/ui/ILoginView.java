package com.epam.traing.gitcl.presentation.ui;

/**
 * Created by Yahor_Fralou on 1/27/2017 3:07 PM.
 */

public interface ILoginView {

    void showLoginProgress(boolean show);

    void showAuthErrorMessage(Throwable e);

    void startMainView();

    void showLoginScreen();

    void startWebViewForOAuth(String authUrl);
}
