package com.epam.traing.gitcl.db.dao;

import com.epam.traing.gitcl.db.model.HistoryModel;
import com.epam.traing.gitcl.db.tables.HistoryTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

import rx.Observable;

/**
 * Created by Yahor_Fralou on 3/20/2017 6:23 PM.
 */

public class HistoryDao implements IHistoryDao {
    private StorIOSQLite storIOSQLite;

    public HistoryDao(StorIOSQLite storIOSQLite) {
        this.storIOSQLite = storIOSQLite;
    }

    @Override
    public void save(HistoryModel model) {
        storIOSQLite.put().object(model).prepare().executeAsBlocking();
    }

    @Override
    public Observable<List<HistoryModel>> findWithText(String text) {
        return storIOSQLite.get().listOfObjects(HistoryModel.class)
                .withQuery(Query.builder()
                        .table(HistoryTable.TABLE)
                        .where(HistoryTable.COLUMN_TEXT + " like ?")
                        .whereArgs("%" + text + "%")
                        .orderBy(HistoryTable.COLUMN_SEARCH_DATE + " desc")
                        .build()
                ).prepare().asRxObservable();
    }
}
