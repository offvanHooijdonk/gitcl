package com.epam.traing.gitcl.presentation.ui.helper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 * Created by Yahor_Fralou on 3/24/2017 1:53 PM.
 */

public class AnimationHelper {
    private static final int DURATION_IN = 225;
    private static final int DURATION_IN_LONG = 325;

    public static class Circle {

        public static void revealViewWithFAB(@NonNull View v, @NonNull View viewCircleOn, @Nullable Integer radius, @Nullable AnimatorListenerAdapter listener) {
            int[] location = new int[2];
            viewCircleOn.getLocationInWindow(location);

            int cx = location[0] + viewCircleOn.getWidth()/2;
            int cy = location[1] + viewCircleOn.getHeight()/2;

            v.setVisibility(View.INVISIBLE);
            int finalRadius = radius != null ? radius : getAnimRadius(v);

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(v, cx, cy, viewCircleOn.getWidth() / 2, finalRadius);
            anim.setDuration(DURATION_IN);

            if (listener != null) {
                anim.addListener(listener);
            }

            v.setVisibility(View.VISIBLE);

            if (viewCircleOn instanceof FloatingActionButton) {
                ((FloatingActionButton) viewCircleOn).hide();
            } else {
                viewCircleOn.setVisibility(View.INVISIBLE);
            }
            anim.start();
        }

        private static int getAnimRadius(View v) {
            return Math.max(v.getWidth(), v.getHeight());
        }

    }

    public static class Activities {

        /**
         * Place in onResume() method
         *
         * @param ctx Context
         * @param collapseView The view to be collapsed to the toolbar size
         * @param mainLayout Layout that will be shown on collapse animation complete
         */
        public static void revealToToolbar(@NonNull Context ctx, @NonNull final View collapseView, @NonNull final View mainLayout) {
            prepareRevealToToolbar(collapseView, mainLayout);

            TypedValue tv = new TypedValue();
            int actionBarHeight = 0;
            if (ctx.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, ctx.getResources().getDisplayMetrics());
            }

            ScaleAnimation anim = new ScaleAnimation(1.0f, 1.0f,
                    1.0f, (float) actionBarHeight / ctx.getResources().getDisplayMetrics().heightPixels);

            anim.setDuration(DURATION_IN_LONG);
            anim.setInterpolator(new LinearOutSlowInInterpolator());
            anim.setFillAfter(true);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    finishRevealToToolBar(collapseView, mainLayout);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            collapseView.startAnimation(anim);
        }

        private static void prepareRevealToToolbar(@NonNull View collapseView, @NonNull View mainLayout) {
            mainLayout.setVisibility(View.INVISIBLE);
            collapseView.setVisibility(View.VISIBLE);
        }

        private static void finishRevealToToolBar(@NonNull View collapseView, @NonNull View mainLayout) {

            ObjectAnimator.ofFloat(collapseView, View.ALPHA, 1.0f, 0.0f).setDuration(DURATION_IN).start();
            Animator animMainLayout = ObjectAnimator.ofFloat(mainLayout, View.ALPHA, 0.0f, 1.0f).setDuration(DURATION_IN);
            animMainLayout.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationEnd(animation);
                    mainLayout.setVisibility(View.VISIBLE);
                }
            });
            animMainLayout.start();
        }
    }
}
