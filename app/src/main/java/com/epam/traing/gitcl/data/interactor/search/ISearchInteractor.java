package com.epam.traing.gitcl.data.interactor.search;

import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.model.HistoryModel;
import com.epam.traing.gitcl.db.model.RepoModel;

import java.util.List;

import rx.Observable;
import rx.Single;

/**
 * Created by Yahor_Fralou on 3/20/2017 7:07 PM.
 */

public interface ISearchInteractor {
    void saveHistoryEntry(HistoryModel model);

    Single<List<HistoryModel>> findHistoryEntries(String queryText, int limit);

    Single<List<RepoModel>> findReposLocal(String queryText);

    Single<List<AccountModel>> findAccountsLocal(String queryText);

    Observable<List<AccountModel>> searchAccountsOnApi(String queryText, int page);

    Observable<List<RepoModel>> searchRepositoriesOnApi(String queryText, int page);
}
