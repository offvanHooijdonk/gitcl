package com.epam.traing.gitcl.presentation.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.epam.traing.gitcl.R;

/**
 * Created by Yahor_Fralou on 2/28/2017 12:18 PM.
 */

public class RepoIconView extends FrameLayout {
    public static final int TYPE_REPO = 0;
    public static final int TYPE_FORK = 1;
    public static final int DIMENSION_UNSET = -1;

    private int repoType;
    private boolean privateRepo;
    private boolean isOwn;
    private int repoIconSize;
    private int privateIconSize;

    ImageView imgIconCircle;
    ImageView imgRepoIcon;
    ImageView imgForkIcon;
    ImageView imgPrivateCircle;
    ImageView imgPrivateIcon;

    public RepoIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RepoIconView, 0, 0);

        try {
            repoType = ta.getInt(R.styleable.RepoIconView_repoType, TYPE_REPO);
            privateRepo = ta.getBoolean(R.styleable.RepoIconView_privateRepo, false);
            repoIconSize = ta.getDimensionPixelSize(R.styleable.RepoIconView_iconSize, DIMENSION_UNSET);
            privateIconSize = ta.getDimensionPixelSize(R.styleable.RepoIconView_privateIconSize, DIMENSION_UNSET);
            isOwn = ta.getBoolean(R.styleable.RepoIconView_isOwn, true);
        } finally {
            ta.recycle();
        }
        init();
        //if (!isInEditMode()) {
            updateRepoIcon();
            updatePrivateIcon();
            updateRepoIconSize();
            updatePrivateIconSize();
            updateIsOwn();
        //}
    }

    private void updateRepoIcon() {
        imgRepoIcon.setVisibility(repoType == TYPE_REPO ? VISIBLE : GONE);
        imgForkIcon.setVisibility(repoType == TYPE_FORK ? VISIBLE : GONE);
    }

    private void updatePrivateIcon() {
        imgPrivateCircle.setVisibility(privateRepo ? VISIBLE : GONE);
        imgPrivateIcon.setVisibility(privateRepo ? VISIBLE : GONE);
    }

    private void updateRepoIconSize() {
        if (repoIconSize != DIMENSION_UNSET) {
            imgIconCircle.getLayoutParams().height = repoIconSize;
            imgIconCircle.getLayoutParams().width = repoIconSize;
        }
    }

    private void updatePrivateIconSize() {
        if (privateIconSize != DIMENSION_UNSET) {
            imgPrivateCircle.getLayoutParams().height = privateIconSize;
            imgPrivateCircle.getLayoutParams().width = privateIconSize;
        }
    }

    private void updateIsOwn() {
        int circleColor = isOwn ? getContext().getResources().getColor(R.color.repo_own) :
                getContext().getResources().getColor(R.color.repo_not_own);
        DrawableCompat.wrap(imgIconCircle.getDrawable()).setTint(circleColor);
    }

    public boolean getPrivateRepo() {
        return privateRepo;
    }

    public void setPrivateRepo(boolean privateRepo) {
        this.privateRepo = privateRepo;
        updatePrivateIcon();
    }

    public int getRepoType() {
        return repoType;
    }

    public void setRepoType(int repoType) {
        this.repoType = repoType;
        updateRepoIcon();
    }

    public boolean getIsOwn() {
        return isOwn;
    }

    public void setIsOwn(boolean isOwn) {
        this.isOwn = isOwn;
        updateIsOwn();
    }

    private void init() {
        inflate(getContext(), R.layout.view_repo_icon, this);

        imgIconCircle = (ImageView) this.findViewById(R.id.imgIconCircle);
        imgRepoIcon = (ImageView) this.findViewById(R.id.imgRepoIcon);
        imgForkIcon = (ImageView) this.findViewById(R.id.imgForkIcon);
        imgPrivateCircle = (ImageView) this.findViewById(R.id.imgPrivateCircle);
        imgPrivateIcon = (ImageView) this.findViewById(R.id.imgPrivateIcon);
    }

}
