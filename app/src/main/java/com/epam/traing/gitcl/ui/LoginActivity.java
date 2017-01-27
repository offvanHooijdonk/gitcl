package com.epam.traing.gitcl.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    @Inject
    ILoginPresenter loginPresenter;

    @Bind(R.id.txtSkipLogin)
    TextView txtSkipLogin;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GitClApplication.getAppComponent().inject(this);
        loginPresenter.setView(this);

        ButterKnife.bind(this);
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
            } break;
            case login_new: {
                animEnter = R.anim.activity_slide_rl_enter;
                animLeave = R.anim.activity_slide_rl_leave;
            } break;
            case logged_in: {
                animEnter = android.R.anim.fade_in;
                animLeave = android.R.anim.fade_out;
            } break;
            default: {
                animEnter = android.R.anim.fade_in;
                animLeave = android.R.anim.fade_out;
            }
        }
        overridePendingTransition(animEnter, animLeave);
    }
}
