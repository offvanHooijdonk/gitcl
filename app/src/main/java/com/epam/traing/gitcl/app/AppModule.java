package com.epam.traing.gitcl.app;

import android.content.Context;

import com.epam.traing.gitcl.helper.SessionHelper;
import com.epam.traing.gitcl.helper.PrefHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by off on 28.01.2017.
 */

@Module
public class AppModule {
    private final Application app;

    AppModule(Application app) {
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

    @Provides
    @Singleton
    SessionHelper provideSession(Context ctx) {
        return new SessionHelper(ctx);
    }
}
