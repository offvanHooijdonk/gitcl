package com.epam.traing.gitcl.presentation.ui;

import com.epam.traing.gitcl.model.AccountModel;
import com.epam.traing.gitcl.model.RepoModel;

/**
 * Created by Yahor_Fralou on 2/27/2017 7:14 PM.
 */

public interface IRepoInfoView {
    void updateOwnerInfo(AccountModel accountModel);

    void displayRepoInfo(RepoModel repoModel);

    void showRefreshingProcess(boolean show);
}
