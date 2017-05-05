package com.epam.traing.gitcl.presentation.presenter;

import android.util.Log;

import com.epam.traing.gitcl.app.Application;
import com.epam.traing.gitcl.data.interactor.account.IAccountInteractor;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.network.Constants;
import com.epam.traing.gitcl.presentation.ui.IMainView;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Yahor_Fralou on 2/7/2017 12:24 PM.
 */

public class MainPresenter extends AbstractSubscribePresenter implements IMainPresenter {
    private IMainView view;
    private IAccountInteractor accountInteractor;
    private PrefHelper prefHelper;
    // TODO CompositeSubscription

    @Inject
    public MainPresenter(IAccountInteractor accountInteractor, PrefHelper prefHelper/*, ISearchInteractor searchIntercator*/) {
        this.accountInteractor = accountInteractor;
        this.prefHelper = prefHelper;
    }

    @Override
    public void attachView(IMainView mainView) {
        this.view = mainView;
    }

    @Override
    public void onViewPrepare() {
        updateAccountInfo();
    }

    @Override
    public void onLogoutSelected() {
        view.showLogoutDialog(true);
    }

    @Override
    public void onLogoutConfirmed() {
        getCompositeSubscription().add(

                accountInteractor.logOutAccount().subscribe(o -> {
                }, this::handleError, () -> {
                    prefHelper.setShowLogin(true);
                    view.showLogoutDialog(false);
                    view.startLoginActivity();
                })
        );
    }

    @Override
    public void onLogoutCanceled() {
        view.showLogoutDialog(false);
    }

    @Override
    public void detachView() {
        unsubscribeAll();

        view = null;
    }

    private void updateAccountInfo() {
        if (hasTimePassed(prefHelper.getAccountLastUpdateTime(), Constants.Refresh.REFRESH_ACCOUNT_MILLS)) {
            requestAccountInfo();
        }
    }

    private void requestAccountInfo() {
        getCompositeSubscription().add(

                accountInteractor.reloadCurrentAccount()
                        .subscribe(accountModel -> view.updateAccountInfo(), this::handleError)
        );
    }

    private boolean hasTimePassed(long timeFrom, int timePass) {
        return new Date().getTime() - timeFrom >= timePass;
    }

    private void handleError(Throwable th) {
        Log.e(Application.LOG, "Error.", th);
        view.showError(th);
    }
}
