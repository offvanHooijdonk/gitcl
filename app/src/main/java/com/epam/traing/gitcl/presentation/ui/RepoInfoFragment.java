package com.epam.traing.gitcl.presentation.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.app.Application;
import com.epam.traing.gitcl.db.model.RepoModel;
import com.epam.traing.gitcl.presentation.ui.animation.InfoTransition;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Yahor_Fralou on 2/27/2017 7:15 PM.
 */

public class RepoInfoFragment extends Fragment implements IRepoInfoView {

    @Inject
    IRepoInfoPresenter presenter;

    private RepoModel repoModel;
    private String transName;

    @Bind(R.id.txtRepoName)
    TextView txtRepoName;

    public static RepoInfoFragment getInstance(RepoModel repoModel, String transName) {
        RepoInfoFragment fragment = new RepoInfoFragment();
        fragment.setRepoModel(repoModel);
        fragment.setTransName(transName);

        fragment.setSharedElementEnterTransition(new InfoTransition());
        fragment.setSharedElementReturnTransition(new InfoTransition());
        fragment.setEnterTransition(new Fade());
        fragment.setExitTransition(new Fade());
        return fragment;
    }

    public RepoInfoFragment() {
        injectComponent();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if (v == null) {
            v = inflater.inflate(R.layout.frag_repo_info, container, false);
        }
        ButterKnife.bind(this, v);

        txtRepoName.setText(repoModel.getName());
        txtRepoName.setTransitionName(transName);

        return v;
    }

    private void setRepoModel(RepoModel repoModel) {
        this.repoModel = repoModel;
    }

    private void setTransName(String transName) {
        this.transName = transName;
    }

    private void injectComponent() {
        Application.getRepositoryComponent().inject(this);
        presenter.attachView(this);
    }
}
