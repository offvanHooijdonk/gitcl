package com.epam.traing.gitcl.interactor;

import android.os.Handler;

import com.epam.traing.gitcl.app.GitClApplication;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.tables.AccountTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Yahor_Fralou on 1/27/2017 5:59 PM.
 */

public class GitAuthenticator implements IAuthenticator {

    @Inject
    StorIOSQLite storIOSQLite;

    private AuthenticationListener listener;

    public GitAuthenticator() {
        GitClApplication.getAuthenticatorComponent().inject(this);
    }

    @Override
    public void setListener(AuthenticationListener listener) {
        this.listener = listener;
    }

    @Override
    public void authenticate() {
        List<AccountModel> models = findAccountLocal();
        if (!models.isEmpty()) {
            AccountModel accountModel = models.get(0); // assume single account for now
            if (listener != null) {
                listener.onSuccess(accountModel);
            }
        } else {
            loadAccount();
        }

    }

    private void loadAccount() {
        final AccountModel accountModel = new AccountModel();
        accountModel.setId(42L);
        accountModel.setFirstName("John");
        accountModel.setLastName("Doe");
        accountModel.setAccountName("goJohnyGo");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                saveAccount(accountModel);
                if (listener != null) {
                    listener.onSuccess(accountModel);
                }
            }
        }, 2000);
    }

    private List<AccountModel> findAccountLocal() {
        return storIOSQLite.get().listOfObjects(AccountModel.class).withQuery(Query.builder().table(AccountTable.TABLE).build()).prepare().executeAsBlocking();
    }

    private void saveAccount(AccountModel accountModel) {
        storIOSQLite.put().object(accountModel).prepare().executeAsBlocking();
    }
}
