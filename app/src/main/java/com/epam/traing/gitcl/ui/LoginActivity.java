package com.epam.traing.gitcl.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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

    private enum ENTER_TYPE {
        skip, login_new, logged_in
    }

    boolean isNextScreenStarted = false;
    private String callbackUrl;

    @Inject
    ILoginPresenter loginPresenter;


    /*@Bind(R.id.editLayoutLogin)
    TextInputLayout editLayoutLogin;
    @Bind(R.id.editLayoutPassword)
    TextInputLayout editLayoutPassword;*/
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

        /*editLayoutLogin.setAlpha(0f);
        editLayoutPassword.setAlpha(0f);*/
        btnLogin.setAlpha(0f);
        txtSkipLogin.setAlpha(0f);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(callbackUrl)) {

        } else if (!isNextScreenStarted) {
            animateRevealLogin();
            isNextScreenStarted = true;
        }

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
        /*ObjectAnimator fadeEditLogin = ObjectAnimator.ofFloat(editLayoutLogin, View.ALPHA, 1f);
        ObjectAnimator fadeEditPassword = ObjectAnimator.ofFloat(editLayoutPassword, View.ALPHA, 1f);*/
        ObjectAnimator fadeBtnLogin = ObjectAnimator.ofFloat(btnLogin, View.ALPHA, 1f);
        ObjectAnimator fadeTxtSkipLogin = ObjectAnimator.ofFloat(txtSkipLogin, View.ALPHA, 1f);

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new AccelerateInterpolator());
        set.setDuration(500);

        set.play(scaleX)
                .with(scaleY)
                .with(moveY)
/*                .before(fadeEditLogin)
                .before(fadeEditPassword)*/
                .before(fadeBtnLogin)
                .before(fadeTxtSkipLogin);

        new Handler().postDelayed(set::start, 750);
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
    public void showAuthErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void startMainViewAsLogged() {
        startMain(ENTER_TYPE.logged_in);
    }

    @Override
    public void startMainViewAsAnon() {
        startMain(ENTER_TYPE.skip);
    }

    @Override
    public void startWebViewForOAuth(String authUrl, String callbackUrl) {
        Intent intent = new Intent(
                Intent.ACTION_VIEW, Uri.parse(authUrl));
        startActivity(intent);
    }

    private void startMain(ENTER_TYPE enterType) {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        setTransitionToMain(enterType);
    }

    private void setTransitionToMain(ENTER_TYPE enterType) {
        int animEnter;
        int animLeave;
        switch (enterType) {
            case skip: {
                animEnter = R.anim.activity_slide_rl_enter;
                animLeave = R.anim.activity_slide_rl_leave;
            }
            break;
            case login_new: {
                animEnter = R.anim.activity_slide_rl_enter;
                animLeave = R.anim.activity_slide_rl_leave;
            }
            break;
            case logged_in: {
                animEnter = android.R.anim.fade_in;
                animLeave = android.R.anim.fade_out;
            }
            break;
            default: {
                animEnter = android.R.anim.fade_in;
                animLeave = android.R.anim.fade_out;
            }
        }
        overridePendingTransition(animEnter, animLeave);
    }
}
