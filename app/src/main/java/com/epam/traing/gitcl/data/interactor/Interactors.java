package com.epam.traing.gitcl.data.interactor;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Yahor_Fralou on 7/6/2017 4:23 PM.
 */

public class Interactors {
    private static Observable.Transformer<?, ?> transformer = observable -> observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());

    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> applySchedulersIO() {
        return (Observable.Transformer<T, T>) transformer;
    }
}
