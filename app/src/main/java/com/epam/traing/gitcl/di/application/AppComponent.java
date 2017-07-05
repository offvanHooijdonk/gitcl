package com.epam.traing.gitcl.di.application;

import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.di.util.AccountComponent;
import com.epam.traing.gitcl.di.util.AuthenticateComponent;
import com.epam.traing.gitcl.di.util.RepoComponent;
import com.epam.traing.gitcl.di.util.SearchComponent;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Yahor_Fralou on 1/26/2017 11:40 AM.
 */

@Component(modules = {
        AppModule.class,
        DBModule.class,
        NetworkModule.class
})
@Singleton
public interface AppComponent {

    ModelConverter getModelConverter();

    AuthenticateComponent plusAuthenticateComponent();

    AccountComponent plusAccountComponent();

    RepoComponent plusRepoComponent();

    SearchComponent plusSearchComponent();

}
