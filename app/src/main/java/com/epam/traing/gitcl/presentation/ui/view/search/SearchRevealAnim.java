package com.epam.traing.gitcl.presentation.ui.view.search;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Yahor_Fralou on 3/17/2017 2:40 PM.
 */

public class SearchRevealAnim {
    private static final int DURATION_IN = 225;
    private static final int DURATION_OUT = 195;

    private AnimationListener listener;
    private View animView;
    private int startX;
    private int startY;

    public SearchRevealAnim(@NonNull View animView, int animStartX, int animStartY) {
        this.animView = animView;
        this.startX = animStartX;
        this.startY = animStartY;
    }

    public void animate(boolean isShow) {
        int[] animViewLocation = new int[2];
        animView.getLocationInWindow(animViewLocation);
        int avX = animViewLocation[0] + animView.getWidth() / 2;
        int avY = animViewLocation[1] + animView.getHeight() / 2;

        int rippleW = startX < avX ? animView.getWidth() - startX : startX - animViewLocation[0];
        int rippleH = startY < avY ? animView.getHeight() - startY : startY - animViewLocation[1];

        float maxRadius = (float) Math.sqrt(rippleW * rippleW + rippleH * rippleH);

        float startRadius = isShow ? 0 : maxRadius;
        float endRadius = isShow ? maxRadius : 0;

        Log.i("LOG", startRadius + " | " + endRadius + " | "  + maxRadius);

        Animator anim = ViewAnimationUtils.createCircularReveal(animView, startX, startY, startRadius, endRadius);
        animView.setVisibility(View.VISIBLE);
        anim.setDuration(isShow ? DURATION_IN : DURATION_OUT);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (isShow) {
                    animView.setVisibility(View.VISIBLE);
                    if (listener != null) listener.onShowAnimationEnd();
                } else {
                    animView.setVisibility(View.GONE);
                    if (listener != null) listener.onHideAnimationEnd();
                }
            }
        });

        anim.start();
    }

    public void setListener(AnimationListener listener) {
        this.listener = listener;
    }

    public static int[] getViewCenterLocation(View v) {
        int startX = 0;
        int startY = 0;
        if (v != null) {
            int[] startLocation = new int[2];
            v.getLocationInWindow(startLocation);
            startX = startLocation[0] + v.getWidth() / 2;
            startY = startLocation[1] + v.getHeight() / 2;
        }

        return new int[]{startX, startY};
    }

    public interface AnimationListener {
        void onShowAnimationEnd();
        void onHideAnimationEnd();
    }
}
