package com.epam.traing.gitcl.presentation.ui.view.search;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.epam.traing.gitcl.R;

/**
 * Created by Yahor_Fralou on 3/16/2017 12:47 PM.
 */

public class SearchDialogFragment extends DialogFragment {
    private Context ctx;

    public static SearchDialogFragment newInstance() {
        Bundle bundle = new Bundle();
        SearchDialogFragment fragment = new SearchDialogFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_FRAME, 0);
        ctx = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_search, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        setupDialog();
    }

    private void setupDialog() {
        //getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.TOP);
            setCancelable(true);
            getDialog().setCancelable(true);
            getDialog().setCanceledOnTouchOutside(true);
            
        } else {
            Toast.makeText(ctx, "Error creating search dialog", Toast.LENGTH_LONG).show();
        }

    }
}
