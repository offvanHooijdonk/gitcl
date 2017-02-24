package com.epam.traing.gitcl.presentation.ui;

import com.epam.traing.gitcl.db.model.RepoModel;

import java.util.List;

/**
 * Created by Yahor_Fralou on 2/22/2017 11:56 AM.
 */

public interface IRepoListView {
    void updateRepoList(List<RepoModel> repoModels);

    void showRefreshProgress(boolean show);

    void displayError(Throwable th);
}
