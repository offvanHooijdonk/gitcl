package com.epam.traing.gitcl.presentation.ui.view.search;

import java.util.List;

/**
 * Created by Yahor_Fralou on 5/4/2017 7:47 PM.
 */

public interface ISearchView {

    void updateSearchResults(List<SearchListAdapter.ItemWrapper> searchResults);

    void showError(Throwable th);
}
