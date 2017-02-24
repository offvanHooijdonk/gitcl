package com.epam.traing.gitcl.presentation.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.app.Application;
import com.epam.traing.gitcl.db.model.RepoModel;
import com.epam.traing.gitcl.helper.SessionHelper;
import com.epam.traing.gitcl.presentation.presenter.IRepoListPresenter;
import com.epam.traing.gitcl.presentation.ui.adapter.RepoListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
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
    @Inject
    SessionHelper session;

    private Context ctx;

    @Bind(R.id.lstRepos)
    RecyclerView lstRepos;
    @Bind(R.id.emptyListPlaceholder)
    View viewEmptyList;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;

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
        ButterKnife.bind(this, v);
        ctx = getActivity();

        getActivity().setTitle(getString(R.string.title_repos));
        lstRepos.setVisibility(View.VISIBLE);
        viewEmptyList.setVisibility(View.GONE);
        refreshLayout.setColorSchemeColors(ctx.getResources().getColor(R.color.refresh1),
                ctx.getResources().getColor(R.color.refresh2),
                ctx.getResources().getColor(R.color.refresh3));
        refreshLayout.setOnRefreshListener(() -> presenter.onRefreshTriggered());

        lstRepos.setLayoutManager(new LinearLayoutManager(ctx));
        lstRepos.setHasFixedSize(true);
        repositories.clear();
        repositories.addAll(getSampleList());
        repoListAdapter = new RepoListAdapter(ctx, repositories, session.getCurrentAccount());
        lstRepos.setAdapter(repoListAdapter);

        presenter.onViewCreate();
        // TODO refactor colors names!

        return v;
    }

    @Override
    public void updateRepoList(List<RepoModel> repoModels) {
        showListOrEmptyView(!repoModels.isEmpty());
        if (!repoModels.isEmpty()) {
            this.repositories.clear();
            this.repositories.addAll(repoModels);
            repoListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showRefreshProgress(boolean show) {
        refreshLayout.setRefreshing(show);
    }

    @Override
    public void displayError(Throwable th) {
        Toast.makeText(ctx, th.toString(), Toast.LENGTH_LONG).show();
    }

    private void showListOrEmptyView(boolean showList) {
        if (showList) {
            lstRepos.setVisibility(View.VISIBLE);
            viewEmptyList.setVisibility(View.GONE);
        } else {
            lstRepos.setVisibility(View.GONE);
            viewEmptyList.setVisibility(View.VISIBLE);
        }
    }

    private void injectComponent() {
        Application.getRepositoryComponent().inject(this);
        presenter.attachView(this);
    }

    private List<RepoModel> getSampleList() {
        RepoModel model1 = new RepoModel();
        model1.setId(22);
        model1.setName("Samplereposit");
        model1.setOwnerName("johndoe");
        model1.setPrivateRepo(true);
        model1.setFork(true);
        model1.setStargazersCount(2930);
        model1.setWatchersCount(627);
        model1.setForksCount(46);
        model1.setLanguage("Java");

        RepoModel model2 = new RepoModel();
        model2.setId(245);
        model2.setName("Newfreshrepo");
        model2.setOwnerName("offvanhooijdonk");
        model2.setPrivateRepo(false);
        model2.setFork(false);
        model2.setStargazersCount(185);
        model2.setWatchersCount(41095);
        model2.setForksCount(53);
        model2.setLanguage("PHP");

        return Arrays.asList(model1, model2);
    }
}
