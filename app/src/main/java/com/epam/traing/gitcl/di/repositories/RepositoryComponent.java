package com.epam.traing.gitcl.di.repositories;

import com.epam.traing.gitcl.presentation.ui.RepoListFragment;
import com.epam.traing.gitcl.presentation.ui.RepoInfoFragment;

import dagger.Subcomponent;

/**
 * Created by Yahor_Fralou on 2/22/2017 12:06 PM.
 */

@Subcomponent(modules = {RepositoryModule.class})
@RepositoryScope
public interface RepositoryComponent {
    void inject(RepoListFragment repoListFragment);
    void inject(RepoInfoFragment repoInfoFragment);
}
