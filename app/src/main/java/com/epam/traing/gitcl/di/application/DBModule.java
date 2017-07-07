package com.epam.traing.gitcl.di.application;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.epam.traing.gitcl.db.DBOpenHelper;
import com.epam.traing.gitcl.model.AccountModel;
import com.epam.traing.gitcl.model.AccountModelSQLiteTypeMapping;
import com.epam.traing.gitcl.model.HistoryModel;
import com.epam.traing.gitcl.model.HistoryModelSQLiteTypeMapping;
import com.epam.traing.gitcl.model.RepoModel;
import com.epam.traing.gitcl.model.RepoModelSQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by off on 28.01.2017.
 */

@Module
public class DBModule {

    @Provides
    @Singleton
    public StorIOSQLite provideSQLite(SQLiteOpenHelper sqLiteOpenHelper) {
        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(AccountModel.class, new AccountModelSQLiteTypeMapping())
                .addTypeMapping(RepoModel.class, new RepoModelSQLiteTypeMapping())
                .addTypeMapping(HistoryModel.class, new HistoryModelSQLiteTypeMapping())
                .build();
    }

    @Provides
    public SQLiteOpenHelper provideSQLiteOpenHelper(Context ctx) {
        return new DBOpenHelper(ctx);
    }
}
