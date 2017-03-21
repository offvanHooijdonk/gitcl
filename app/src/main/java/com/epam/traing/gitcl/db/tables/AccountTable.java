package com.epam.traing.gitcl.db.tables;

/**
 * Created by off on 28.01.2017.
 */

public class AccountTable {
    public static final String TABLE = "Accounts";
    public static final String COLUMN_PERSON_NAME = "person_name";
    public static final String COLUMN_ACCOUNT_NAME = "account_name";
    public static final String COLUMN_ACCESS_TOKEN = "access_token";
    public static final String COLUMN_AVATAR_FILE_NAME = "avatar_file_name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_LOCATION = "location";

    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "(" +
                COLUMN_ACCOUNT_NAME + " TEXT NOT NULL COLLATE NOCASE PRIMARY KEY," +
                COLUMN_PERSON_NAME + " TEXT," +
                COLUMN_ACCESS_TOKEN + " TEXT," +
                COLUMN_EMAIL + " TEXT," +
                COLUMN_AVATAR_FILE_NAME + " TEXT, " +
                COLUMN_LOCATION + " TEXT" +
                ")";
    }
}
