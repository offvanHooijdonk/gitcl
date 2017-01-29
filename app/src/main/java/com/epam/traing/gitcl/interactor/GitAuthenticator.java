package com.epam.traing.gitcl.interactor;

import android.os.Handler;

import com.epam.traing.gitcl.app.GitClApplication;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.tables.AccountTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

/**
 * Created by Yahor_Fralou on 1/27/2017 5:59 PM.
 */

public class GitAuthenticator implements IAuthenticator {

    @Inject
    StorIOSQLite storIOSQLite;

    public GitAuthenticator() {
        GitClApplication.getAuthenticatorComponent().inject(this);
    }

    @Override
    public Observable<AccountModel> authenticate() {
        ReplaySubject<AccountModel> s = ReplaySubject.create();

        findAccountLocal().subscribe(accountModels -> {
            if (!accountModels.isEmpty()) {
                s.onNext(accountModels.get(0));
                s.onCompleted();
            } else {
                //s.onNext(null);
                loadAccount(s);
            }
        });

        return s;

        /*List<AccountModel> models = findAccountLocal();
        if (!models.isEmpty()) {
            AccountModel accountModel = models.get(0); // assume single account for now
            if (listener != null) {
                listener.onSuccess(accountModel);
            }
        } else {
            loadAccount();
        }*/

    }

    private void loadAccount(Observer<AccountModel> o) {
        final AccountModel accountModel = new AccountModel();
        accountModel.setId(42L);
        accountModel.setFirstName("John");
        accountModel.setLastName("Doe");
        accountModel.setAccountName("goJohnyGo");

        Observable.just(accountModel)
                .delay(2, TimeUnit.SECONDS)
                .subscribe(accountModel1 -> {
                    saveAccount(accountModel);
                    o.onNext(accountModel);
                });
    }

    private Observable<List<AccountModel>> findAccountLocal() {
        return storIOSQLite.get().listOfObjects(AccountModel.class).withQuery(Query.builder().table(AccountTable.TABLE).build()).prepare().asRxObservable();//executeAsBlocking();
    }

    private void saveAccount(AccountModel accountModel) {
        storIOSQLite.put().object(accountModel).prepare().executeAsBlocking();
    }
}
