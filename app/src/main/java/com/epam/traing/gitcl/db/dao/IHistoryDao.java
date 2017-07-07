package com.epam.traing.gitcl.db.dao;

import com.epam.traing.gitcl.model.HistoryModel;

import java.util.List;

import rx.Single;

/**
 * Created by Yahor_Fralou on 3/20/2017 6:23 PM.
 */

public interface IHistoryDao {
    int LIMIT_NONE = -1;

    Single<?> save(HistoryModel model);

    Single<List<HistoryModel>> findWithText(String text, int limit);
}
