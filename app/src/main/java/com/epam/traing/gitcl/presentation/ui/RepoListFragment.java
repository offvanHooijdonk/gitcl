package com.epam.traing.gitcl.presentation.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.app.Application;
import com.epam.traing.gitcl.db.model.RepoModel;
import com.epam.traing.gitcl.presentation.presenter.IRepoListPresenter;
import com.epam.traing.gitcl.presentation.ui.adapter.RepoListAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Yahor_Fralou on 2/21/2017 5:40 PM.
 */

public class RepoListFragment extends Fragment implements IRepoListView {

    @Inject
    IRepoListPresenter presenter;

    @Bind(R.id.lstRepos)
    RecyclerView lstRepos;
    @Bind(R.id.emptyListPlaceholder)
    View viewEmptyList;

    private List<RepoModel> repositories = new ArrayList<>();
    private RepoListAdapter repoListAdapter;

    public RepoListFragment() {
        injectComponent();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if (v == null) {
            v = inflater.inflate(R.layout.frag_repo_list, container, false);
        }
        ButterKnife.bind(v);

        getActivity().setTitle(getString(R.string.title_repos));
        lstRepos.setVisibility(View.VISIBLE);
        viewEmptyList.setVisibility(View.GONE);

        repoListAdapter = new RepoListAdapter();

        return v;
    }

    private void injectComponent() {
        Application.getRepositoryComponent().inject(this);
    }
}
