package com.epam.traing.gitcl.app;

import android.content.Context;

import com.epam.traing.gitcl.helper.PrefHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by off on 28.01.2017.
 */

@Module
public class AppModule {
    private final GitClApplication app;

    AppModule(GitClApplication app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return app;
    }

    @Provides
    @Singleton
    PrefHelper providePreferenceHelper() {
        return new PrefHelper(app);
    }
}
