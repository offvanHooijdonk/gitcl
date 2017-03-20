package com.epam.traing.gitcl.data.interactor.search;

import com.epam.traing.gitcl.db.model.HistoryModel;

import java.util.List;

import rx.Observable;

/**
 * Created by Yahor_Fralou on 3/20/2017 7:07 PM.
 */

public interface ISearchIntercator {
    void saveHistoryEntry(HistoryModel model);

    Observable<List<HistoryModel>> findHistoryEntries(String queryText);
}
