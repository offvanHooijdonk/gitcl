package com.epam.traing.gitcl.db.dao;

import com.epam.traing.gitcl.model.HistoryModel;
import com.epam.traing.gitcl.db.tables.HistoryTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

import rx.Single;

/**
 * Created by Yahor_Fralou on 3/20/2017 6:23 PM.
 */

public class HistoryDao implements IHistoryDao {
    private StorIOSQLite storIOSQLite;

    public HistoryDao(StorIOSQLite storIOSQLite) {
        this.storIOSQLite = storIOSQLite;
    }

    @Override
    public Single<?> save(HistoryModel model) {
        return storIOSQLite.put().object(model).prepare().asRxSingle();
    }

    @Override
    public Single<List<HistoryModel>> findWithText(String text, int limit) {
        return storIOSQLite.get().listOfObjects(HistoryModel.class)
                .withQuery(Query.builder()
                        .table(HistoryTable.TABLE)
                        .where(HistoryTable.COLUMN_TEXT + " like ?")
                        .whereArgs("%" + text + "%")
                        .orderBy(HistoryTable.COLUMN_SEARCH_DATE + " desc")
                        .limit(limit)
                        .build()
                ).prepare().asRxSingle();
    }
}
