package com.epam.traing.gitcl.di.presentation.search;

import com.epam.traing.gitcl.data.interactor.search.ICompositeSearchInteractor;
import com.epam.traing.gitcl.presentation.presenter.ISearchPresenter;
import com.epam.traing.gitcl.presentation.presenter.SearchPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 5/4/2017 7:09 PM.
 */

@Module
public class SearchScreenModule {

    @Provides
    @SearchScreenScope
    ISearchPresenter provideSearchPresenter(ICompositeSearchInteractor searchInteractor) {
        return new SearchPresenter(searchInteractor);
    }

}
