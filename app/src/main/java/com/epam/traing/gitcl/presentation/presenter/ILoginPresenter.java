package com.epam.traing.gitcl.presentation.presenter;

import android.content.Intent;

import com.epam.traing.gitcl.presentation.ui.ILoginView;

/**
 * Created by Yahor_Fralou on 1/25/2017 5:09 PM.
 */

public interface ILoginPresenter {
    void setView(ILoginView loginView);

    void onSkipLoginSelected();

    void onLoginSelected();

    void onActivityResume(Intent intent);
}
