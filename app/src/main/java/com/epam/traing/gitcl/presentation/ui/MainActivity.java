package com.epam.traing.gitcl.presentation.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.di.DependencyManager;
import com.epam.traing.gitcl.helper.SessionHelper;
import com.epam.traing.gitcl.network.Constants;
import com.epam.traing.gitcl.presentation.presenter.IMainPresenter;
import com.epam.traing.gitcl.presentation.ui.view.search.SearchDialogFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements IMainView, NavigationView.OnNavigationItemSelectedListener {
    private static final String FRAG_REPO_LIST = "repo_list";
    private static final String FRAG_SEARCH_DIALOG = "search_dialog";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private TextView txtDrawerAccountUserName;
    private TextView txtDrawerAccountName;
    private ImageView imgAvatar;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private DrawerArrowDrawable arrowDrawable;
    private AlertDialog logoutDialog;

    private int prevBackStackCount = 0;

    @Inject
    IMainPresenter presenter;
    @Inject
    SessionHelper session;

    private ValueAnimator animToArrow;
    private ValueAnimator animFromArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DependencyManager.getMainScreenComponent().inject(this);
        presenter.attachView(this);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        arrowDrawable = toggle.getDrawerArrowDrawable();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        txtDrawerAccountUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtDrawerAccountUserName);
        txtDrawerAccountName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtDrawerAccountName);
        imgAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imgAccountAvatar);

        updateAccountInfo();

        getFragmentManager().addOnBackStackChangedListener(() -> {
            if (getFragmentManager().getBackStackEntryCount() == 0) {
                animateFromArrow();
            } else if (prevBackStackCount == 0) {
                animateToArrow();
            }
            prevBackStackCount = getFragmentManager().getBackStackEntryCount();
        });

        navigationView.setCheckedItem(R.id.nav_repo_list);
        loadFragment(new RepoListFragment(), FRAG_REPO_LIST);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, PreferenceActivity.class));
        } else if (id == R.id.action_search) {
            View itemView = findViewById(R.id.action_search);
            Log.i("LOG", String.valueOf(itemView != null));
            SearchDialogFragment searchFragment = SearchDialogFragment.newInstance(itemView, session.getCurrentAccount());

            searchFragment.show(getFragmentManager(), FRAG_SEARCH_DIALOG);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_repo_list) {
            loadFragment(new RepoListFragment(), FRAG_REPO_LIST);
        } else if (id == R.id.nav_starred) {

        } else if (id == R.id.nav_logout) {
            presenter.onLogoutSelected();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void updateAccountInfo() {
        AccountModel accountModel = session.getCurrentAccount();
        if (accountModel != null) {
            txtDrawerAccountUserName.setText(accountModel.getPersonName());
            txtDrawerAccountName.setText(accountModel.getAccountName());
            if (accountModel.getAvatar() != null) {
                Glide.with(this).load(accountModel.getAvatar()).into(imgAvatar);
            }

            imgAvatar.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                intent.putExtra(AccountActivity.EXTRA_ACCOUNT, session.getCurrentAccount());
                startActivity(intent);
            });
        }
    }

    @Override
    public void showLogoutDialog(boolean show) {
        if (show) {
            if (logoutDialog == null || !logoutDialog.isShowing()) {
                logoutDialog = new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setTitle(R.string.dialog_logout_title)
                        .setMessage(R.string.dialod_logout_message)
                        .setPositiveButton(R.string.dialog_btn_positive, (dialog, which) -> presenter.onLogoutConfirmed())
                        .setNegativeButton(R.string.dialog_btn_negative, (dialog, which) -> presenter.onLogoutCanceled())
                        .show();
            }
        } else {
            if (logoutDialog != null && logoutDialog.isShowing()) {
                logoutDialog.dismiss();
            }
        }
    }

    @Override
    public void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_lr_enter, R.anim.activity_slide_lr_leave);
    }

    @Override
    public void showError(Throwable th) {
        Toast.makeText(this, th.getMessage(), Toast.LENGTH_LONG).show();
    }

    public void animateToArrow() {
        if (arrowDrawable != null) {
            if (animToArrow == null) {
                animToArrow = initArrowAnim(true);
            }
            animToArrow.start();
        }
    }

    public void animateFromArrow() {
        if (arrowDrawable != null) {
            if (animFromArrow == null) {
                animFromArrow = initArrowAnim(false);
            }

            animFromArrow.start();
        }
    }

    private ValueAnimator initArrowAnim(boolean toArrow) {
        float valueFrom = toArrow ? 0 : 1;
        float valueTo = toArrow ? 1 : 0;

        ValueAnimator anim = ValueAnimator.ofFloat(valueFrom, valueTo).setDuration(Constants.Animation.ARROW_ANIM_DEFAULT_DURATION);
        anim.addUpdateListener(a -> {
            float position = (Float) a.getAnimatedValue();
            if (position == 1f) {
                arrowDrawable.setVerticalMirror(true);
            } else if (position == 0f) {
                arrowDrawable.setVerticalMirror(false);
            }
            arrowDrawable.setProgress(position);
        });
        if (toArrow) {
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    toolbar.setNavigationOnClickListener(v -> getFragmentManager().popBackStack());
                }
            });
        } else {
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    toggle.syncState();
                    toolbar.setNavigationOnClickListener(v -> drawer.openDrawer(GravityCompat.START));
                }
            });
        }

        return anim;
    }

    private void loadFragment(Fragment fragment, String tag) {
        getFragmentManager().beginTransaction().replace(R.id.content_main, fragment, tag).commit();
    }

}
