package com.epam.traing.gitcl.network.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yahor_Fralou on 2/3/2017 1:59 PM.
 */

public class AccountJson {
    @SerializedName("login")
    private String login;
    @SerializedName("avatar_url")
    private String avatarUrl;
    @SerializedName("name")
    private String personName;
    @SerializedName("company")
    private String company;
    @SerializedName("email")
    private String email;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
