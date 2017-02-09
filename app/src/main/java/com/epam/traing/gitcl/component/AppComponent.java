package com.epam.traing.gitcl.component;

import android.content.Context;

import com.epam.traing.gitcl.app.AppModule;
import com.epam.traing.gitcl.helper.PrefHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Yahor_Fralou on 1/26/2017 11:40 AM.
 */

@Component(modules = {AppModule.class/*, PresenterModule.class*//*, NetworkModule.class, DBModule.class*/})
@Singleton
public interface AppComponent {
    Context getContext();
    PrefHelper getPreferenceHelper();
}
