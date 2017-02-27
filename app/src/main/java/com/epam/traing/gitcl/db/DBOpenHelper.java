package com.epam.traing.gitcl.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.epam.traing.gitcl.db.tables.AccountTable;
import com.epam.traing.gitcl.db.tables.RepoTable;

/**
 * Created by off on 28.01.2017.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 8;
    private static final String DB_NAME = "github_db";

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AccountTable.getCreateTableQuery());
        db.execSQL(RepoTable.getCreateTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
