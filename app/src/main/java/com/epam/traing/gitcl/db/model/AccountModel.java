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
    @StorIOSQLiteColumn(name = AccountTable.COLUMN_FNAME)
    String firstName;
    @StorIOSQLiteColumn(name = AccountTable.COLUMN_LNAME)
    String lastName;
    @StorIOSQLiteColumn(name = AccountTable.COLUMN_ACCOUNT_NAME)
    String accountName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
