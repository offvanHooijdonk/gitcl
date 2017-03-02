package com.epam.traing.gitcl.presentation.ui;

import com.epam.traing.gitcl.db.model.RepoModel;

/**
 * Created by Yahor_Fralou on 2/27/2017 7:16 PM.
 */

public interface IRepoInfoPresenter {
    void attachView(IRepoInfoView repoInfoView);

    void onViewCreated(RepoModel repoModel);
}
