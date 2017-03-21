package com.epam.traing.gitcl.data.interactor.search;

import com.epam.traing.gitcl.db.dao.IHistoryDao;
import com.epam.traing.gitcl.db.model.HistoryModel;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Yahor_Fralou on 3/20/2017 7:07 PM.
 */

public class SearchInteractor implements ISearchIntercator {

    private IHistoryDao historyDao;

    public SearchInteractor(IHistoryDao historyDao) {
        this.historyDao = historyDao;
    }

    @Override
    public void saveHistoryEntry(HistoryModel model) {
        historyDao.save(model);
    }

    @Override
    public Observable<List<HistoryModel>> findHistoryEntries(String queryText, int limit) {
        return historyDao.
                findWithText(queryText, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
