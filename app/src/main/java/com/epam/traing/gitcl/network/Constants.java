package com.epam.traing.gitcl.network;

/**
 * Created by Yahor_Fralou on 2/7/2017 4:03 PM.
 */

public interface Constants {
    interface Api {
        String OAUTH_LOGIN_BASE_URL = "https://github.com";
        String API_BASE_URL = "https://api.github.com";
        String OAUTH_KEY = "6203c4ce6b8758a78dce";
        String OAUTH_SECRET = "c71efb17a495bce469e238624de486b7bce1edf5";
        String OAUTH_SCOPES = "user user:email public_repo";
        String OAUTH_URL = OAUTH_LOGIN_BASE_URL + "/login/oauth/authorize?scope=%s&client_id=%s";
        String OAUTH_CALLBACK_URL = "githubcl://githubcloauth/callback";
        String OAUTH_AUTH_HEADER = "Authorization";
    }

    interface Refresh {
        int REFRESH_ACCOUNT_MILLS = 2 * 60 * 60 * 1000;
    }

}
