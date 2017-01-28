package com.epam.traing.gitcl.presenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 1/27/2017 12:08 PM.
 */

@Module
public class PresenterModule {

    @Provides
    @Singleton
    ILoginPresenter provideLoginPresenter() {
        return new LoginPresenter();
    }
}
