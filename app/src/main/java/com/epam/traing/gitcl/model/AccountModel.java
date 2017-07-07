package com.epam.traing.gitcl.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.epam.traing.gitcl.db.tables.AccountTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by Yahor_Fralou on 1/25/2017 5:47 PM.
 */

@StorIOSQLiteType(table = AccountTable.TABLE)
public class AccountModel implements Parcelable {
    @StorIOSQLiteColumn(name = AccountTable.COLUMN_ACCOUNT_NAME, key = true)
    String accountName;
    @StorIOSQLiteColumn(name = AccountTable.COLUMN_PERSON_NAME)
    String personName;
    @StorIOSQLiteColumn(name = AccountTable.COLUMN_ACCESS_TOKEN)
    String accessToken;
    @StorIOSQLiteColumn(name = AccountTable.COLUMN_AVATAR_FILE_NAME)
    String avatar;
    @StorIOSQLiteColumn(name = AccountTable.COLUMN_EMAIL)
    String email;
    @StorIOSQLiteColumn(name = AccountTable.COLUMN_LOCATION)
    String location;
    float searchScore;

    public AccountModel() {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public float getSearchScore() {
        return searchScore;
    }

    public void setSearchScore(float searchScore) {
        this.searchScore = searchScore;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accountName);
        dest.writeString(personName);
        dest.writeString(avatar);
    }

    public static final Parcelable.Creator<AccountModel> CREATOR = new Creator<AccountModel>() {
        @Override
        public AccountModel createFromParcel(Parcel source) {
            return new AccountModel(source);
        }

        @Override
        public AccountModel[] newArray(int size) {
            return new AccountModel[size];
        }
    };


    private AccountModel(Parcel in) {
        accountName = in.readString();
        personName = in.readString();
        avatar = in.readString();
    }
}
