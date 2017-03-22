package com.epam.traing.gitcl.data.interactor.search;

import com.epam.traing.gitcl.db.dao.IAccountDao;
import com.epam.traing.gitcl.db.dao.IHistoryDao;
import com.epam.traing.gitcl.db.dao.IRepoDao;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.model.HistoryModel;
import com.epam.traing.gitcl.db.model.RepoModel;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Yahor_Fralou on 3/20/2017 7:07 PM.
 */

public class SearchInteractor implements ISearchIntercator {

    private IHistoryDao historyDao;
    private IRepoDao repoDao;
    private IAccountDao accountDao;

    public SearchInteractor(IHistoryDao historyDao, IRepoDao repoDao, IAccountDao accountDao) {
        this.historyDao = historyDao;
        this.repoDao = repoDao;
        this.accountDao = accountDao;
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

    @Override
    public Observable<List<RepoModel>> findReposLocal(String queryText) {
        if (queryText == null || queryText.isEmpty()) {
            return Observable.empty();
        } else {
            return repoDao.
                    findRepos(queryText)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    @Override
    public Observable<List<AccountModel>> findAccountsLocal(String queryText) {
        if (queryText == null || queryText.isEmpty()) {
            return Observable.empty();
        } else {
            return accountDao.findAccounts(queryText)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

}
