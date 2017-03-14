package com.epam.traing.gitcl.presentation.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.app.Application;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.model.RepoModel;
import com.epam.traing.gitcl.helper.SessionHelper;
import com.epam.traing.gitcl.presentation.ui.animation.InfoTransition;
import com.epam.traing.gitcl.presentation.ui.helper.DateHelper;
import com.epam.traing.gitcl.presentation.ui.view.BadgeNumbersView;
import com.epam.traing.gitcl.presentation.ui.view.RepoIconView;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.ALPHA;
import static com.epam.traing.gitcl.presentation.ui.view.RepoIconView.TYPE_FORK;
import static com.epam.traing.gitcl.presentation.ui.view.RepoIconView.TYPE_REPO;

/**
 * Created by Yahor_Fralou on 2/27/2017 7:15 PM.
 */

public class RepoInfoFragment extends Fragment implements IRepoInfoView {
    private static final String ARG_REPO_MODEL = "ARG_REPO_MODEL";

    @Inject
    IRepoInfoPresenter presenter;
    @Inject
    SessionHelper session;

    private RepoModel repoModel;
    private AccountModel accountModel;
    private Context ctx;
    private int recentOrientation;
    private boolean layoutRevealRequired = false;

    // TODO move to a helper
    private List<View> nonTransitionViews;
    private DateHelper dateHelper = new DateHelper();

    @Bind(R.id.txtRepoName)
    TextView txtRepoName;
    @Bind(R.id.repoIcon)
    RepoIconView repoIcon;
    @Bind(R.id.blockOwner)
    View blockOwner;
    @Bind(R.id.imgOwnerPhoto)
    ImageView imgOwnerPhoto;
    @Bind(R.id.txtOwnerAccount)
    TextView txtOwnerAccount;
    @Bind(R.id.txtOwnerFullName)
    TextView txtOwnerFullName;
    @Bind(R.id.txtLanguage)
    TextView txtLanguage;
    @Bind(R.id.txtDefaultBranch)
    TextView txtDefaultBranch;
    @Bind(R.id.dividerMain)
    View dividerMain;
    @Bind(R.id.blockDates)
    View blockDates;
    @Bind(R.id.blockBadges)
    View blockBadges;
    @Bind(R.id.txtCreateTime)
    TextView txtCreateTime;
    @Bind(R.id.txtUpdateTime)
    TextView txtUpdateTime;
    @Bind(R.id.txtPushTime)
    TextView txtPushTime;
    @Bind(R.id.fabEditRepo)
    FloatingActionButton fabEditRepo;
    @Bind(R.id.badgeStars)
    BadgeNumbersView badgeStars;
    @Bind(R.id.badgeWatchers)
    BadgeNumbersView badgeWatchers;
    @Bind(R.id.badgeContributors)
    BadgeNumbersView badgeContributors;
    @Bind(R.id.badgeForks)
    BadgeNumbersView badgeForks;
    @Bind(R.id.srlRepoInfo)
    SwipeRefreshLayout srlRepoInfo;

    public static RepoInfoFragment getInstance(RepoModel repoModel) {
        Log.i(Application.LOG, "Constructor");
        RepoInfoFragment fragment = new RepoInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_REPO_MODEL, repoModel);
        fragment.setArguments(args);

