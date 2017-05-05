package com.epam.traing.gitcl.presentation.presenter;

import com.epam.traing.gitcl.presentation.ui.IAccountView;

/**
 * Created by Yahor_Fralou on 3/7/2017 4:33 PM.
 */

public interface IAccountPresenter {

    void attachView(IAccountView accountView);

    void detachView();
}
