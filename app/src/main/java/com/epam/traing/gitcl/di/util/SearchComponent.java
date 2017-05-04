package com.epam.traing.gitcl.di.util;

import com.epam.traing.gitcl.di.presentation.search.SearchScreenComponent;
import com.epam.traing.gitcl.di.presentation.search.SearchScreenModule;

import dagger.Subcomponent;

/**
 * Created by Yahor_Fralou on 5/3/2017 6:01 PM.
 */

@Subcomponent(modules = {AccountModule.class, RepoModule.class, HistoryModule.class, SearchInteractorModule.class})
@UtilsScope
public interface SearchComponent {

    SearchScreenComponent plusSearchScreenComponent(SearchScreenModule searchScreenModule);
}
