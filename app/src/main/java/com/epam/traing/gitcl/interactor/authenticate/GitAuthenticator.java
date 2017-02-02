package com.epam.traing.gitcl.interactor.authenticate;

import com.epam.traing.gitcl.app.GitClApplication;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.tables.AccountTable;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.helper.account.GitHubAccountAuthenticator;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.ReplaySubject;

/**
 * Created by Yahor_Fralou on 1/27/2017 5:59 PM.
 */

public class GitAuthenticator implements IAuthenticator {
// TODO store in kind of properties
    private static final String OAUTH_KEY = "";
    private static final String OAUTH_SECRET = "";
    private static final String OAUTH_URL = "";
    private static final String OAUTH_CALLBACK_URL = "";

    @Inject
    StorIOSQLite storIOSQLite;
    @Inject
    PrefHelper prefHelper;
    @Inject
    GitHubAccountAuthenticator gitHubAuthenticator;

    public GitAuthenticator() {
        GitClApplication.getAuthenticatorComponent().inject(this);
    }

    @Override
    public Observable<AccountModel> startAuthentication() {
        ReplaySubject<AccountModel> s = ReplaySubject.create();

        loadAccount().subscribe(accountModel -> {
            saveAccount(accountModel);
            prefHelper.setLoggedAccountName(accountModel.getAccountName());
            GitClApplication.setAccount(accountModel);
            s.onNext(accountModel);
            s.onCompleted();
        });
        return s;
    }

    public String getOAuthUrl() {
        // TODO create OAuth URL here!
        return null;
    }

    public String getOAuthCallbackUrl() {
        return OAUTH_CALLBACK_URL;
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

    private Observable<AccountModel> loadAccount() {
        final AccountModel accountModel = new AccountModel();
        accountModel.setId(42L);
        accountModel.setFirstName("John");
        accountModel.setLastName("Doe");
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
