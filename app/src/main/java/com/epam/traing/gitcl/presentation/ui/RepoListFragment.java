package com.epam.traing.gitcl.presentation.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.epam.traing.gitcl.helper.SessionHelper;
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

public class RepoListFragment extends Fragment implements IRepoListView, RepoListAdapter.RepoClickListener {

    @Inject
    IRepoListPresenter presenter;
    @Inject
    SessionHelper session;

    private Context ctx;

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

        setHasOptionsMenu(true);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ctx = getActivity();

        getActivity().setTitle(getString(R.string.title_repos));
        initRefreshLayout(refreshLayoutList);
        initRefreshLayout(refreshLayoutEmpty);
        refreshLayoutList.setVisibility(View.VISIBLE);
        refreshLayoutEmpty.setVisibility(View.GONE);

        lstRepos.setLayoutManager(new LinearLayoutManager(ctx));
        lstRepos.setHasFixedSize(true);
        repositories.clear();
        repoListAdapter = new RepoListAdapter(ctx, repositories, session.getCurrentAccount());
        repoListAdapter.setClickListener(this);
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
        fab.setOnClickListener(fabView -> Snackbar.make(fab, "No", Snackbar.LENGTH_SHORT).show());

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
            Log.i(Application.LOG, "Updating repos on UI");
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

    private void showListOrEmptyView(boolean showList) {
        if (showList) {
            refreshLayoutList.setVisibility(View.VISIBLE);
            refreshLayoutEmpty.setVisibility(View.GONE);
        } else {
            refreshLayoutList.setVisibility(View.GONE);
            refreshLayoutEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void initRefreshLayout(SwipeRefreshLayout srl) {
        // TODO move to some helper?
        srl.setColorSchemeColors(ctx.getResources().getColor(R.color.refresh1),
                ctx.getResources().getColor(R.color.refresh2),
                ctx.getResources().getColor(R.color.refresh3));
        srl.setOnRefreshListener(() -> presenter.onRefreshTriggered());
    }

    private void injectComponent() {
        Application.getRepositoryComponent().inject(this);
        presenter.attachView(this);
    }

}
