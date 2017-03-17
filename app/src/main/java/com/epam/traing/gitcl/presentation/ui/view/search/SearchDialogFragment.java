package com.epam.traing.gitcl.presentation.ui.view.search;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.epam.traing.gitcl.R;

/**
 * Created by Yahor_Fralou on 3/16/2017 12:47 PM.
 */

public class SearchDialogFragment extends DialogFragment implements ViewTreeObserver.OnPreDrawListener {
    private static final String EXTRA_ANIM_X = "extra_anim_x";
    private static final String EXTRA_ANIM_Y = "extra_anim_y";

    private Context ctx;
    private SearchRevealAnim revealAnim;

    private ImageView imgClose;
    private EditText inputSearch;

    public static SearchDialogFragment newInstance(View animateOverView) {
        int[] centerLocation = SearchRevealAnim.getViewCenterLocation(animateOverView);
        int startX = centerLocation[0];
        int startY = centerLocation[1];

        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_ANIM_X, startX);
        bundle.putInt(EXTRA_ANIM_Y, startY);
        SearchDialogFragment fragment = new SearchDialogFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_FRAME, R.style.AppBaseTheme_SearchDialog);
        ctx = getActivity();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_search, container, false);

        int startX = getArguments().getInt(EXTRA_ANIM_X);
        int startY = getArguments().getInt(EXTRA_ANIM_Y);
        revealAnim = new SearchRevealAnim(view, startX, startY);
        revealAnim.setListener(new SearchRevealAnim.AnimationListener() {
            @Override
            public void onShowAnimationEnd() {
                showKeyBoard(true);
            }
            @Override
            public void onHideAnimationEnd() {

            }
        });

        setupLayout(view);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        setupDialog();
    }

    private void setupDialog() {
        Window window = getDialog().getWindow();
        if (window != null) {
            //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.TOP);
            setCancelable(true);
            getDialog().setCancelable(true);
            getDialog().setCanceledOnTouchOutside(true);
            window.setWindowAnimations(R.style.DialogEmptyAnimation);

        } else {
            Toast.makeText(ctx, "Error creating search dialog", Toast.LENGTH_LONG).show();
        }

    }

    private void setupLayout(View root) {
        imgClose = (ImageView) root.findViewById(R.id.imgClose);
        inputSearch = (EditText) root.findViewById(R.id.inputSearch);

        imgClose.getViewTreeObserver().addOnPreDrawListener(this);

    }

    @Override
    public boolean onPreDraw() {
        imgClose.getViewTreeObserver().removeOnPreDrawListener(this);
        revealAnim.animate(true);

        return true;
    }

    private void showKeyBoard(boolean isShow) {
        if (isShow) {
            InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(inputSearch, InputMethodManager.RESULT_SHOWN);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }
}
