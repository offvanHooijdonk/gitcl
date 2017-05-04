package com.epam.traing.gitcl.di.presentation.main;

import com.epam.traing.gitcl.presentation.ui.AccountActivity;
import com.epam.traing.gitcl.presentation.ui.MainActivity;

import dagger.Subcomponent;

/**
 * Created by Yahor_Fralou on 2/16/2017 6:32 PM.
 */

@Subcomponent(modules = {
        MainScreenModule.class
})
@MainScreenScope
public interface MainScreenComponent {
    void inject(MainActivity mainActivity);
    void inject(AccountActivity accountActivity);
}
