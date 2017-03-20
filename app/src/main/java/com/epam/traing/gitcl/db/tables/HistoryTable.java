package com.epam.traing.gitcl.db.tables;

/**
 * Created by Yahor_Fralou on 3/20/2017 6:17 PM.
 */

public class HistoryTable {
    public static final String TABLE = "History";
    public static final String COLUMN_TEXT = "search_text";
    public static final String COLUMN_SEARCH_DATE = "search_date";

    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "(" +
                COLUMN_TEXT + " TEXT NOT NULL COLLATE NOCASE PRIMARY KEY," +
                COLUMN_SEARCH_DATE + " NUMBER NOT NULL" +
                ")";
    }
}
