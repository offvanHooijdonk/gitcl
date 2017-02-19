package com.epam.traing.gitcl.data.interactor.account;

import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.db.dao.AccountDao;
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
    private AccountDao accountDao;

    @Inject
    public AccountInteractor(GitHubUserClient userClient,
                             AccountDao accountDao,
                             SessionHelper sessionHelper,
                             ModelConverter modelConverter) {
        this.userClient = userClient;
        this.accountDao = accountDao;
        this.sessionHelper = sessionHelper;
        this.modelConverter = modelConverter;
    }

    @Deprecated
    @Override
    public Observable<AccountModel> updateAccount(String accountName) {
        return null;
    }

    public Observable<AccountModel> subscribeAccountChange() {
        // FIXME
        return null;
    }

    @Override
    public Observable<AccountModel> updateCurrentAccount() {
        return userClient.getCurrentUserInfo()
                .map(modelConverter::toAccountModel)
                .doOnNext(accountDao::saveAccount)
                .doOnNext(sessionHelper::setCurrentAccount);
    }

}
