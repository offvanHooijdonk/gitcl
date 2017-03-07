package com.epam.traing.gitcl.presentation.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.app.Application;
import com.epam.traing.gitcl.presentation.presenter.IAccountPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by Yahor_Fralou on 3/7/2017 3:32 PM.
 */

public class AccountActivity extends AppCompatActivity implements IAccountView {

    @Inject
    IAccountPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Application.getMainFrameComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);
    }
}
