package com.epam.traing.gitcl.db.model;

import com.epam.traing.gitcl.db.tables.AccountTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by Yahor_Fralou on 1/25/2017 5:47 PM.
 */

@StorIOSQLiteType(table = AccountTable.TABLE)
public class AccountModel {
    @StorIOSQLiteColumn(name = AccountTable.COLUMN_ID, key = true)
    long id;
    @StorIOSQLiteColumn(name = AccountTable.COLUMN_PERSON_NAME)
    String personName;
    @StorIOSQLiteColumn(name = AccountTable.COLUMN_ACCOUNT_NAME)
    String accountName;
    @StorIOSQLiteColumn(name = AccountTable.COLUMN_ACCESS_TOKEN)
    String accessToken;
    @StorIOSQLiteColumn(name = AccountTable.COLUMN_AVATAR_FILE_NAME)
    String avatar;
    @StorIOSQLiteColumn(name = AccountTable.COLUMN_EMAIL)
    String email;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
