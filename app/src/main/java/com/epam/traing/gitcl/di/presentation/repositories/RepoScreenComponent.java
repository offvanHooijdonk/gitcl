package com.epam.traing.gitcl.di.presentation.repositories;

import com.epam.traing.gitcl.presentation.ui.RepoInfoFragment;
import com.epam.traing.gitcl.presentation.ui.RepoListFragment;

import dagger.Subcomponent;

/**
 * Created by Yahor_Fralou on 2/22/2017 12:06 PM.
 */

@Subcomponent(modules = {RepoScreenModule.class})
@RepoScreenScope
public interface RepoScreenComponent {
    void inject(RepoListFragment repoListFragment);
    void inject(RepoInfoFragment repoInfoFragment);
}
