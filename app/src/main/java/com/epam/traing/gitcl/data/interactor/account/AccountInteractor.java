package com.epam.traing.gitcl.data.interactor.account;

import android.support.annotation.NonNull;

import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.data.interactor.Interactors;
import com.epam.traing.gitcl.db.dao.IAccountDao;
import com.epam.traing.gitcl.model.AccountModel;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.helper.SessionHelper;
import com.epam.traing.gitcl.network.GitHubUserClient;

import rx.Observable;

/**
 * Created by off on 19.02.2017.
 */

public class AccountInteractor implements IAccountInteractor {

    private GitHubUserClient userClient;
    private SessionHelper sessionHelper;
    private ModelConverter modelConverter;
    private PrefHelper prefHelper;
    private IAccountDao accountDao;

    public AccountInteractor(GitHubUserClient userClient,
                             IAccountDao accountDao,
                             SessionHelper sessionHelper,
                             PrefHelper prefHelper,
                             ModelConverter modelConverter) {
        this.userClient = userClient;
        this.accountDao = accountDao;
        this.sessionHelper = sessionHelper;
        this.prefHelper = prefHelper;
        this.modelConverter = modelConverter;
    }

    @Override
    public Observable<AccountModel> loadAccountInfo(String accountName) {
        return accountDao.findAccountByName(accountName)
                .flatMap(accountModel ->
                        accountModel == null ?
                                loadAccountInfoRemote(accountName) : Observable.just(accountModel))
                .compose(Interactors.applySchedulersIO());
    }

    @Override
    public Observable<AccountModel> reloadCurrentAccount() {
        return userClient.getCurrentUserInfo()
                .map(modelConverter::toAccountModel)
                .doOnNext(this::storeCurrentAccount)
                .compose(Interactors.applySchedulersIO());
    }

    @Override
    public Observable<Void> logOutAccount() {
        sessionHelper.setCurrentAccount(null);
        prefHelper.setLoggedAccountName(null);

        return Observable.empty();
    }

    /**
     * Does not run in a separate thread
     */
    @NonNull
    private Observable<AccountModel> loadAccountInfoRemote(String accountName) {
        return userClient.getUserInfo(accountName)
                .map(modelConverter::toAccountModel)
                .doOnNext(accountDao::saveAccount);
    }

    private void storeCurrentAccount(AccountModel accountModel) {
        accountModel.setAccessToken(sessionHelper.getCurrentAccount().getAccessToken());
        accountDao.saveAccount(accountModel);
        sessionHelper.setCurrentAccount(accountModel);
    }
}
