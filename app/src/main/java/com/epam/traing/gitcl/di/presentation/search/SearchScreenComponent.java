package com.epam.traing.gitcl.di.presentation.search;

import com.epam.traing.gitcl.presentation.ui.view.search.SearchDialogFragment;

import dagger.Subcomponent;

/**
 * Created by Yahor_Fralou on 5/4/2017 7:09 PM.
 */

@Subcomponent(modules = SearchScreenModule.class)
@SearchScreenScope
public interface SearchScreenComponent {

    void inject(SearchDialogFragment searchDialogFragment);
}
