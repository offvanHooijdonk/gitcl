package com.epam.traing.gitcl.presentation.presenter;

import com.epam.traing.gitcl.presentation.ui.IAccountView;

/**
 * Created by Yahor_Fralou on 3/7/2017 4:34 PM.
 */

public class AccountPresenter extends AbstractSubscribePresenter implements IAccountPresenter {

    private IAccountView view;

    @Override
    public void attachView(IAccountView accountView) {
        this.view = accountView;
    }

    @Override
    public void detachView() {
        unsubscribeAll();

        view = null;
    }
}
