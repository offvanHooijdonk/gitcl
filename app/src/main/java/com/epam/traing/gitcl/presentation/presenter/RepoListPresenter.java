package com.epam.traing.gitcl.presentation.presenter;

import com.epam.traing.gitcl.data.interactor.repositories.IRepositoriesInteractor;
import com.epam.traing.gitcl.db.model.RepoModel;
import com.epam.traing.gitcl.presentation.ui.IRepoListView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Yahor_Fralou on 2/22/2017 11:57 AM.
 */

public class RepoListPresenter implements IRepoListPresenter {

    private IRepoListView view;
    private IRepositoriesInteractor reposInteractor;

    @Inject
    public RepoListPresenter(IRepositoriesInteractor repositoriesInteractor) {
        this.reposInteractor = repositoriesInteractor;
    }

    @Override
    public void attachView(IRepoListView view) {
        this.view = view;
    }

    @Override
    public void onViewCreate() {
        getReposFromDB();

        reposInteractor.subscribeReposChanges()
                .subscribe(this::onReposUpdated, this::onError);
        // TODO decide if need update Repos at the moment and update if so
    }

    @Override
    public void onRefreshTriggered() {
        view.showRefreshProgress(true);

        reposInteractor.loadRepositories()
                .subscribe(repoModels -> {
                    view.showRefreshProgress(false);
                }, this::onError);
    }

    private void getReposFromDB() {
        reposInteractor.getRepositories()
                .doOnNext(this::onReposUpdated)
                .subscribe(repoModels -> {}, this::onError);
    }

    private void onReposUpdated(List<RepoModel> repoModels) {
        view.updateRepoList(repoModels);

        view.showRefreshProgress(false);
    }

    private void onError(Throwable th) {
        view.showRefreshProgress(false);
        view.displayError(th);
    }
}
