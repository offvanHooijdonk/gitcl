package com.epam.traing.gitcl.data.interactor.authenticate;

import android.net.Uri;
import android.util.Log;

import com.epam.traing.gitcl.app.Application;
import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.db.dao.AccountDao;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.helper.SessionHelper;
import com.epam.traing.gitcl.network.Constants;
import com.epam.traing.gitcl.network.GitHubTokenClient;
import com.epam.traing.gitcl.network.GitHubUserClient;
import com.epam.traing.gitcl.network.json.AccountJson;

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

    private PrefHelper prefHelper;
    private GitHubTokenClient tokenClient;
    private GitHubUserClient userClient;
    private AccountDao accountDao;
    private SessionHelper session;
    private ModelConverter modelConverter;

    @Inject
    public GitAuthenticator(PrefHelper prefHelper,
                            GitHubTokenClient tokenClient,
                            GitHubUserClient userClient,
                            AccountDao accountDao,
                            SessionHelper session,
                            ModelConverter modelConverter) {
        this.prefHelper = prefHelper;
        this.tokenClient = tokenClient;
        this.userClient = userClient;
        this.accountDao = accountDao;
        this.session = session;
        this.modelConverter = modelConverter;
    }

    @Override
    public Observable<AccountModel> authorizeFromCallback(String callbackUrl) {
        Uri uri = Uri.parse(callbackUrl);

        return Observable.just(uri.getQueryParameter(QPARAM_CODE))
                .flatMap(code -> {
                    if (code == null) {
                        String errorMsg = uri.getQueryParameter(QPARAM_ERROR);
                        throw Exceptions.propagate(new AuthenticationException(errorMsg != null ? errorMsg : ""));
                    } else {
                        Log.d(Application.LOG, "Request Access token");
                        return tokenClient.requestAccessToken(Constants.Api.OAUTH_KEY, Constants.Api.OAUTH_SECRET, code)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .doOnNext(tokenJson -> {
                    Log.d(Application.LOG, "Token received: " + tokenJson.getTokenType() + " " + tokenJson.getAccessToken());
                    prefHelper.setTokenType(tokenJson.getTokenType());
                    prefHelper.setAccessToken(tokenJson.getAccessToken());
                })
                .flatMap(tokenJson -> {
                    Log.d(Application.LOG, "Call Api '/user'");
                    return userClient.getCurrentUserInfo()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                })
                .map(modelConverter::toAccountModel)
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
        accountDao.saveAccount(accountModel);
        session.setCurrentAccount(accountModel);
        prefHelper.setLoggedAccountName(accountModel.getAccountName());

        Log.d(Application.LOG, "Account passing to presenter");

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
                        return accountDao.findAccountByName(loggedAccountName);
                    }
                })
                .doOnNext(accountModel -> {
                    if (accountModel != null) {
                        session.setCurrentAccount(accountModel);
                        prefHelper.setAccessToken(accountModel.getAccessToken());
                    } else {
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

}
