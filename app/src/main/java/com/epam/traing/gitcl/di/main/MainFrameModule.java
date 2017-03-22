package com.epam.traing.gitcl.di.main;

import com.epam.traing.gitcl.data.converter.ModelConverter;
import com.epam.traing.gitcl.data.interactor.account.AccountInteractor;
import com.epam.traing.gitcl.data.interactor.account.IAccountInteractor;
import com.epam.traing.gitcl.data.interactor.search.ISearchIntercator;
import com.epam.traing.gitcl.data.interactor.search.SearchInteractor;
import com.epam.traing.gitcl.db.dao.HistoryDao;
import com.epam.traing.gitcl.db.dao.IAccountDao;
import com.epam.traing.gitcl.db.dao.IHistoryDao;
import com.epam.traing.gitcl.db.dao.IRepoDao;
import com.epam.traing.gitcl.helper.PrefHelper;
import com.epam.traing.gitcl.helper.SessionHelper;
import com.epam.traing.gitcl.network.GitHubUserClient;
import com.epam.traing.gitcl.presentation.presenter.AccountPresenter;
import com.epam.traing.gitcl.presentation.presenter.IAccountPresenter;
import com.epam.traing.gitcl.presentation.presenter.IMainPresenter;
import com.epam.traing.gitcl.presentation.presenter.MainPresenter;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yahor_Fralou on 2/16/2017 6:26 PM.
 */

@Module
public class MainFrameModule {
    @Provides
    @MainFrameScope
    IMainPresenter provideMainPresenter(IAccountInteractor accountInteractor, PrefHelper prefHelper, ISearchIntercator searchIntercator) {
        return new MainPresenter(accountInteractor, prefHelper, searchIntercator);
    }

    @Provides
    @MainFrameScope
    IAccountPresenter provideAccountPresenter() {
        return new AccountPresenter();
    }

    @Provides
    @MainFrameScope
    public IAccountInteractor provideAccountInteractor(GitHubUserClient userClient,
                                                       IAccountDao accountDao,
                                                       SessionHelper sessionHelper,
                                                       PrefHelper prefHelper,
                                                       ModelConverter modelConverter) {
        return new AccountInteractor(userClient, accountDao, sessionHelper, prefHelper, modelConverter);
    }

    @Provides
    @MainFrameScope
    public ISearchIntercator provideSearchIntercator(IHistoryDao historyDao, IRepoDao repoDao, IAccountDao accountDao) {
        return new SearchInteractor(historyDao, repoDao, accountDao);
    }

    @Provides
    @MainFrameScope
    public IHistoryDao provideHistoryDao(StorIOSQLite storIOSQLite) {
        return new HistoryDao(storIOSQLite);
    }
}
