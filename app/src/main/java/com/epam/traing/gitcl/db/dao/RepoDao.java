package com.epam.traing.gitcl.db.dao;

import com.epam.traing.gitcl.db.model.RepoModel;
import com.epam.traing.gitcl.db.tables.RepoTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Yahor_Fralou on 2/24/2017 3:35 PM.
 */

public class RepoDao implements IRepoDao {

    private StorIOSQLite sqLite;

    @Inject
    public RepoDao(StorIOSQLite sqLite) {
        this.sqLite = sqLite;
    }

    @Override
    public Observable<List<RepoModel>> getRepositories() {
        return sqLite.get()
                .listOfObjects(RepoModel.class)
                .withQuery(Query.builder()
                        .table(RepoTable.TABLE).orderBy(RepoTable.COLUMN_NAME).build())
                .prepare().asRxObservable();
    }

    @Override
    public Observable<List<RepoModel>> findRepos(String queryText) {
        return sqLite.get()
                .listOfObjects(RepoModel.class)
                .withQuery(Query.builder()
                        .table(RepoTable.TABLE)
                        .where(RepoTable.COLUMN_NAME + " like ?")
                        .whereArgs("%" + queryText + "%")
                        .orderBy(RepoTable.COLUMN_NAME).build())
                .prepare().asRxObservable();
    }

    @Override
    public Observable<RepoModel> getById(long id) {
        return sqLite.get().object(RepoModel.class)
                .withQuery(Query.builder()
                        .table(RepoTable.TABLE).where(RepoTable.COLUMN_ID + "=?").whereArgs(id).build())
                .prepare().asRxObservable();
    }

    @Override
    public void saveAll(List<RepoModel> models) {
        Observable.from(models).doOnNext(this::save).subscribe();
    }

    @Override
    public void save(RepoModel model) {
        sqLite.put().object(model).prepare().executeAsBlocking();
    }
}
