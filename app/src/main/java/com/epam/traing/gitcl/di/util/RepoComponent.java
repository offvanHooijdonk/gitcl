package com.epam.traing.gitcl.di.util;

import com.epam.traing.gitcl.di.presentation.repositories.RepoScreenComponent;
import com.epam.traing.gitcl.di.presentation.repositories.RepoScreenModule;

import dagger.Subcomponent;

/**
 * Created by Yahor_Fralou on 5/3/2017 6:00 PM.
 */

@Subcomponent(modules = {RepoModule.class, RepoInteractorModule.class, AccountModule.class, AccountInteractorModule.class})
@UtilsScope
public interface RepoComponent {
    //IRepositoriesInteractor getRepositoriesInteractor();

    RepoScreenComponent plusRepoScreenComponent(RepoScreenModule repoScreenModule);
}
