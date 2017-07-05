package com.epam.traing.gitcl.data.interactor.search;

import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.db.dao.IAccountDao;
import com.epam.traing.gitcl.db.dao.IHistoryDao;
import com.epam.traing.gitcl.db.dao.IRepoDao;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.model.HistoryModel;
import com.epam.traing.gitcl.db.model.RepoModel;
import com.epam.traing.gitcl.network.GitHubRepoClient;
import com.epam.traing.gitcl.network.GitHubUserClient;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Yahor_Fralou on 3/20/2017 7:07 PM.
 */

public class SearchInteractor implements ISearchInteractor {

    private IHistoryDao historyDao;
    private IRepoDao repoDao;
    private IAccountDao accountDao;
    private GitHubRepoClient repoClient;
    private GitHubUserClient userClient;
    private ModelConverter modelConverter;

    public SearchInteractor(IHistoryDao historyDao, IRepoDao repoDao, IAccountDao accountDao,
                            GitHubRepoClient repoClient, GitHubUserClient userClient, ModelConverter modelConverter) {
        this.historyDao = historyDao;
        this.repoDao = repoDao;
        this.accountDao = accountDao;
        this.repoClient = repoClient;
        this.userClient = userClient;
        this.modelConverter = modelConverter;
    }

    @Override
    public void saveHistoryEntry(HistoryModel model) {
        historyDao.save(model);
    }

    @Override
    public Single<List<HistoryModel>> findHistoryEntries(String queryText, int limit) {
        return historyDao.
                findWithText(queryText, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<List<RepoModel>> findReposLocal(String queryText) {
        if (queryText == null || queryText.isEmpty()) {
            return Single.just(Collections.emptyList());
        } else {
            return repoDao.findRepos(queryText)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    @Override
    public Single<List<AccountModel>> findAccountsLocal(String queryText) {
        if (queryText == null || queryText.isEmpty()) {
            return Single.just(Collections.emptyList());
        } else {
            return accountDao.findAccounts(queryText)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    @Override
    public Observable<List<AccountModel>> searchAccountsOnApi(String queryText, int page) {
        return userClient.searchUsers(queryText, page)
                .map(searchResults -> modelConverter.toAccountModelList(searchResults.getItems()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<RepoModel>> searchRepositoriesOnApi(String queryText, int page) {
        return repoClient.searchRepositories(queryText, page)
                .map(searchResults ->  modelConverter.toRepoModelList(searchResults.getItems()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
