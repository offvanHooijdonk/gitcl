package com.epam.traing.gitcl.presentation.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.SharedElementCallback;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Inject
    IRepoInfoPresenter presenter;
    @Inject
    SessionHelper session;

    private RepoModel repoModel;
    private Context ctx;

    // TODO move to a helper
    private List<View> nonTransitionViews;
    private boolean isEnterAnimationEnded = false;
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
        RepoInfoFragment fragment = new RepoInfoFragment();
        fragment.setRepoModel(repoModel);

        fragment.setSharedElementEnterTransition(new InfoTransition());
        fragment.setSharedElementReturnTransition(new InfoTransition());
        fragment.setEnterTransition(new Fade());
        fragment.setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                if (!fragment.isEnterAnimationEnded) {
                    fragment.revealNonTransitionViews(true);
                    fragment.isEnterAnimationEnded = true;
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
        ButterKnife.bind(this, v);

        getActivity().setTitle(String.format("%s/%s", repoModel.getOwnerName(), repoModel.getName()));
        setHasOptionsMenu(true);

        ctx = getActivity();
        nonTransitionViews = Arrays.asList(txtLanguage, txtDefaultBranch, blockOwner, dividerMain, blockDates, blockBadges);

        return v;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        txtOwnerFullName.setText(null);
        displayRepoInfo();

        fabEditRepo.setOnClickListener(v1 -> Snackbar.make(fabEditRepo, "Not so fast", Snackbar.LENGTH_SHORT).show());

        srlRepoInfo.setColorSchemeColors(ctx.getResources().getColor(R.color.refresh1),
                ctx.getResources().getColor(R.color.refresh2),
                ctx.getResources().getColor(R.color.refresh3));
        srlRepoInfo.setOnRefreshListener(() -> presenter.onRefreshTriggered());

        presenter.onViewCreated(repoModel);
        revealNonTransitionViews(false);
    }

    @Override
    public void updateOwnerInfo(AccountModel accountModel) {
        if (accountModel.getPersonName() != null) {
            txtOwnerFullName.setText(accountModel.getPersonName());
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

    private void displayRepoInfo() {
        txtRepoName.setText(repoModel.getName());
        repoIcon.setPrivateRepo(repoModel.isPrivateRepo());
        repoIcon.setRepoType(repoModel.isFork() ? TYPE_FORK : TYPE_REPO);
        repoIcon.setIsOwn(repoModel.getOwnerName().equalsIgnoreCase(session.getCurrentAccount().getAccountName()));
        txtOwnerAccount.setText(repoModel.getOwnerName());
        if (repoModel.getLanguage() != null) {
            txtLanguage.setText(repoModel.getLanguage());
        } else {
            txtLanguage.setVisibility(View.GONE);
        }
        if (repoModel.getDefaultBranch() != null) {
            txtDefaultBranch.setText(repoModel.getDefaultBranch());
        } else {
            txtDefaultBranch.setVisibility(View.GONE);
        }

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

    private void setRepoModel(RepoModel repoModel) {
        this.repoModel = repoModel;
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

    private void injectComponent() {
        Application.getRepositoryComponent().inject(this);
        presenter.attachView(this);
    }

}
