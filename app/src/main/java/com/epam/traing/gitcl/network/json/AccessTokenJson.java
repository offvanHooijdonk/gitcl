package com.epam.traing.gitcl.network.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yahor_Fralou on 2/3/2017 11:42 AM.
 */

public class AccessTokenJson {
    @SerializedName("access_token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
