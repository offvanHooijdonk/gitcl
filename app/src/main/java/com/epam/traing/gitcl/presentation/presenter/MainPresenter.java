package com.epam.traing.gitcl.presentation.presenter;

import com.epam.traing.gitcl.data.interactor.account.IAccountInteractor;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.network.Constants;
import com.epam.traing.gitcl.presentation.ui.IMainView;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Yahor_Fralou on 2/7/2017 12:24 PM.
 */

public class MainPresenter implements IMainPresenter {
    private IMainView view;

    private IAccountInteractor accountInteractor;
    private PrefHelper prefHelper;

    @Inject
    public MainPresenter(IAccountInteractor accountInteractor, PrefHelper prefHelper) {
        this.accountInteractor = accountInteractor;
        this.prefHelper = prefHelper;
    }

    @Override
    public void attachView(IMainView mainView) {
        this.view = mainView;
    }

    @Override
    public void onViewPrepare() {
        subscribeAccountChange();

        updateAccountInfo();
    }

    @Override
    public void onLogoutSelected() {
        view.showLogoutDialog(true);
    }

    @Override
    public void onLogoutConfirmed() {
        accountInteractor.logOutAccount().subscribe(o -> {}, th -> {}, () -> {
            prefHelper.setShowLogin(true);
            view.showLogoutDialog(false);
            view.startLoginActivity();
        });
    }

    @Override
    public void onLogoutCanceled() {
        view.showLogoutDialog(false);
    }

    private void subscribeAccountChange() {
        accountInteractor.subscribeCurrentAccountChange()
                .subscribe(accountModel -> view.updateAccountInfo());

    }

    private void updateAccountInfo() {
        if (hasTimePassed(prefHelper.getAccountLastUpdateTime(), Constants.Refresh.REFRESH_ACCOUNT_MILLS)) {
            requestAccountInfo();
        }
    }


    private void requestAccountInfo() {
        accountInteractor.reloadCurrentAccount()
                .subscribe(accountModel -> view.updateAccountInfo());
    }

    private boolean hasTimePassed(long timeFrom, int timePass) {
        return new Date().getTime() - timeFrom >= timePass;
    }
}
