package com.epam.traing.gitcl.network;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 2/3/2017 11:13 AM.
 */

@Module
public class NetworkModule {

    @Provides
    public ApiClientFactory getApiClientFactory() {
        return new ApiClientFactory();
    }

    @Provides
    public GitHubTokenClient getTokenClient(ApiClientFactory factory) {
        return factory.getTokenClient();
    }

    @Provides
    public GitHubUserClient getUserClient(ApiClientFactory factory) {
        return factory.getUserClient();
    }

}
