package com.epam.traing.gitcl.presentation.presenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Yahor_Fralou on 5/5/2017 4:04 PM.
 */

public abstract class AbstractSubscribePresenter {
    private CompositeSubscription compositeSubscription;

    /**
     * Designed way to add a subscription to a set of current subscriptions,
     * so then can be unsubscribed from. Override to add your custom logic on subscription collect
     * @param subscription subscription that needs to be unsubscribed from when presenter exits
     */
    protected void collectSubscription(Subscription subscription) {
        getCompositeSubscription().add(subscription);
    }

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
