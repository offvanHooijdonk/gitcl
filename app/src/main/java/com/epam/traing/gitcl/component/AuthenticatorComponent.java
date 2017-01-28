package com.epam.traing.gitcl.component;

import com.epam.traing.gitcl.app.AppModule;
import com.epam.traing.gitcl.db.DBModule;
import com.epam.traing.gitcl.interactor.GitAuthenticator;
import com.epam.traing.gitcl.interactor.LoginScope;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Subcomponent;

/**
 * Created by off on 28.01.2017.
 */

@Component(modules = {DBModule.class}, dependencies = {AppComponent.class})
@LoginScope
public interface AuthenticatorComponent {
    void inject(GitAuthenticator gitAuthenticator);

    /*StorIOSQLite storIOSQLite();*/
}
