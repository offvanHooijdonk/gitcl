package com.epam.traing.gitcl.interactor.authenticate;

import android.net.Uri;
import android.util.Log;

import com.epam.traing.gitcl.app.GitClApplication;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.tables.AccountTable;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.network.Constants;
import com.epam.traing.gitcl.network.GitHubTokenClient;
import com.epam.traing.gitcl.network.GitHubUserClient;
import com.epam.traing.gitcl.network.json.AccountJson;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.schedulers.Schedulers;

/**
 * Created by Yahor_Fralou on 1/27/2017 5:59 PM.
 */

public class GitAuthenticator implements IAuthenticator {
    private static final String QPARAM_CODE = "code";
    private static final String QPARAM_ERROR = "error";

    /*private */StorIOSQLite storIOSQLite;

    /*private */PrefHelper prefHelper;

    /*private */GitHubTokenClient tokenClient;

    /*private */GitHubUserClient userClient;

    @Inject
    public GitAuthenticator(StorIOSQLite storIOSQLite, PrefHelper prefHelper, GitHubTokenClient tokenClient, GitHubUserClient userClient) {
        this.storIOSQLite = storIOSQLite;
        this.prefHelper = prefHelper;
        this.tokenClient = tokenClient;
        this.userClient = userClient;
    }

    /*public GitAuthenticator() {
        GitClApplication.getAuthenticatorComponent().inject(this);
    }*/

    @Override
    public Observable<AccountModel> authorizeFromCallback(String callbackUrl) {
        Uri uri = Uri.parse(callbackUrl);

        return Observable.just(uri.getQueryParameter(QPARAM_CODE))
                .flatMap(code -> {
                    if (code == null) {
                        String errorMsg = uri.getQueryParameter(QPARAM_ERROR);
                        throw Exceptions.propagate(new AuthenticationException(errorMsg != null ? errorMsg : ""));
                    } else {
                        Log.d(GitClApplication.LOG, "Request Access token");
                        return tokenClient.requestAccessToken(Constants.Api.OAUTH_KEY, Constants.Api.OAUTH_SECRET, code)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .doOnNext(tokenJson -> {
                    Log.d(GitClApplication.LOG, "Token received: " + tokenJson.getTokenType() + " " + tokenJson.getAccessToken());
                    prefHelper.setTokenType(tokenJson.getTokenType());
                    prefHelper.setAccessToken(tokenJson.getAccessToken());
                })
                .flatMap(tokenJson -> {
                    Log.d(GitClApplication.LOG, "Call Api '/user'");
                    return userClient.getUserInfo()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                })
                .map(this::convertJsonToAccountModel)
                .doOnNext(accountModel -> {
                    accountModel.setAccessToken(prefHelper.getAccessToken());
                    onAccountLoaded(accountModel);
                });
    }

    private void onAccountLoaded(AccountModel accountModel) {
        // if no Person Name provided - set it to Account Name instead
        if (accountModel.getPersonName() == null) {
            accountModel.setPersonName(accountModel.getAccountName());
        }
        saveAccount(accountModel);
        GitClApplication.setAccount(accountModel);
        prefHelper.setLoggedAccountName(accountModel.getAccountName());

        Log.d(GitClApplication.LOG, "Account passing to presenter");

        // TODO start avatar loading if not null
    }

    @Override
    public Observable<Boolean> getShowLogin() {
        return Observable.fromCallable(prefHelper::isShowLogin);
    }

    @Override
    public Observable<AccountModel> prepareOnLoginData() {
        final String[] accountName = new String[1];
        return Observable.fromCallable(prefHelper::getLoggedAccountName)
                .flatMap(loggedAccountName -> {
                    accountName[0] = loggedAccountName;
                    if (loggedAccountName == null) {
                        return Observable.defer(null);
                    } else {
                        return findAccountLocal(loggedAccountName);
                    }
                })
                .doOnNext(accountModel -> {
                    if (accountModel != null) {
                        GitClApplication.setAccount(accountModel);
                        prefHelper.setAccessToken(accountModel.getAccessToken());
                    } else {
                        // TODO pass AccountName
                        throw Exceptions.propagate(new AccountNotFoundException(accountName[0]));
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
        // TODO implement auto conversion or move to a converter
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
