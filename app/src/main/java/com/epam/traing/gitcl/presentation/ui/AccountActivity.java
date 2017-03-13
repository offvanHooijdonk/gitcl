package com.epam.traing.gitcl.presentation.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.app.Application;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.presentation.presenter.IAccountPresenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Yahor_Fralou on 3/7/2017 3:32 PM.
 */

public class AccountActivity extends AppCompatActivity implements IAccountView {
    public static final String EXTRA_ACCOUNT = "extra_account";

    private AccountModel account;

    @Inject
    IAccountPresenter presenter;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.imgAccountAvatar)
    ImageView imgAccountAvatar;
    @Bind(R.id.txtFullName)
    TextView txtFullName;
    @Bind(R.id.txtLocation)
    TextView txtLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        account = getIntent().getParcelableExtra(EXTRA_ACCOUNT);

        Application.getMainFrameComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24);
        collapsingToolbar.setTitle(account.getAccountName());
        if (account.getAvatar() != null) {
            Glide.with(this).load(account.getAvatar()).into(imgAccountAvatar);
        }

        displayAccountInfo();
    }

    private void displayAccountInfo() {
        txtFullName.setText(account.getPersonName() != null ? account.getPersonName() : account.getAccountName());
        if (account.getLocation() != null) {
            txtLocation.setText(account.getLocation());
        } else {
            txtLocation.setVisibility(View.GONE);
        }
    }
}