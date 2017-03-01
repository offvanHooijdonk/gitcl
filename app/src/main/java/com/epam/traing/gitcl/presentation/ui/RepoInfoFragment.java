package com.epam.traing.gitcl.presentation.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.app.Application;
import com.epam.traing.gitcl.db.model.RepoModel;
import com.epam.traing.gitcl.helper.SessionHelper;
import com.epam.traing.gitcl.presentation.ui.animation.InfoTransition;
import com.epam.traing.gitcl.presentation.ui.view.RepoIconView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.epam.traing.gitcl.presentation.ui.view.RepoIconView.TYPE_FORK;
import static com.epam.traing.gitcl.presentation.ui.view.RepoIconView.TYPE_REPO;

/**
 * Created by Yahor_Fralou on 2/27/2017 7:15 PM.
 */

public class RepoInfoFragment extends Fragment implements IRepoInfoView {

    @Inject
    IRepoInfoPresenter presenter;
    @Inject
    SessionHelper session;

    private RepoModel repoModel;

    @Bind(R.id.txtRepoName)
    TextView txtRepoName;
    @Bind(R.id.repoIcon)
    RepoIconView repoIcon;

    public static RepoInfoFragment getInstance(RepoModel repoModel) {
        RepoInfoFragment fragment = new RepoInfoFragment();
        fragment.setRepoModel(repoModel);

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

        getActivity().setTitle(String.format("%s/%s", repoModel.getOwnerName(), repoModel.getName()));

        return v;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        txtRepoName.setText(repoModel.getName());
        repoIcon.setPrivateRepo(repoModel.isPrivateRepo());
        repoIcon.setRepoType(repoModel.isFork() ? TYPE_FORK : TYPE_REPO);
        repoIcon.setIsOwn(repoModel.getOwnerName().equalsIgnoreCase(session.getCurrentAccount().getAccountName()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            searchItem.setVisible(false);
        }
    }

    private void setRepoModel(RepoModel repoModel) {
        this.repoModel = repoModel;
    }

    private void injectComponent() {
        Application.getRepositoryComponent().inject(this);
        presenter.attachView(this);
    }
}
