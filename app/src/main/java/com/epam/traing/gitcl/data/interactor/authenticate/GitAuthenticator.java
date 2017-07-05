package com.epam.traing.gitcl.data.interactor.authenticate;

import android.net.Uri;
import android.util.Log;

import com.epam.traing.gitcl.app.GitClientApplication;
import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.db.dao.IAccountDao;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.helper.SessionHelper;
import com.epam.traing.gitcl.network.Constants;
import com.epam.traing.gitcl.network.GitHubTokenClient;
import com.epam.traing.gitcl.network.GitHubUserClient;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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
    private IAccountDao accountDao;
    private SessionHelper session;
    private ModelConverter modelConverter;

    public GitAuthenticator(PrefHelper prefHelper,
                            GitHubTokenClient tokenClient,
                            GitHubUserClient userClient,
                            IAccountDao accountDao,
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
                        return Observable.error(
                                new Exception(errorMsg != null ? errorMsg : "Unspecified error when authenticating the user.")); // TODO add custom Exception?
                    } else {
                        Log.d(GitClientApplication.LOG, "Request Access token");
                        return tokenClient.requestAccessToken(Constants.Api.OAUTH_KEY, Constants.Api.OAUTH_SECRET, code);
                    }
                })
                .doOnNext(tokenJson -> {
                    Log.d(GitClientApplication.LOG, "Token received: " + tokenJson.getTokenType() + " " + tokenJson.getAccessToken());
                    prefHelper.setTokenType(tokenJson.getTokenType());
                    prefHelper.setAccessToken(tokenJson.getAccessToken());
                })
                .flatMap(tokenJson -> userClient.getCurrentUserInfo())
                .map(modelConverter::toAccountModel)
                .doOnNext(accountModel -> accountModel.setAccessToken(prefHelper.getAccessToken()))
                .doOnNext(this::onAccountLoaded)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void onAccountLoaded(AccountModel accountModel) {
        // if no Person Name provided - set it to Account Name instead
        if (accountModel.getPersonName() == null) {
            accountModel.setPersonName(accountModel.getAccountName());
        }
        accountDao.saveAccount(accountModel);
        session.setCurrentAccount(accountModel);
        prefHelper.setLoggedAccountName(accountModel.getAccountName());

        Log.d(GitClientApplication.LOG, "Account passing to presenter");
    }

    @Override
    public Observable<AccountModel> prepareOnLoginData() {
        return Observable.fromCallable(prefHelper::getLoggedAccountName)
                .flatMap(loggedAccountName -> {
                    if (loggedAccountName == null) {
                        return Observable.just(null);
                    } else {
                        return accountDao.findAccountByName(loggedAccountName);
                    }
                })
                .doOnNext(accountModel -> {
                    if (accountModel != null) {
                        session.setCurrentAccount(accountModel);
                        prefHelper.setAccessToken(accountModel.getAccessToken());
                    } else {
                        throw new AccountNotFoundException("");
                    }
                });
    }

    @Override
    public String composeOAuthUrl() {
        return String.format(Constants.Api.OAUTH_URL, Constants.Api.OAUTH_SCOPES, Constants.Api.OAUTH_KEY);
    }

}
