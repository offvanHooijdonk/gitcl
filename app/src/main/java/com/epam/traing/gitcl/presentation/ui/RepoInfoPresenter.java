package com.epam.traing.gitcl.presentation.ui;

import com.epam.traing.gitcl.data.interactor.account.IAccountInteractor;
import com.epam.traing.gitcl.data.interactor.repositories.IRepositoriesInteractor;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.model.RepoModel;

import javax.inject.Inject;

/**
 * Created by Yahor_Fralou on 2/27/2017 7:17 PM.
 */

public class RepoInfoPresenter implements IRepoInfoPresenter {

    private IRepoInfoView view;
    private IRepositoriesInteractor repoInteractor;
    private IAccountInteractor accountInteractor;

    @Inject
    public RepoInfoPresenter(IRepositoriesInteractor repositoriesInteractor, IAccountInteractor accountInteractor) {
        this.repoInteractor = repositoriesInteractor;
        this.accountInteractor = accountInteractor;
    }

    @Override
    public void attachView(IRepoInfoView repoInfoView) {
        this.view = repoInfoView;
    }

    @Override
    public void onViewCreated(RepoModel repoModel) {
        accountInteractor.loadAccount(repoModel.getOwnerName())
                .subscribe(this::onAccountLoaded);
    }

    private void onAccountLoaded(AccountModel accountModel) {
        view.updateOwnerInfo(accountModel);
    }
}
