package com.epam.traing.gitcl.presentation.ui.view.search;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.model.HistoryModel;
import com.epam.traing.gitcl.di.DependencyManager;
import com.epam.traing.gitcl.presentation.presenter.ISearchPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Yahor_Fralou on 3/16/2017 12:47 PM.
 */

public class SearchDialogFragment extends DialogFragment implements ISearchView, ViewTreeObserver.OnPreDrawListener, SearchListAdapter.ItemClickListener {
    public static final int HISTORY_SHOW_MAX = 5;
    public static final int DEFAULT_MIN_CHARS_FOR_FULL_SEARCH = 3;

    private static final String EXTRA_ANIM_X = "extra_anim_x";
    private static final String EXTRA_ANIM_Y = "extra_anim_y";
    private static final int LIVE_SEARCH_THROTTLE = 250;

    private Context ctx;
    private InputMethodManager imm;
    private SearchRevealAnim revealAnim;
    private SearchListAdapter adapter;
    private List<SearchListAdapter.ItemWrapper> searchResults;
    private PublishSubject<String> obsLiveQuery;
    private PublishSubject<String> obsFullQuery;

    private View viewSearchBar;
    private View rootView;
    private ImageView imgBack;
    private ImageView imgClear;
    private ImageView imgSearch;
    private EditText inputSearch;
    private View viewBackOverlay;
    private RecyclerView listView;
    private ProgressBar progressSearch;

    @Inject
    ISearchPresenter searchPresenter;

    private AccountModel user;
    private int minCharsForFullSearch = DEFAULT_MIN_CHARS_FOR_FULL_SEARCH;
    private boolean isLiveSearchEnabled = true;

    public static SearchDialogFragment newInstance(View animateOverView, AccountModel user) {
        int[] centerLocation = SearchRevealAnim.getViewCenterLocation(animateOverView);
        int startX = centerLocation[0];
        int startY = centerLocation[1];

        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_ANIM_X, startX);
        bundle.putInt(EXTRA_ANIM_Y, startY);
        SearchDialogFragment fragment = new SearchDialogFragment();
        fragment.setArguments(bundle);
        fragment.user = user;

