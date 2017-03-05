package com.epam.traing.gitcl.presentation.ui;

import android.support.v7.graphics.drawable.DrawerArrowDrawable;

/**
 * Created by off on 05.03.2017.
 */

public interface IArrowToggleAnimator {
    int ARROW_ANIM_DEFAULT_DURATION = 450;
    void animateToArrow();
    void animateFromArrow();
}
