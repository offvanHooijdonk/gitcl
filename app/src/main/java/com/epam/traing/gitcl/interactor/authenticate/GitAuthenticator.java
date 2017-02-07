package com.epam.traing.gitcl.interactor.authenticate;

import android.net.Uri;
import android.util.Log;

import com.epam.traing.gitcl.app.Constants;
import com.epam.traing.gitcl.app.GitClApplication;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.tables.AccountTable;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.network.GitHubTokenClient;
import com.epam.traing.gitcl.network.GitHubUserClient;
import com.epam.traing.gitcl.network.json.AccountJson;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.ReplaySubject;

/**
 * Created by Yahor_Fralou on 1/27/2017 5:59 PM.
 */

public class GitAuthenticator implements IAuthenticator {
    // TODO store in kind of properties
    private static final String QPARAM_CODE = "code";
    private static final String QPARAM_ERROR = "error";

    @Inject
    StorIOSQLite storIOSQLite;
    @Inject
    PrefHelper prefHelper;
    @Inject
    GitHubTokenClient tokenClient;
    @Inject
    GitHubUserClient userClient;

    private ReplaySubject<AccountModel> loadAccountSubject = ReplaySubject.create();

    public GitAuthenticator() {
        GitClApplication.getAuthenticatorComponent().inject(this);
    }

    @Override
    public Observable<AccountModel> authorizeFromCallback(String callbackUrl) {
        Uri uri = Uri.parse(callbackUrl);
        String code = uri.getQueryParameter(QPARAM_CODE);
        if (code != null) {
            tokenClient.requestAccessToken(Constants.Api.OAUTH_KEY, Constants.Api.OAUTH_SECRET, code)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(tokenJson -> {
                                Log.d(GitClApplication.LOG, "Token received: " + tokenJson.getTokenType() + " " + tokenJson.getAccessToken());
                                prefHelper.setTokenType(tokenJson.getTokenType());
                                userClient.getUserInfo(tokenJson.getTokenType() + " " + tokenJson.getAccessToken())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .map(this::convertJsonToAccountModel)
                                        .flatMap(accountModel -> {
                                            accountModel.setAccessToken(tokenJson.getTokenType());
                                            return Observable.just(accountModel);
                                        })
                                        .subscribe(this::onAccountLoaded, loadAccountSubject::onError);
                            },
                            loadAccountSubject::onError);
        } else {
            String errorMsg = uri.getQueryParameter(QPARAM_ERROR);
            // TODO handle error
        }

        return loadAccountSubject;
    }

    private void onAccountLoaded(AccountModel accountModel) {
        // TODO if no Person Name provided - set it to Account Name instead
        saveAccount(accountModel);
        GitClApplication.setAccount(accountModel);
        prefHelper.setLoggedAccountName(accountModel.getAccountName());

        Log.d(GitClApplication.LOG, "Account passing to presenter");
        loadAccountSubject.onNext(accountModel);

        // TODO start avatar loading if not null
    }

    @Override
    public Observable<Boolean> getShowLogin() {
        return Observable.just(prefHelper.isShowLogin());
    }

    @Override
    public Observable<AccountModel> prepareOnLoginData() {
        return Observable.fromCallable(prefHelper::getLoggedAccountName)
                .flatMap(loggedAccountName -> {
                    if (loggedAccountName == null) {
                        return Observable.defer(null);
                    } else {
                        return findAccountLocal(loggedAccountName);
                    }
                })
                .doOnNext(accountModel -> {
                    if (accountModel != null) {
                        GitClApplication.setAccount(accountModel);
                    }
                });
    }

    @Override
    public String composeOAuthUrl() {
        return String.format(Constants.Api.OAUTH_URL, Constants.Api.OAUTH_SCOPES, Constants.Api.OAUTH_KEY);
    }

    @Override
    public void setShowLogin(boolean show) {
        prefHelper.setShowLogin(show);
    }

    private AccountModel convertJsonToAccountModel(AccountJson json) {
        AccountModel model = new AccountModel();
        model.setAccountName(json.getLogin());
        model.setPersonName(json.getPersonName());
        model.setAvatar(json.getAvatarUrl());
        model.setEmail(json.getEmail());
        return model;
    }

    private Observable<AccountModel> findAccountLocal(String accountName) {
        // TODO move to DAO
        return storIOSQLite.get().object(AccountModel.class)
                .withQuery(Query.builder()
                        .table(AccountTable.TABLE)
                        .where(String.format("%s='%s'", AccountTable.COLUMN_ACCOUNT_NAME, accountName)).build())
                .prepare().asRxObservable();
    }

    private void saveAccount(AccountModel accountModel) {
        // TODO move to DAO
        storIOSQLite.put().object(accountModel).prepare().executeAsBlocking();
    }
}
