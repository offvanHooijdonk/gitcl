package com.epam.traing.gitcl.di.util;

import com.epam.traing.gitcl.di.presentation.main.MainScreenComponent;

import dagger.Subcomponent;

/**
 * Created by Yahor_Fralou on 5/3/2017 5:59 PM.
 */

@Subcomponent(modules = {AccountModule.class, AccountInteractorModule.class})
@UtilsScope
public interface AccountComponent {

    MainScreenComponent plusMainScreenComponent();
}