        fragment.layoutRevealRequired = true;
        fragment.setSharedElementEnterTransition(new InfoTransition());
        fragment.setSharedElementReturnTransition(new InfoTransition());
        fragment.setEnterTransition(new Fade());
        fragment.setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                if (fragment.layoutRevealRequired) {
                    fragment.revealNonTransitionViews(true);
                    fragment.layoutRevealRequired = false;
                } else {
                    fragment.revealNonTransitionViews(false);
                }
            }
        });

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

        Log.i(Application.LOG, "onCreateView");
        ButterKnife.bind(this, v);
        ctx = getActivity();
        recentOrientation = ctx.getResources().getConfiguration().orientation;

        repoModel = getArguments().getParcelable(ARG_REPO_MODEL);
        if (repoModel == null) {
            Toast.makeText(ctx, "Repository not selected!", Toast.LENGTH_LONG).show();
            getFragmentManager().popBackStack();
        }

        getActivity().setTitle(String.format("%s/%s", repoModel.getOwnerName(), repoModel.getName()));
        setHasOptionsMenu(true);

        nonTransitionViews = Arrays.asList(txtLanguage, txtDefaultBranch, blockOwner, dividerMain, blockDates, blockBadges);

        srlRepoInfo.setColorSchemeColors(ctx.getResources().getColor(R.color.refresh1),
                ctx.getResources().getColor(R.color.refresh2),
                ctx.getResources().getColor(R.color.refresh3));
        srlRepoInfo.setOnRefreshListener(() -> presenter.onRefreshTriggered());

        blockOwner.setOnClickListener(v1 -> {
            Intent intent = new Intent(ctx, AccountActivity.class);
            intent.putExtra(AccountActivity.EXTRA_ACCOUNT, accountModel);
            ctx.startActivity(intent);
        });

        fabEditRepo.setOnClickListener(v1 -> Snackbar.make(fabEditRepo, "Not so fast", Snackbar.LENGTH_SHORT).show());

        return v;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        Log.i(Application.LOG, "onViewCreated");

        if (accountModel == null) {
            accountModel = new AccountModel();
            accountModel.setAccountName(repoModel.getOwnerName());
            txtOwnerFullName.setVisibility(View.GONE);
        } else {
            updateOwnerInfo(accountModel);
        }
        displayRepoInfoMain();

        presenter.onViewCreated(repoModel);
        if (layoutRevealRequired) {
            revealNonTransitionViews(true);
            layoutRevealRequired = false;
        } else {
            revealNonTransitionViews(false);
        }
    }

    @Override
    public void updateOwnerInfo(AccountModel accountModel) {
        this.accountModel = accountModel;
        if (accountModel.getPersonName() != null) {
            txtOwnerFullName.setText(accountModel.getPersonName());
            txtOwnerFullName.setVisibility(View.VISIBLE);
        } else{
            txtOwnerFullName.setVisibility(View.GONE);
        }
        if (accountModel.getAvatar() != null) {
            Glide.with(this).load(accountModel.getAvatar()).into(imgOwnerPhoto);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            searchItem.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayRepoInfo(RepoModel repoModel) {
        this.repoModel = repoModel;

        displayRepoInfo();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(Application.LOG, "onConfigurationChanged");
        if (recentOrientation != newConfig.orientation) {
            recentOrientation = newConfig.orientation;
            layoutRevealRequired = true;
            getFragmentManager().beginTransaction().detach(this).attach(this).commitAllowingStateLoss();
        }
    }

    private void displayRepoInfoMain() {
        txtRepoName.setText(repoModel.getName());
        repoIcon.setPrivateRepo(repoModel.isPrivateRepo());
        repoIcon.setRepoType(repoModel.isFork() ? TYPE_FORK : TYPE_REPO);
        repoIcon.setIsOwn(repoModel.getOwnerName().equalsIgnoreCase(session.getCurrentAccount().getAccountName()));
        txtOwnerAccount.setText(repoModel.getOwnerName());
        if (repoModel.getLanguage() != null) {
            txtLanguage.setText(repoModel.getLanguage());
            txtLanguage.setBackgroundTintList(ColorStateList.valueOf(getLangColor(repoModel.getLanguage())));
        } else {
            txtLanguage.setVisibility(View.GONE);
        }
        if (repoModel.getDefaultBranch() != null) {
            txtDefaultBranch.setText(repoModel.getDefaultBranch());
        } else {
            txtDefaultBranch.setVisibility(View.GONE);
        }
    }

    private void displayRepoInfo() {
        displayRepoInfoMain();

        setDateText(txtCreateTime, repoModel.getCreateDate());
        setDateText(txtUpdateTime, repoModel.getUpdateDate());
        setDateText(txtPushTime, repoModel.getPushDate());
        badgeStars.setNumberValue(repoModel.getStargazersCount());
        badgeWatchers.setNumberValue(repoModel.getWatchersCount());
        badgeForks.setNumberValue(repoModel.getForksCount());
        badgeContributors.setNumberValue(repoModel.getContributorsCount());
    }

    @Override
    public void showRefreshingProcess(boolean show) {
        srlRepoInfo.setRefreshing(show);
    }

    private void setDateText(TextView txt, long dateMillis) {
        if (dateMillis != 0) {
            txt.setText(dateHelper.formatDateTimeMedium(dateMillis));
        } else {
            txt.setText(R.string.repo_info_date_empty);
        }
    }

    private void revealNonTransitionViews(boolean show) {
        if (show) {
            if (!nonTransitionViews.isEmpty()) {
                AnimatorSet set = new AnimatorSet();
                AnimatorSet.Builder animBuilder = set.play(ObjectAnimator.ofFloat(nonTransitionViews.get(0), ALPHA, 1.0f));
                for (int i = 0; i < nonTransitionViews.size(); i++) {
                    animBuilder.with(ObjectAnimator.ofFloat(nonTransitionViews.get(i), ALPHA, 1.0f));
                }
                set.setStartDelay(InfoTransition.DURATION + 50);
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        fabEditRepo.show();
                    }
                });
                set.start();
            }
        } else {
            for (View v : nonTransitionViews) {
                v.setAlpha(0.0f);
            }
            fabEditRepo.setVisibility(View.INVISIBLE);
        }
    }

    // TODO move to helper
    private int getLangColor(@NonNull String lang) {
        int colorRes = R.color.label_lang_default;
        if (lang.equalsIgnoreCase("java")) {
            colorRes = R.color.label_java;
        } else if (lang.equalsIgnoreCase("typescript")) {
            colorRes = R.color.label_typescript;
        } else if (lang.equalsIgnoreCase("html")) {
            colorRes = R.color.label_html;
        }

        return getResources().getColor(colorRes);
    }

    private void injectComponent() {
        Application.getRepositoryComponent().inject(this);
        presenter.attachView(this);
    }

}
