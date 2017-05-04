package com.epam.traing.gitcl.di.application;

import android.content.Context;

import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.di.util.AccountComponent;
import com.epam.traing.gitcl.di.util.AccountInteractorModule;
import com.epam.traing.gitcl.di.util.AccountModule;
import com.epam.traing.gitcl.di.util.AuthModule;
import com.epam.traing.gitcl.di.util.AuthenticateComponent;
import com.epam.traing.gitcl.di.util.HistoryModule;
import com.epam.traing.gitcl.di.util.RepoComponent;
import com.epam.traing.gitcl.di.util.RepoInteractorModule;
import com.epam.traing.gitcl.di.util.RepoModule;
import com.epam.traing.gitcl.di.util.SearchComponent;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.helper.SessionHelper;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

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

    Context getContext();
    PrefHelper getPreferenceHelper();
    SessionHelper getSession();
    @Named(NetworkModule.RETROFIT_OAUTH)
    Retrofit getAuthApi();
    @Named(NetworkModule.RETROFIT_API)
    Retrofit getGitApi();
    ModelConverter getModelConverter();

    AuthenticateComponent plusAuthenticateComponent(AuthModule authModule, AccountModule accountModule);
    AccountComponent plusAccountComponent(AccountModule accountModule, AccountInteractorModule accountInteractorModule);
    RepoComponent plusRepoComponent(RepoModule repoModule, RepoInteractorModule repoInteractorModule,
                                    AccountModule accountModule, AccountInteractorModule accountInteractorModule);
    SearchComponent plusSearchComponent(HistoryModule historyModule, RepoModule repoModule, AccountModule accountModule);

}
