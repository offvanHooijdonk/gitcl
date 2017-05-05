package com.epam.traing.gitcl.presentation.presenter;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Yahor_Fralou on 5/5/2017 4:04 PM.
 */

public abstract class AbstractSubscribePresenter {
    private CompositeSubscription compositeSubscription;

    protected CompositeSubscription getCompositeSubscription() {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }

        return compositeSubscription;
    }

    protected void unsubscribeAll() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
            compositeSubscription = null;
        }
    }
}
