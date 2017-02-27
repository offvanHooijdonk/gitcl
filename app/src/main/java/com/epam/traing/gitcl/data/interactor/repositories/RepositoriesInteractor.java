package com.epam.traing.gitcl.data.interactor.repositories;

import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.db.dao.IRepoDao;
import com.epam.traing.gitcl.db.model.RepoModel;
import com.epam.traing.gitcl.network.GitHubRepoClient;
import com.epam.traing.gitcl.network.json.RepoJson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Yahor_Fralou on 2/22/2017 12:31 PM.
 */

public class RepositoriesInteractor implements IRepositoriesInteractor {

    private GitHubRepoClient repoClient;
    private IRepoDao repoDao;
    private ModelConverter modelConverter;

    @Inject
    public RepositoriesInteractor(GitHubRepoClient repoClient, IRepoDao repoDao, ModelConverter modelConverter) {
        this.repoClient = repoClient;
        this.repoDao = repoDao;
        this.modelConverter = modelConverter;
    }

    @Override
    public Observable<List<RepoModel>> getRepositories() {
        return repoDao.getRepositories();
    }

    @Override
    public Observable<List<RepoModel>> loadRepositories() {
        // TODO implement pagination ?
        return repoClient.getAccountRepositories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::convertToModels)
                .doOnNext(repoDao::saveAll);
    }

    private List<RepoModel> convertToModels(List<RepoJson> repoJsonList) {
        List<RepoModel> models = new ArrayList<>();
        for (RepoJson json : repoJsonList) {
            models.add(modelConverter.toRepoModel(json));
        }

        return models;
    }
}
