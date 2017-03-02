package com.epam.traing.gitcl.di.main;

import com.epam.traing.gitcl.di.repositories.RepositoryComponent;
import com.epam.traing.gitcl.di.repositories.RepositoryModule;
import com.epam.traing.gitcl.presentation.ui.MainActivity;

import dagger.Subcomponent;

/**
 * Created by Yahor_Fralou on 2/16/2017 6:32 PM.
 */

@Subcomponent(modules = {
        MainFrameModule.class
})
@MainFrameScope
public interface MainFrameComponent {
    void inject(MainActivity mainActivity);

    RepositoryComponent plusRepositoryComponent(RepositoryModule repositoryModule);
}
