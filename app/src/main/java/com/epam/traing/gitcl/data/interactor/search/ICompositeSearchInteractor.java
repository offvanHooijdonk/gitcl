package com.epam.traing.gitcl.data.interactor.search;

import com.epam.traing.gitcl.model.search.SearchResultItem;

import java.util.List;

import rx.Observable;

/**
 * Created by Yahor_Fralou on 7/10/2017 10:54 AM.
 */

public interface ICompositeSearchInteractor {
    Observable<List<SearchResultItem>> searchLive(String text, int maxHistory);

    Observable<List<SearchResultItem>> searchRemote(String text);
}
