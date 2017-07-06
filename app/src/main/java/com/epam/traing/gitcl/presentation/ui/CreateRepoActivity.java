package com.epam.traing.gitcl.presentation.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.presentation.ui.helper.ActivityAnimationHelper;

/**
 * Created by Yahor_Fralou on 3/24/2017 2:07 PM.
 */

public class CreateRepoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private View viewCollapsePlaceholder;
    private View mainLayout;
    private FloatingActionButton fabSave;

    private boolean isCollapseCalled = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_repo);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mainLayout = findViewById(R.id.mainLayout);
        viewCollapsePlaceholder = findViewById(R.id.collapsePlaceholder);
        fabSave = (FloatingActionButton) findViewById(R.id.fabSaveRepo);

        fabSave.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isCollapseCalled) {
            viewCollapsePlaceholder.setVisibility(View.VISIBLE);

            ActivityAnimationHelper.Activities.revealToToolbar(this, viewCollapsePlaceholder, mainLayout, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    fabSave.show();
                }
            });

            isCollapseCalled = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
