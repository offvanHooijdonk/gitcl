package com.epam.traing.gitcl.data.interactor.search;

import com.epam.traing.gitcl.model.AccountModel;
import com.epam.traing.gitcl.model.HistoryModel;
import com.epam.traing.gitcl.model.RepoModel;

import java.util.List;

import rx.Observable;

/**
 * Created by Yahor_Fralou on 3/20/2017 7:07 PM.
 */

public interface ISearchInteractor {

    /*Observable<List<SearchResultItem>> searchLive(String text, int maxHistory);

    Observable<List<SearchResultItem>> searchRemote(String text);*/

    Observable<?> saveSearchText(String searchText);

    Observable<List<HistoryModel>> findHistoryEntries(String queryText, int limit);

    Observable<List<RepoModel>> findReposLocal(String queryText);

    Observable<List<AccountModel>> findAccountsLocal(String queryText);

    Observable<List<AccountModel>> searchAccountsOnApi(String queryText, int page);

    Observable<List<RepoModel>> searchRepositoriesOnApi(String queryText, int page);
}
