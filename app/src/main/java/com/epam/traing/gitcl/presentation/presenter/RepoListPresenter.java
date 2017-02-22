package com.epam.traing.gitcl.presentation.presenter;

import com.epam.traing.gitcl.data.interactor.repositories.IRepositoriesInteractor;
import com.epam.traing.gitcl.presentation.ui.IRepoListView;

import javax.inject.Inject;

/**
 * Created by Yahor_Fralou on 2/22/2017 11:57 AM.
 */

public class RepoListPresenter implements IRepoListPresenter {

    private IRepoListView view;
    private IRepositoriesInteractor repositoriesInteractor;

    @Inject
    public RepoListPresenter(IRepositoriesInteractor repositoriesInteractor) {
        this.repositoriesInteractor = repositoriesInteractor;
    }

    @Override
    public void attachView(IRepoListView view) {
        this.view = view;
    }
}
