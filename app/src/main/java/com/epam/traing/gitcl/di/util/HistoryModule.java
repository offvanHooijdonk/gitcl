package com.epam.traing.gitcl.di.util;

import com.epam.traing.gitcl.db.dao.HistoryDao;
import com.epam.traing.gitcl.db.dao.IHistoryDao;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 5/3/2017 6:03 PM.
 */

@Module
public class HistoryModule {

    @Provides
    @UtilsScope
    public IHistoryDao provideHistoryDao(StorIOSQLite storIOSQLite) {
        return new HistoryDao(storIOSQLite);
    }
}
