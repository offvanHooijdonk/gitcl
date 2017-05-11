package com.epam.traing.gitcl.data.interactor.account;

import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.db.dao.IAccountDao;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.helper.SessionHelper;
import com.epam.traing.gitcl.network.GitHubUserClient;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by off on 19.02.2017.
 */

public class AccountInteractor implements IAccountInteractor {

    private GitHubUserClient userClient;
    private SessionHelper sessionHelper;
    private ModelConverter modelConverter;
    private PrefHelper prefHelper;
    private IAccountDao accountDao;

    @Inject
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

    @Deprecated
    @Override
    public Observable<AccountModel> loadAccount(String accountName) {
        return accountDao.findAccountByName(accountName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(accountModel -> {
                    if (accountModel == null) {
                        return onNoAccountLocal(accountName);
                    } else {
                        return Observable.just(accountModel);
                    }
                }).filter(accountModel -> accountModel != null);
    }

    @Override
    public Observable<AccountModel> reloadCurrentAccount() {
        return userClient.getCurrentUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(modelConverter::toAccountModel)
                .doOnNext(this::storeCurrentAccount);
    }

    @Override
    public Observable<Void> logOutAccount() {
        sessionHelper.setCurrentAccount(null);
        prefHelper.setLoggedAccountName(null);

        return Observable.empty();
    }

    private Observable<AccountModel> onNoAccountLocal(String accountName) {
        return userClient.getUserInfo(accountName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(modelConverter::toAccountModel)
                .doOnNext(accountDao::saveAccount);
    }

    private void storeCurrentAccount(AccountModel accountModel) {
        accountModel.setAccessToken(sessionHelper.getCurrentAccount().getAccessToken());
        accountDao.saveAccount(accountModel);
        sessionHelper.setCurrentAccount(accountModel);
    }
}
