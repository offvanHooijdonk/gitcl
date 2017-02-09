package com.epam.traing.gitcl.component;

import com.epam.traing.gitcl.interactor.authenticate.GitAuthenticator;

/**
 * Created by off on 28.01.2017.
 */

//@Component(modules = {AuthenticatorModule.class, DBModule.class, NetworkModule.class}, dependencies = {AppComponent.class})
//@LoginScope
public interface AuthenticatorComponent {
    /*void inject(GitAuthenticator gitAuthenticator);*/

    GitAuthenticator getGitAuthenticator();
    /*GitHubTokenClient gitHubTokenClient();
    GitHubUserClient gitHubUserClient();
    StorIOSQLite storIOSQLite();*/
}
