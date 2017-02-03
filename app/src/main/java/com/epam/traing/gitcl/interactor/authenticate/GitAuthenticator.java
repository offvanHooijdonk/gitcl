package com.epam.traing.gitcl.interactor.authenticate;

import android.net.Uri;

import com.epam.traing.gitcl.app.GitClApplication;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.tables.AccountTable;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.network.GitHubTokenClient;
import com.epam.traing.gitcl.network.GitHubUserClient;
import com.epam.traing.gitcl.network.json.AccessTokenJson;
import com.epam.traing.gitcl.network.json.AccountJson;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.concurrent.TimeUnit;

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
    private static final String OAUTH_KEY = "6203c4ce6b8758a78dce";
    private static final String OAUTH_SECRET = "c71efb17a495bce469e238624de486b7bce1edf5";
    private static final String OAUTH_SCOPES = "user public_repo";
    private static final String OAUTH_URL = "https://github.com/login/oauth/authorize?scope=%s&client_id=%s";
    private static final String OAUTH_CALLBACK_URL = "githubcl://githubcloauth/callback";

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
            tokenClient.requestAccessToken(OAUTH_KEY, OAUTH_SECRET, code)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(AccessTokenJson::getAccessToken)
                    .subscribe(token -> {
                        userClient.getUserInfo("token " + token)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(this::convertJsonToAccountModel)
                                .flatMap(accountModel -> {
                                    accountModel.setAccessToken(token);
                                    return Observable.just(accountModel);
                                })
                                .subscribe(this::onAccountLoaded);
                    },th -> {
                        loadAccountSubject.onError(th);
                    });
        } else {
            String errorMsg = uri.getQueryParameter(QPARAM_ERROR);
            // TODO handle error
        }

        return loadAccountSubject;
    }

    private void onAccountLoaded(AccountModel accountModel) {
        saveAccount(accountModel);
        GitClApplication.setAccount(accountModel);
        prefHelper.setLoggedAccountName(accountModel.getAccountName());
        loadAccountSubject.onNext(accountModel);
        // TODO start avatar loading if not null
    }

    @Override
    public Observable<AccountModel> getLoggedAccount() {
        ReplaySubject<AccountModel> s = ReplaySubject.create();

        String loggedAccountName = prefHelper.getLoggedAccountName();
        if (loggedAccountName != null) {
            findAccountLocal(loggedAccountName).subscribe(accountModel -> {
                if (accountModel != null) {
                    s.onNext(accountModel);
                    s.onCompleted();
                } else {
                    s.onError(new AccountNotFoundException(loggedAccountName));
                }
            });
        } else {
            // TODO handle. Maybe won't be a case in future
        }

        return s;
    }

    @Override
    public String getOAuthUrl() {
        // TODO create OAuth URL here!
        return String.format(OAUTH_URL, OAUTH_SCOPES, OAUTH_KEY);
    }

    @Override
    public String getOAuthCallbackUrl() {
        return OAUTH_CALLBACK_URL;
    }

    private AccountModel convertJsonToAccountModel(AccountJson json) {
        AccountModel model = new AccountModel();
        model.setAccountName(json.getLogin());
        model.setPersonName(json.getPersonName());
        model.setAvatar(json.getAvatarUrl());
        model.setEmail(json.getEmail());
        return model;
    }

    private Observable<AccountModel> loadAccount() {
        final AccountModel accountModel = new AccountModel();
        accountModel.setId(42L);
        accountModel.setPersonName("John Doe");
        accountModel.setAccountName("goJohnyGo");

        return Observable.just(accountModel)
                .delay(2, TimeUnit.SECONDS);
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
