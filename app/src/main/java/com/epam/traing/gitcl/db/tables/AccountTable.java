package com.epam.traing.gitcl.db.tables;

/**
 * Created by off on 28.01.2017.
 */

public class AccountTable {
    public static final String TABLE = "Accounts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PERSON_NAME = "person_name";
    public static final String COLUMN_ACCOUNT_NAME = "account_name";
    public static final String COLUMN_ACCESS_TOKEN = "access_token";
    public static final String COLUMN_AVATAR_FILE_NAME = "avatar_file_name";
    public static final String COLUMN_EMAIL = "email";

    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "(" +
                COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY," +
                COLUMN_PERSON_NAME + " TEXT NOT NULL," +
                COLUMN_ACCESS_TOKEN + " TEXT NOT NULL," +
                COLUMN_EMAIL + " TEXT NOT NULL," +
                COLUMN_AVATAR_FILE_NAME + " TEXT," +
                COLUMN_ACCOUNT_NAME + " TEXT NOT NULL)";
    }
}
