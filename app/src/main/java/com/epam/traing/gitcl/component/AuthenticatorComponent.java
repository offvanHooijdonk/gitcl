package com.epam.traing.gitcl.component;

import com.epam.traing.gitcl.db.DBModule;
import com.epam.traing.gitcl.interactor.authenticate.GitAuthenticator;
import com.epam.traing.gitcl.interactor.authenticate.LoginScope;

import dagger.Component;

/**
 * Created by off on 28.01.2017.
 */

@Component(modules = {DBModule.class}, dependencies = {AppComponent.class})
@LoginScope
public interface AuthenticatorComponent {
    void inject(GitAuthenticator gitAuthenticator);

    /*StorIOSQLite storIOSQLite();*/
}
