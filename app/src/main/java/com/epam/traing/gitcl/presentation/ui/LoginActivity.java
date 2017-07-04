package com.epam.traing.gitcl.presentation.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.app.GitClientApplication;
import com.epam.traing.gitcl.di.DependencyManager;
import com.epam.traing.gitcl.presentation.presenter.ILoginPresenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    boolean isNextScreenStarted = false;

    @Inject
    ILoginPresenter loginPresenter;

    @Bind(R.id.btnLogin)
    Button btnLogin;
    @Bind(R.id.txtSkipLogin)
    TextView txtSkipLogin;
    @Bind(R.id.imgLogo)
    ImageView imgLogo;
    @Bind(R.id.txtErrMsg)
    TextView txtErrMsg;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DependencyManager.getLoginScreenComponent().inject(this);
        loginPresenter.attachView(this);

        ButterKnife.bind(this);

        btnLogin.setAlpha(0f);
        txtSkipLogin.setAlpha(0f);
        txtErrMsg.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(GitClientApplication.LOG, "onResume");

        loginPresenter.onActivityResume(getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        loginPresenter.detachView();
        DependencyManager.releaseLoginScreenComponent();
    }

    @OnClick(R.id.btnLogin)
    void onLoginClick() {
        loginPresenter.onLoginSelected();
    }

    @OnClick(R.id.txtSkipLogin)
    void onSkipLoginClick() {
        loginPresenter.onSkipLoginSelected();
    }

    @Override
    public void showLoginProgress(boolean show) {
        Log.d(GitClientApplication.LOG, "Progress: " + show);
        if (show) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(LoginActivity.this.getString(R.string.authentication_progress));
            }

            progressDialog.show();
        } else {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void showAuthErrorMessage(Throwable th) {
        Log.e(GitClientApplication.LOG, "Error handled.", th);
        showLoginScreen();
        Toast.makeText(this, th.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showAuthRequestedMessage() {
        txtErrMsg.setText(R.string.error_msg_account_not_found);
        txtErrMsg.setVisibility(View.VISIBLE);
    }

    @Override
    public void startMainView() {
        Log.d(GitClientApplication.LOG, "Starting Main Activity");
        startMain();
    }

    @Override
    public void showLoginScreen() {
        if (!isNextScreenStarted) {
            Log.d(GitClientApplication.LOG, "Animation reveal started.");
            animateRevealLogin();
            isNextScreenStarted = true;
        }
    }

    @Override
    public void startWebViewForOAuth(String authUrl) {
        Intent intent = new Intent(
                Intent.ACTION_VIEW, Uri.parse(authUrl));
        startActivity(intent);
    }

    private void animateRevealLogin() {
        ObjectAnimator fadeBtnLogin = ObjectAnimator.ofFloat(btnLogin, View.ALPHA, 1f);
        ObjectAnimator fadeTxtSkipLogin = ObjectAnimator.ofFloat(txtSkipLogin, View.ALPHA, 1f);

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new AccelerateInterpolator());
        set.setDuration(500);
        set.play(fadeBtnLogin)
                .with(fadeTxtSkipLogin);
        set.start();
    }

    private void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        setTransitionToMain();
    }

    private void setTransitionToMain() {
        int animEnter;
        int animLeave;
        if (isNextScreenStarted) {
            animEnter = R.anim.activity_slide_rl_enter;
            animLeave = R.anim.activity_slide_rl_leave;
        } else {
            animEnter = android.R.anim.fade_in;
            animLeave = android.R.anim.fade_out;
        }
        overridePendingTransition(animEnter, animLeave);
    }
}
