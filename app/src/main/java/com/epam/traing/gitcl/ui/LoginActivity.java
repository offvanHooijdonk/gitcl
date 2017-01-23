package com.epam.traing.gitcl.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.epam.traing.gitcl.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private enum ENTER_TYPE {
        skip, login_new, logged_in
    }

    @Bind(R.id.txtSkipLogin)
    TextView txtSkipLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnLogin)
    void onLoginClick() {
        startMain(ENTER_TYPE.logged_in);
    }

    @OnClick(R.id.txtSkipLogin)
    void onSkipLoginClick() {
        startMain(ENTER_TYPE.skip);
    }

    private void startMain(ENTER_TYPE enterType) {
        // TODO remove this activity from backlog
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
