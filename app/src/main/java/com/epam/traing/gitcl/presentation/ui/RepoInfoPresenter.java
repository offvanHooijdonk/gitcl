package com.epam.traing.gitcl.presentation.ui;

import com.epam.traing.gitcl.data.interactor.account.IAccountInteractor;
import com.epam.traing.gitcl.data.interactor.repositories.IRepositoriesInteractor;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.model.RepoModel;
import com.epam.traing.gitcl.helper.PrefHelper;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Yahor_Fralou on 2/27/2017 7:17 PM.
 */

public class RepoInfoPresenter implements IRepoInfoPresenter {

    private IRepoInfoView view;
    private IRepositoriesInteractor repoInteractor;
    private IAccountInteractor accountInteractor;
    private PrefHelper prefHelper;

    private RepoModel repoModel;

    @Inject
    public RepoInfoPresenter(IRepositoriesInteractor repositoriesInteractor,
                             IAccountInteractor accountInteractor,
                             PrefHelper prefHelper) {
        this.repoInteractor = repositoriesInteractor;
        this.accountInteractor = accountInteractor;
        this.prefHelper = prefHelper;
    }

    @Override
    public void attachView(IRepoInfoView repoInfoView) {
        this.view = repoInfoView;
    }

    @Override
    public void onViewCreated(RepoModel repoModel) {
        this.repoModel = repoModel;

        getRepoInfoFromDB();
        loadAccountInfo();
        if (new Date().getTime() - repoModel.getVerboseUpdateDate() > prefHelper.getRepoInfoUpdateIntervalMins() * 60 * 1000) {
            loadRepoInfo();
        }
    }



    @Override
    public void onRefreshTriggered() {
        loadAccountInfo();
        loadRepoInfo();
    }

    private void getRepoInfoFromDB() {
        repoInteractor.getRepoInfo(repoModel.getId())
                .subscribe(this::onRepoLoaded, this::onError);
    }

    private void loadAccountInfo() {
        accountInteractor.loadAccount(repoModel.getOwnerName())
                .subscribe(this::onAccountLoaded, this::onError);
    }

    private void loadRepoInfo() {
        view.showRefreshingProcess(true);
        repoInteractor.loadVerbose(repoModel)
                .subscribe(this::onRepoLoaded, this::onError);
    }

    private void onAccountLoaded(AccountModel accountModel) {
        view.updateOwnerInfo(accountModel);
    }

    private void onRepoLoaded(RepoModel repoModel) {
        this.repoModel = repoModel;
        view.displayRepoInfo(repoModel);
        view.showRefreshingProcess(false);
    }

    private void onError(Throwable th) {

    }
}
