package com.epam.traing.gitcl.presentation.ui;

import com.epam.traing.gitcl.data.interactor.repositories.IRepositoriesInteractor;

import javax.inject.Inject;

/**
 * Created by Yahor_Fralou on 2/27/2017 7:17 PM.
 */

public class RepoInfoPresenter implements IRepoInfoPresenter {

    private IRepoInfoView view;
    private IRepositoriesInteractor interactor;

    @Inject
    public RepoInfoPresenter(IRepositoriesInteractor repositoriesInteractor) {
        this.interactor = repositoriesInteractor;
    }

    @Override
    public void attachView(IRepoInfoView repoInfoView) {
        this.view = repoInfoView;
    }
}
