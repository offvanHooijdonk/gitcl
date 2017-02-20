package com.epam.traing.gitcl.data.interactor.account;

import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.db.dao.IAccountDao;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.helper.SessionHelper;
import com.epam.traing.gitcl.network.GitHubUserClient;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by off on 19.02.2017.
 */

public class AccountInteractor implements IAccountInteractor {

    private GitHubUserClient userClient;
    private SessionHelper sessionHelper;
    private ModelConverter modelConverter;
    private IAccountDao accountDao;

    @Inject
    public AccountInteractor(GitHubUserClient userClient,
                             IAccountDao accountDao,
                             SessionHelper sessionHelper,
                             ModelConverter modelConverter) {
        this.userClient = userClient;
        this.accountDao = accountDao;
        this.sessionHelper = sessionHelper;
        this.modelConverter = modelConverter;
    }

    @Deprecated
    @Override
    public Observable<AccountModel> reloadAccount(String accountName) {
        return null;
    }

    @Override
    public Observable<AccountModel> subscribeCurrentAccountChange() {
        return accountDao.subscribeAccountChange()
                .flatMap(changes -> accountDao.findAccountByName(sessionHelper.getCurrentAccount().getAccountName()));
        // do not store changes here as they are already stored
    }

    @Override
    public Observable<AccountModel> reloadCurrentAccount() {
        return userClient.getCurrentUserInfo()
                .map(modelConverter::toAccountModel)
                .doOnNext(this::storeCurrentAccount);
    }

    private void storeCurrentAccount(AccountModel accountModel) {
        accountDao.saveAccount(accountModel);
        sessionHelper.setCurrentAccount(accountModel);
    }
}
