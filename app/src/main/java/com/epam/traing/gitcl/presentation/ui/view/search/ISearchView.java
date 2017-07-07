package com.epam.traing.gitcl.presentation.ui.view.search;

import com.epam.traing.gitcl.db.model.search.SearchResultItem;

import java.util.List;

/**
 * Created by Yahor_Fralou on 5/4/2017 7:47 PM.
 */

public interface ISearchView {

    void updateSearchResults(List<SearchResultItem> searchResults);

    void showError(Throwable th);
}
