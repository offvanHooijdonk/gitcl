package com.epam.traing.gitcl.presentation.ui;

import com.epam.traing.gitcl.presentation.ui.view.search.SearchListAdapter;

import java.util.List;

/**
 * Created by Yahor_Fralou on 2/7/2017 11:40 AM.
 */

public interface IMainView {
    void updateAccountInfo();

    void showLogoutDialog(boolean show);

    void startLoginActivity();

    void updateSearchResults(List<SearchListAdapter.ItemWrapper> historyModels);
}
