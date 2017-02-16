package com.epam.traing.gitcl.db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.model.AccountModelSQLiteTypeMapping;
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
                .build();
    }

    @Provides
    public SQLiteOpenHelper provideSQLiteOpenHelper(Context ctx) {
        return new DBOpenHelper(ctx);
    }
}
