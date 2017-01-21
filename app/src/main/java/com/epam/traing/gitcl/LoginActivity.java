package com.epam.traing.gitcl;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.txtSkipLogin)
    TextView txtSkipLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        txtSkipLogin.setPaintFlags(txtSkipLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    @OnClick(R.id.txtSkipLogin)
    void onSkipLoginClick() {
        // TODO remove this activity from backlog
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}
