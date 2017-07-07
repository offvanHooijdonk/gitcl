package com.epam.traing.gitcl.presentation.presenter;

import com.epam.traing.gitcl.data.interactor.repositories.IRepositoriesInteractor;
import com.epam.traing.gitcl.model.RepoModel;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.presentation.ui.IRepoListView;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Yahor_Fralou on 2/22/2017 11:57 AM.
 */

public class RepoListPresenter extends AbstractSubscribePresenter implements IRepoListPresenter {

    private IRepoListView view;
    private IRepositoriesInteractor reposInteractor;
    private PrefHelper prefHelper;

    @Inject
    public RepoListPresenter(IRepositoriesInteractor repositoriesInteractor, PrefHelper prefHelper) {
        this.reposInteractor = repositoriesInteractor;
        this.prefHelper = prefHelper;
    }

    @Override
    public void attachView(IRepoListView view) {
        this.view = view;
    }

    @Override
    public void onViewShows() {
        getReposFromDB();
        // decide if need update Repos at the moment and update if so
        long currentTimeMillis = new Date().getTime();
        int repoUpdateMins = prefHelper.getReposListUpdateIntervalMins();
        if (repoUpdateMins != PrefHelper.VALUE_NEVER &&
                currentTimeMillis - prefHelper.getReposLastUpdateTime() > repoUpdateMins * 60 * 1000) {
            loadRepositoriesList();
        }
    }

    @Override
    public void onRefreshTriggered() {
        loadRepositoriesList();
    }

    @Override
    public void detachView() {
        unsubscribeAll();

        view = null;
    }

    private void loadRepositoriesList() {
        view.showRefreshProgress(true);

        collectSubscription(

                reposInteractor.loadRepositories()
                        .subscribe(repoModels -> {
                            view.showRefreshProgress(false);
                            prefHelper.setReposLastUpdateTime(new Date().getTime());
                        }, this::onError)
        );
    }

    private void getReposFromDB() {
        collectSubscription(

                reposInteractor.getRepositories()
                        .subscribe(this::onReposUpdated, this::onError)
        );
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
