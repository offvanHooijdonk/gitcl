package com.epam.traing.gitcl.db.tables;

/**
 * Created by off on 28.01.2017.
 */

public class AccountTable {
    public static final String TABLE = "Accounts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FNAME = "first_name";
    public static final String COLUMN_LNAME = "last_name";
    public static final String COLUMN_ACCOUNT_NAME = "account_name";

    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "(" +
                COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY," +
                COLUMN_FNAME + " TEXT NOT NULL," +
                COLUMN_LNAME + " TEXT NOT NULL," +
                COLUMN_ACCOUNT_NAME + " TEXT NOT NULL)";
    }
}
