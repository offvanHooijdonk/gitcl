package com.epam.traing.gitcl.db.dao;

import com.epam.traing.gitcl.db.model.HistoryModel;

import java.util.List;

import rx.Observable;

/**
 * Created by Yahor_Fralou on 3/20/2017 6:23 PM.
 */

public interface IHistoryDao {
    int LIMIT_NONE = -1;

    void save(HistoryModel model);

    Observable<List<HistoryModel>> findWithText(String text, int limit);
}
