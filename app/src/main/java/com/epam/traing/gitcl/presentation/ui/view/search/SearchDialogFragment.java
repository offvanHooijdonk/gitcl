package com.epam.traing.gitcl.presentation.ui.view.search;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
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

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Yahor_Fralou on 3/16/2017 12:47 PM.
 */

public class SearchDialogFragment extends DialogFragment implements ViewTreeObserver.OnPreDrawListener {
    private static final String EXTRA_ANIM_X = "extra_anim_x";
    private static final String EXTRA_ANIM_Y = "extra_anim_y";

    private Context ctx;
    private InputMethodManager imm;
    private SearchRevealAnim revealAnim;
    private SearchListAdapter adapter;
    private List<SearchListAdapter.ItemWrapper> searchResults;
    private PublishSubject<String> obsLiveQuery = PublishSubject.create();
    private PublishSubject<String> obsFullQuery = PublishSubject.create();

    private View rootView;
    private ImageView imgBack;
    private ImageView imgSearch;
    private EditText inputSearch;
    private View viewBackOverlay;
    private RecyclerView listView;

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
        imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);

        searchResults = new ArrayList<>();
        adapter = new SearchListAdapter(ctx, searchResults);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_search, container, false);

        rootView = view;
        setupLayout();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        setupDialog();
    }

    public Observable<String> observeLiveQuery() {
        return obsLiveQuery;
    }

    public Observable<String> observeFullQuery() {
        return obsFullQuery;
    }

    public void updateResults(List<SearchListAdapter.ItemWrapper> results) {
        searchResults.clear();
        searchResults.addAll(results);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onPreDraw() {
        imgSearch.getViewTreeObserver().removeOnPreDrawListener(this);
        int startX = getArguments().getInt(EXTRA_ANIM_X);
        int startY = SearchRevealAnim.getViewCenterLocation(imgSearch)[1];
        revealAnim = new SearchRevealAnim(rootView, startX, startY); // TODO add flag for this
        revealAnim.setListener(new SearchRevealAnim.AnimationListener() {
            @Override
            public void onShowAnimationEnd() {
                showKeyBoard(true);
            }

            @Override
            public void onHideAnimationEnd() {
                closeDialog();
            }
        });

        revealAnim.animate(true);

        return true;
    }

    private void setupDialog() {
        Window window = getDialog().getWindow();
        if (window != null) {
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

    private void setupLayout() {
        imgBack = (ImageView) rootView.findViewById(R.id.imgBack);
        imgSearch = (ImageView) rootView.findViewById(R.id.imgSearch);
        inputSearch = (EditText) rootView.findViewById(R.id.inputSearch);
        viewBackOverlay = rootView.findViewById(R.id.viewBackgroundOverlay);
        listView = new RecyclerView(ctx);

        imgBack.setOnClickListener(v -> closeDialog());
        imgSearch.getViewTreeObserver().addOnPreDrawListener(this);
        imgSearch.setOnClickListener(v -> searchByClick());
        viewBackOverlay.setOnClickListener(v -> revealAnim.animate(false));

        listView.setLayoutManager(new LinearLayoutManager(ctx));
        listView.setAdapter(adapter);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                search(false);
            }
        });

        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                closeDialog();
            } else if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                searchByClick();
            }
            return false;
        });
    }

    private void searchByClick() {
        showKeyBoard(false);
        // TODO save to history here ?

        search(true);
    }

    private void search(boolean isFull) {
        String queryText = inputSearch.getText().toString();
        if (queryText == null || queryText.isEmpty()) return;
        if (isFull) {
            obsFullQuery.onNext(queryText);
        } else {
            obsLiveQuery.onNext(queryText);
        }
    }

    private void closeDialog() {
        showKeyBoard(false);

        obsFullQuery.onCompleted();
        obsLiveQuery.onCompleted();

        this.dismiss();
    }

    private void showKeyBoard(boolean isShow) {
        if (isShow) {
            imm.showSoftInput(inputSearch, InputMethodManager.RESULT_SHOWN);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        } else {
            imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
        }
    }
}
