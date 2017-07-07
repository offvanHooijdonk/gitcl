package com.epam.traing.gitcl.presentation.presenter;

import com.epam.traing.gitcl.model.RepoModel;
import com.epam.traing.gitcl.presentation.ui.IRepoInfoView;

/**
 * Created by Yahor_Fralou on 2/27/2017 7:16 PM.
 */

public interface IRepoInfoPresenter {
    void attachView(IRepoInfoView repoInfoView);

    void onViewCreated(RepoModel repoModel);

    void onRefreshTriggered();

    void detachView();
}
