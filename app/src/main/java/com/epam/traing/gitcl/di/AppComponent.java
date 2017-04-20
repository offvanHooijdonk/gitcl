package com.epam.traing.gitcl.di;

import android.content.Context;

import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.db.dao.IAccountDao;
import com.epam.traing.gitcl.di.main.AccountModule;
import com.epam.traing.gitcl.di.main.MainFrameComponent;
import com.epam.traing.gitcl.di.main.MainFrameModule;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.helper.SessionHelper;
import com.epam.traing.gitcl.network.GitHubUserClient;

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
        NetworkModule.class,
        AccountModule.class,
        SearchModule.class
})
@Singleton
public interface AppComponent {

    Context getContext();
    PrefHelper getPreferenceHelper();
    SessionHelper getSession();

    @Named(NetworkModule.RETROFIT_OAUTH)
    Retrofit getAuthApi();
    GitHubUserClient getGitHubUserClient();
    IAccountDao getAccountDao();
    ModelConverter getModelConverter();

    /*LoginComponent plusLoginComponent(LoginModule loginModule,
                                      AuthenticatorModule authenticatorModule,
                                      AuthApiModule authApiModule);*/

    MainFrameComponent plusMainFrameComponent(MainFrameModule mainFrameModule);
}
