package com.epam.traing.gitcl.app;

/**
 * Created by Yahor_Fralou on 2/7/2017 4:03 PM.
 */

public interface Constants {
    public static interface Api {
        public String OAUTH_KEY = "6203c4ce6b8758a78dce";
        public String OAUTH_SECRET = "c71efb17a495bce469e238624de486b7bce1edf5";
        public String OAUTH_SCOPES = "user user:email public_repo";
        public String OAUTH_URL = "https://github.com/login/oauth/authorize?scope=%s&client_id=%s";
        public String OAUTH_CALLBACK_URL = "githubcl://githubcloauth/callback";
    }

}
