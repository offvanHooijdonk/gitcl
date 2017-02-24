package com.epam.traing.gitcl.data.interactor.repositories;

import com.epam.traing.gitcl.db.model.RepoModel;

import java.util.List;

import rx.Observable;

/**
 * Created by Yahor_Fralou on 2/22/2017 12:32 PM.
 */

public interface IRepositoriesInteractor {
    Observable<List<RepoModel>> getRepositories();

    Observable<List<RepoModel>> subscribeReposChanges();

    Observable<List<RepoModel>> loadRepositories();
}
