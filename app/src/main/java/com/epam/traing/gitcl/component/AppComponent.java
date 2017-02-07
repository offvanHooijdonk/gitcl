package com.epam.traing.gitcl.component;

import android.content.Context;

import com.epam.traing.gitcl.app.AppModule;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.presentation.presenter.PresenterModule;
import com.epam.traing.gitcl.presentation.ui.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Yahor_Fralou on 1/26/2017 11:40 AM.
 */

@Component(modules = {AppModule.class, PresenterModule.class})
@Singleton
public interface AppComponent {
    // TODO move activities injection to a single Presenter Component ?
    void inject(LoginActivity loginActivity);

    Context getContext();
    PrefHelper getPreferenceHelper();
}
