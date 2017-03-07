package com.epam.traing.gitcl.db.tables;

/**
 * Created by Yahor_Fralou on 2/22/2017 3:56 PM.
 */

public class RepoTable {
    public static final String TABLE = "Repositories";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_OWNER_NAME = "owner_name";
    public static final String COLUMN_IS_PRIVATE = "is_private";
    public static final String COLUMN_IS_FORK = "is_fork";
    public static final String COLUMN_STARGAZERS_COUNT = "stargazers_count";
    public static final String COLUMN_WATCHERS_COUNT = "watchers_count";
    public static final String COLUMN_FORKS_COUNT = "forks_count";
    public static final String COLUMN_LANGUAGE = "language";
    public static final String COLUMN_CREATE_DATE = "create_date";
    public static final String COLUMN_UPDATE_DATE = "update_date";
    public static final String COLUMN_PUSH_DATE = "push_date";
    public static final String COLUMN_VERBOSE_UPDATE_DATE = "verbose_update_date";
    public static final String COLUMN_CONTRIBUTORS_COUNT = "contributors_count";
    public static final String COLUMN_DEFAULT_BRANCH = "default_branch";

    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "(" +
                COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY," +
                COLUMN_NAME + " TEXT NOT NULL," +
                COLUMN_OWNER_NAME + " TEXT NOT NULL," +
                COLUMN_IS_PRIVATE + " INTEGER NOT NULL," +
                COLUMN_IS_FORK + " INTEGER NOT NULL," +
                COLUMN_STARGAZERS_COUNT + " INTEGER NOT NULL," +
                COLUMN_WATCHERS_COUNT + " INTEGER NOT NULL," +
                COLUMN_FORKS_COUNT + " INTEGER NOT NULL," +
                COLUMN_LANGUAGE + " TEXT," +
                COLUMN_CREATE_DATE + " NUMBER," +
                COLUMN_UPDATE_DATE + " NUMBER," +
                COLUMN_PUSH_DATE + " NUMBER," +
                COLUMN_VERBOSE_UPDATE_DATE + " NUMBER, " +
                COLUMN_CONTRIBUTORS_COUNT + " NUMBER DEFAULT 0," +
                COLUMN_DEFAULT_BRANCH + " TEXT" +
                ")";
    }

}