        return fragment;
    }

    public SearchDialogFragment() {
        obsLiveQuery = PublishSubject.create();
        obsFullQuery = PublishSubject.create();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DependencyManager.getSearchScreenComponent().inject(this);

        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_FRAME, R.style.AppBaseTheme_SearchDialog);
        ctx = getActivity();
        imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);

        searchResults = new ArrayList<>();
        adapter = new SearchListAdapter(ctx, searchResults, user);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_search, container, false);

        rootView = view;
        setupLayout();

        adapter.setHistoryPickListener(text -> inputSearch.setText(text));
        adapter.setItemClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        setupDialog();
    }

    public Observable<String> observeLiveQuery() {
        return obsLiveQuery.throttleLast(LIVE_SEARCH_THROTTLE, TimeUnit.MILLISECONDS);
    }

    public Observable<String> observeFullQuery() {
        return obsFullQuery;
    }

    public void updateResults(List<SearchListAdapter.ItemWrapper> results) {
        searchResults.clear();
        searchResults.addAll(results);
        if (searchResults.isEmpty()) {
            listView.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.VISIBLE);
        }
        adapter.setSearchText(inputSearch.getText().toString());
        adapter.notifyDataSetChanged();

        showSearchProgress(false);
    }

    @Override
    public boolean onPreDraw() {
        imgSearch.getViewTreeObserver().removeOnPreDrawListener(this);
        int startX = getArguments().getInt(EXTRA_ANIM_X);
        int startY = SearchRevealAnim.getViewCenterLocation(imgSearch)[1];
        revealAnim = new SearchRevealAnim(viewSearchBar, startX, startY);
        revealAnim.setListener(new SearchRevealAnim.AnimationListener() {
            @Override
            public void onShowAnimationEnd() {
                search(false);
                showKeyBoard(true);
            }

            @Override
            public void onHideAnimationEnd() {
                performCloseActions();
            }
        });

        revealAnim.animate(true);

        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (adapter != null) {
            adapter.setOrientation(newConfig.orientation);
        }
    }

    @Override
    public void onSearchItemClick(SearchListAdapter.ItemWrapper itemWrapper) {
        if (itemWrapper.getType() == SearchListAdapter.ItemWrapper.HISTORY) {
            HistoryModel historyModel = (HistoryModel) itemWrapper.getItem();

            isLiveSearchEnabled = false;
            inputSearch.setText(historyModel.getText());
            listView.setVisibility(View.GONE);
            isLiveSearchEnabled = true;

            searchByClick();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        obsLiveQuery.onCompleted();
        obsFullQuery.onCompleted();

        searchPresenter.detachView();
    }

    public void setMinCharsForFullSearch(int minCharsForFullSearch) {
        this.minCharsForFullSearch = minCharsForFullSearch;
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
        imgClear = (ImageView) rootView.findViewById(R.id.imgClear);
        imgSearch = (ImageView) rootView.findViewById(R.id.imgSearch);
        inputSearch = (EditText) rootView.findViewById(R.id.inputSearch);
        viewBackOverlay = rootView.findViewById(R.id.viewBackgroundOverlay);
        listView = (RecyclerView) rootView.findViewById(R.id.listSearchResults);
        viewSearchBar = rootView.findViewById(R.id.blockSearchBar);
        progressSearch = (ProgressBar) rootView.findViewById(R.id.progressSearch);

        imgBack.setOnClickListener(v -> closeSearchDialog());
        imgClear.setOnClickListener(v -> clearSearch());
        enableClearControls(false);
        imgSearch.getViewTreeObserver().addOnPreDrawListener(this);
        imgSearch.setOnClickListener(v -> searchByClick());
        enableFullSearchControls(false);
        viewBackOverlay.setOnClickListener(v -> closeSearchDialog());

        listView.setLayoutManager(new LinearLayoutManager(ctx));
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setHasFixedSize(true);
        listView.setAdapter(adapter);
        listView.setVisibility(View.GONE);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                enableFullSearchControls(inputSearch.getText().length() >= minCharsForFullSearch);
                if (isLiveSearchEnabled) search(false);
                enableClearControls(inputSearch.getText().length() > 0);
            }
        });

        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                closeSearchDialog();
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                searchByClick();
                return true;
            }
            return false;
        });
    }

    private void closeSearchDialog() {
        if (!searchResults.isEmpty()) {
            Animator a = ObjectAnimator.ofFloat(listView, View.ALPHA, 1, 0).setDuration(200);
            a.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    revealAnim.animate(false);
                }
            });
            a.start();
        } else {
            revealAnim.animate(false);
        }
    }

    private void searchByClick() {
        if (inputSearch.getText().length() >= minCharsForFullSearch) {
            showKeyBoard(false);
            showSearchProgress(true);

            search(true);
        } else {
            Toast.makeText(ctx, ctx.getString(R.string.search_input_less_than, String.valueOf(minCharsForFullSearch)), Toast.LENGTH_LONG).show();
        }
    }

    private void search(boolean isFull) {
        String queryText = inputSearch.getText().toString();
        Log.i("LOG", "Query text: " + queryText);
        if (isFull) {
            obsFullQuery.onNext(queryText);
        } else {
            obsLiveQuery.onNext(queryText);
        }
    }

    private void showSearchProgress(boolean isShowProgress) {
        imgSearch.setVisibility(isShowProgress ? View.GONE : View.VISIBLE);
        progressSearch.setVisibility(isShowProgress ? View.VISIBLE : View.GONE);
    }

    private void clearSearch() {
        inputSearch.setText("");
        showKeyBoard(true);
    }

    private void enableFullSearchControls(boolean isEnable) {
        imgSearch.setEnabled(isEnable);
        DrawableCompat.setTint(imgSearch.getDrawable(), ctx.getResources().getColor(isEnable ? R.color.dialog_controls : R.color.dialog_controls_disabled));
    }

    private void enableClearControls(boolean isEnable) {
        imgClear.setEnabled(isEnable);
        DrawableCompat.setTint(imgClear.getDrawable(), ctx.getResources().getColor(isEnable ? R.color.dialog_controls : R.color.dialog_controls_disabled));
    }

    private void performCloseActions() {
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
