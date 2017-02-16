package com.epam.traing.gitcl.presentation.presenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 2/16/2017 6:26 PM.
 */

@Module
public class MainFrameModule {
    @Provides
    @Singleton
    IMainPresenter provideMainPresenter() {
        return new MainPresenter();
    }
}
