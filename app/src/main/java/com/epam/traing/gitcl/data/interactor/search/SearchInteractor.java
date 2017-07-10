package com.epam.traing.gitcl.data.interactor.search;

import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.data.interactor.Interactors;
import com.epam.traing.gitcl.db.dao.IAccountDao;
import com.epam.traing.gitcl.db.dao.IHistoryDao;
import com.epam.traing.gitcl.db.dao.IRepoDao;
import com.epam.traing.gitcl.model.AccountModel;
import com.epam.traing.gitcl.model.HistoryModel;
import com.epam.traing.gitcl.model.RepoModel;
import com.epam.traing.gitcl.network.GitHubRepoClient;
import com.epam.traing.gitcl.network.GitHubUserClient;

import java.util.Date;
import java.util.List;

import rx.Observable;

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
    public Observable<?> saveSearchText(String searchText) {
        HistoryModel historyModel = new HistoryModel();
        historyModel.setText(searchText);
        historyModel.setSearchDate(new Date().getTime());

        return historyDao.save(historyModel)
                .toObservable()
                .compose(Interactors.applySchedulersIO());
    }

    @Override
    public Observable<List<HistoryModel>> findHistoryEntries(String queryText, int limit) {
        return historyDao.findWithText(queryText, limit)
                .toObservable()
                .compose(Interactors.applySchedulersIO());
    }

    @Override
    public Observable<List<RepoModel>> findReposLocal(String queryText) {
        if (queryText == null || queryText.isEmpty()) {
            return Observable.empty();
        } else {
            return repoDao.findRepos(queryText)
                    .toObservable()
                    .compose(Interactors.applySchedulersIO());
        }
    }

    @Override
    public Observable<List<AccountModel>> findAccountsLocal(String queryText) {
        if (queryText == null || queryText.isEmpty()) {
            return Observable.empty();
        } else {
            return accountDao.findAccounts(queryText)
                    .toObservable()
                    .compose(Interactors.applySchedulersIO());
        }
    }

    @Override
    public Observable<List<AccountModel>> searchAccountsOnApi(String queryText, int page) {
        return userClient.searchUsers(queryText, page)
                .map(searchResults -> modelConverter.toAccountModelList(searchResults.getItems()))
                .compose(Interactors.applySchedulersIO());
    }

    @Override
    public Observable<List<RepoModel>> searchRepositoriesOnApi(String queryText, int page) {
        return repoClient.searchRepositories(queryText, page)
                .map(searchResults -> modelConverter.toRepoModelList(searchResults.getItems()))
                .compose(Interactors.applySchedulersIO());
    }


}
