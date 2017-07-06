package com.epam.traing.gitcl.data.interactor.repositories;

import android.support.annotation.NonNull;

import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.data.interactor.Interactors;
import com.epam.traing.gitcl.db.dao.IRepoDao;
import com.epam.traing.gitcl.db.model.RepoModel;
import com.epam.traing.gitcl.network.GitHubRepoClient;
import com.epam.traing.gitcl.network.json.RepoJson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by Yahor_Fralou on 2/22/2017 12:31 PM.
 */

public class RepositoriesInteractor implements IRepositoriesInteractor {

    private GitHubRepoClient repoClient;
    private IRepoDao repoDao;
    private ModelConverter modelConverter;

    public RepositoriesInteractor(GitHubRepoClient repoClient, IRepoDao repoDao, ModelConverter modelConverter) {
        this.repoClient = repoClient;
        this.repoDao = repoDao;
        this.modelConverter = modelConverter;
    }

    @Override
    public Observable<List<RepoModel>> getRepositories() {
        return repoDao.getRepositories()
                .compose(Interactors.applySchedulersIO());
    }

    @Override
    public Observable<List<RepoModel>> loadRepositories() {
        // TODO implement pagination ?
        return repoClient.getAccountRepositories()
                .map(this::convertToModels)
                .doOnNext(repoDao::saveAll)
                .compose(Interactors.applySchedulersIO());
    }

    @Override
    public Observable<RepoModel> getRepoInfo(long id) {
        return repoDao.getById(id)
                .compose(Interactors.applySchedulersIO());
    }

    @Override
    public Observable<RepoModel> loadVerbose(RepoModel repoModel) {
        return repoClient.loadRepoInfo(repoModel.getOwnerName(), repoModel.getName())
                .map(modelConverter::toRepoModel)
                .flatMap(this::loadContributorsInfo)
                .doOnNext(rm -> rm.setVerboseUpdateDate(new Date().getTime()))
                .doOnNext(repoDao::save)
                .flatMap(rm -> repoDao.getById(rm.getId()))
                .compose(Interactors.applySchedulersIO());
    }

    @NonNull
    private Observable<RepoModel> loadContributorsInfo(RepoModel repoModel) {
        return repoClient.getContributors(repoModel.getOwnerName(), repoModel.getName())
                .doOnNext(accountModels -> repoModel.setContributorsCount(accountModels.size()))
                .map(accountModels -> repoModel);
    }

    private List<RepoModel> convertToModels(List<RepoJson> repoJsonList) {
        List<RepoModel> models = new ArrayList<>();
        for (RepoJson json : repoJsonList) {
            models.add(modelConverter.toRepoModel(json));
        }

        return models;
    }
}
