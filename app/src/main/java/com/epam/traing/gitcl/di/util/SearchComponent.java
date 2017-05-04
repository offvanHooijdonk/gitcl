package com.epam.traing.gitcl.di.util;

import dagger.Subcomponent;

/**
 * Created by Yahor_Fralou on 5/3/2017 6:01 PM.
 */

@Subcomponent(modules = {AccountModule.class, RepoModule.class, HistoryModule.class})
@UtilsScope
public interface SearchComponent {
}
