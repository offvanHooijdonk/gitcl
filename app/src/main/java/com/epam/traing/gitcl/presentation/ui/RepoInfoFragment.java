package com.epam.traing.gitcl.presentation.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.app.Application;

import javax.inject.Inject;

/**
 * Created by Yahor_Fralou on 2/27/2017 7:15 PM.
 */

public class RepoInfoFragment extends Fragment implements IRepoInfoView {

    @Inject
    IRepoInfoPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if (v == null) {
            v = inflater.inflate(R.layout.frag_repo_info, container, false);
        }

        injectComponent();

        return v;
    }

    private void injectComponent() {
        Application.getRepositoryComponent().inject(this);
        presenter.attachView(this);
    }
}
