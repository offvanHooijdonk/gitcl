package com.epam.traing.gitcl.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.app.GitClApplication;
import com.epam.traing.gitcl.presenter.ILoginPresenter;

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
    @Bind(R.id.imgLogoAnim)
    ImageView imgLogoAnim;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GitClApplication.getAppComponent().inject(this);
        loginPresenter.setView(this);

        ButterKnife.bind(this);

        btnLogin.setAlpha(0f);
        txtSkipLogin.setAlpha(0f);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(GitClApplication.LOG, "onResume");

        loginPresenter.onActivityResume(getIntent());
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
    public void startLoginProgress(boolean show) {
        Log.d(GitClApplication.LOG, "Progress: " + show);
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
        Log.e(GitClApplication.LOG, "Error handled.", th);
        showLoginScreen();
        Toast.makeText(this, th.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void startMainView() {
        Log.d(GitClApplication.LOG, "Starting Main Activity");
        startMain();
    }

    @Override
    public void showLoginScreen() {
        if (!isNextScreenStarted) {
            Log.d(GitClApplication.LOG, "Animation reveal started.");
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
        float marginTopStart = getResources().getDimensionPixelSize(R.dimen.login_logo_vert_margin_start);
        float marginTopEnd = getResources().getDimensionPixelSize(R.dimen.login_logo_vert_margin);
        float sizeStart = getResources().getDimensionPixelSize(R.dimen.login_logo_anim_size);
        float sizeEnd = getResources().getDimensionPixelSize(R.dimen.login_logo_size);
        float logoEndScale = sizeEnd / sizeStart;

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imgLogoAnim, View.SCALE_X, 1, logoEndScale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imgLogoAnim, View.SCALE_Y, 1, logoEndScale);
        ObjectAnimator moveY = ObjectAnimator.ofFloat(imgLogoAnim, View.Y, marginTopStart, marginTopEnd);
        ObjectAnimator fadeBtnLogin = ObjectAnimator.ofFloat(btnLogin, View.ALPHA, 1f);
        ObjectAnimator fadeTxtSkipLogin = ObjectAnimator.ofFloat(txtSkipLogin, View.ALPHA, 1f);

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new AccelerateInterpolator());
        set.setDuration(500);

        set.play(scaleX)
                .with(scaleY)
                .with(moveY)
                .before(fadeBtnLogin)
                .before(fadeTxtSkipLogin);
        // TODO return observable to notify animation end
        new Handler().postDelayed(set::start, 750);
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
