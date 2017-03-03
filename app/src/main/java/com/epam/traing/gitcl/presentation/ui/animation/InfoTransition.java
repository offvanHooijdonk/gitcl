package com.epam.traing.gitcl.presentation.ui.animation;

import android.content.Context;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.util.AttributeSet;

/**
 * Created by Yahor_Fralou on 2/28/2017 12:03 PM.
 */

public class InfoTransition extends TransitionSet {
    public static final int DURATION = 500;

    public InfoTransition() {
        init();
    }

    public InfoTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setDuration(DURATION);
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds()).
                addTransition(new ChangeTransform()).
                addTransition(new ChangeImageTransform())
                .addTransition(new ChangeClipBounds());
    }
}
