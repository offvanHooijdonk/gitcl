package com.epam.traing.gitcl.presentation.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.app.Application;
import com.epam.traing.gitcl.db.model.RepoModel;
import com.epam.traing.gitcl.di.DependencyManager;
import com.epam.traing.gitcl.helper.SessionHelper;
import com.epam.traing.gitcl.presentation.presenter.IRepoListPresenter;
import com.epam.traing.gitcl.presentation.ui.adapter.RepoListAdapter;
import com.epam.traing.gitcl.presentation.ui.helper.AnimationHelper;
import com.epam.traing.gitcl.presentation.ui.helper.ColorsHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Yahor_Fralou on 2/21/2017 5:40 PM.
 */

public class RepoListFragment extends Fragment implements IRepoListView, RepoListAdapter.RepoClickListener {

    @Inject
    IRepoListPresenter presenter;
    @Inject
    SessionHelper session;

    private Context ctx;
    private LinearLayoutManager layoutManager;
    private int recentConfiguration;

    @Bind(R.id.lstRepos)
    RecyclerView lstRepos;
    @Bind(R.id.emptyListPlaceholder)
    View viewEmptyList;
    @Bind(R.id.srlList)
    SwipeRefreshLayout refreshLayoutList;
    @Bind(R.id.srlEmpty)
    SwipeRefreshLayout refreshLayoutEmpty;
    @Bind(R.id.fabCreateRepo)
    FloatingActionButton fab;
    private View viewRevealPlaceholder;

    private List<RepoModel> repositories = new ArrayList<>();
    private RepoListAdapter repoListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if (v == null) {
            v = inflater.inflate(R.layout.frag_repos_list, container, false);
        }
        ctx = getActivity();
        injectComponent();

        ButterKnife.bind(this, v);

        recentConfiguration = ctx.getResources().getConfiguration().orientation;

        setHasOptionsMenu(true);
        viewRevealPlaceholder = getActivity().findViewById(R.id.revealPlaceholder);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.title_repos));
        ColorsHelper.initRefreshLayout(ctx, refreshLayoutList);
        ColorsHelper.initRefreshLayout(ctx, refreshLayoutEmpty);
        refreshLayoutList.setOnRefreshListener(() -> presenter.onRefreshTriggered());
        refreshLayoutEmpty.setOnRefreshListener(() -> presenter.onRefreshTriggered());

        refreshLayoutList.setVisibility(View.VISIBLE);
        refreshLayoutEmpty.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(ctx);
        lstRepos.setLayoutManager(layoutManager);
        lstRepos.setHasFixedSize(true);
        repositories.clear();
        repoListAdapter = new RepoListAdapter(ctx, repositories, session.getCurrentAccount());
        repoListAdapter.setClickListener(this);
        repoListAdapter.setOrientation(ctx.getResources().getConfiguration().orientation);
        lstRepos.setAdapter(repoListAdapter);
        lstRepos.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });
        fab.setOnClickListener(fabView -> {
            if (viewRevealPlaceholder == null) {
                startActivity(new Intent(ctx, CreateRepoActivity.class));
                return;
            }
            AnimationHelper.Circle.revealViewWithFAB(viewRevealPlaceholder, fab, null, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    Intent intent = new Intent(ctx, CreateRepoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    new Handler().postDelayed(() -> {
                        viewRevealPlaceholder.setVisibility(View.INVISIBLE);
                        fab.show();
                    }, 350);
                }
            });
        });

        presenter.onViewShows();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            getActivity().setTitle(getString(R.string.title_repos));
        }
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
        if (show) {
            if (!refreshLayoutList.isRefreshing()) {
                refreshLayoutList.setRefreshing(true);
            }
            if (!refreshLayoutEmpty.isRefreshing()) {
                refreshLayoutEmpty.setRefreshing(true);
            }
        } else {
            if (refreshLayoutList.isRefreshing()) {
                refreshLayoutList.setRefreshing(false);
            }
            if (refreshLayoutEmpty.isRefreshing()) {
                refreshLayoutEmpty.setRefreshing(false);
            }
        }
    }

    @Override
    public void displayError(Throwable th) {
        Log.e(Application.LOG, "Error on repos", th);
        Toast.makeText(ctx, th.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRepoClick(RepoListAdapter.RepoViewHolder holder, int position) {
        if (position < repositories.size()) {
            //setEnterTransition(new Fade());
            setExitTransition(new Fade());
            getActivity().getFragmentManager()
                    .beginTransaction()
                    .addSharedElement(holder.txtRepoName, ctx.getString(R.string.transit_repo_name))
                    .addSharedElement(holder.repoIcon, ctx.getString(R.string.transit_repo_icon))
                    .add(R.id.content_main, RepoInfoFragment.getInstance(repositories.get(position)))
                    .hide(this)
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(ctx, R.string.err_position_out_of_bounds, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.repo_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_refresh) {
            presenter.onRefreshTriggered();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (recentConfiguration != newConfig.orientation) {
            adaptToOrientation(newConfig.orientation);

            recentConfiguration = newConfig.orientation;
        }
    }

    private void adaptToOrientation(int orientation) {
        repoListAdapter.setOrientation(orientation);
    }

    private void showListOrEmptyView(boolean showList) {
        if (showList) {
            refreshLayoutList.setVisibility(View.VISIBLE);
            refreshLayoutEmpty.setVisibility(View.GONE);
        } else {
            refreshLayoutList.setVisibility(View.GONE);
            refreshLayoutEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void injectComponent() {
        DependencyManager.getRepositoryComponent(ctx).inject(this);
        presenter.attachView(this);
    }

}
