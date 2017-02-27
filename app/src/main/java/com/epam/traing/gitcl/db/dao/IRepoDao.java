package com.epam.traing.gitcl.db.dao;

import com.epam.traing.gitcl.db.model.RepoModel;

import java.util.List;

import rx.Observable;

/**
 * Created by Yahor_Fralou on 2/24/2017 3:35 PM.
 */

public interface IRepoDao {
    Observable<List<RepoModel>> getRepositories();

    void saveAll(List<RepoModel> models);

    void save(RepoModel model);
}
