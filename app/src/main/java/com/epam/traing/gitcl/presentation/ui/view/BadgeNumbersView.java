package com.epam.traing.gitcl.presentation.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.app.GitClientApplication;

/**
 * Created by Yahor_Fralou on 3/3/2017 1:42 PM.
 */

public class BadgeNumbersView extends FrameLayout {
    public static final int STATE_INACTIVE = 0;
    public static final int STATE_ACTIVE = 1;
    public static final int DIMENSION_UNSET = -1;
    private static final int DEFAULT_NUMBER = 0;

    private NumberFormatter formatter;

    private Drawable badgeDrawable;
    private int imageSize;
    private int textSize;
    private int state;
    private long numberValue;

    private TextView txtBadgeNumber;
    private ImageView imgBadgeIcon;

    public BadgeNumbersView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        formatter = new NumberFormatter(getContext());

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BadgeNumbersView, 0, 0);
        try {
            imageSize = ta.getDimensionPixelSize(R.styleable.BadgeNumbersView_imageSize, DIMENSION_UNSET);
            textSize = ta.getDimensionPixelSize(R.styleable.BadgeNumbersView_textSize, DIMENSION_UNSET);
            state = ta.getInt(R.styleable.BadgeNumbersView_state, STATE_INACTIVE);
            badgeDrawable = ta.getDrawable(R.styleable.BadgeNumbersView_src);
            String numString = null;
            try {
                numString = ta.getString(R.styleable.BadgeNumbersView_numberValue);
                if (numString != null && !numString.isEmpty()) {
                    numberValue = Long.parseLong(ta.getString(R.styleable.BadgeNumbersView_numberValue));
                } else {
                    numberValue = DEFAULT_NUMBER;
                }
            } catch (NumberFormatException e) {
                Log.e(GitClientApplication.LOG, "Error parsing numberValue = " + numString, e);
                numberValue = DEFAULT_NUMBER;
            }
        } catch (Exception e) {
            Log.e(GitClientApplication.LOG, "Error getting badge attrs.", e);
        } finally {
            ta.recycle();
        }

        init();

        updateState();
        updateBadgeDrawable();
        updateImageSize();
        updateTextSize();
        updateNumberValue();
    }

    private void init() {
        inflate(getContext(), R.layout.view_badge_numbers, this);

        txtBadgeNumber = (TextView) this.findViewById(R.id.txtBadgeNumber);
        imgBadgeIcon = (ImageView) this.findViewById(R.id.imgBadgeIcon);
    }

    private void updateState() {
        int colorResource = state == STATE_ACTIVE ? R.color.badge_active : R.color.badge_inactive;
        int color = getContext().getResources().getColor(colorResource);

        DrawableCompat.setTint(imgBadgeIcon.getDrawable(), color);
        txtBadgeNumber.setTextColor(color);
    }

    private void updateImageSize() {
        if (imageSize != DIMENSION_UNSET && imageSize > 0) {
            imgBadgeIcon.getLayoutParams().height = imageSize;
            imgBadgeIcon.getLayoutParams().width = imageSize;
        }
    }

    private void updateTextSize() {
        if (textSize != DIMENSION_UNSET && textSize > 0) {
            txtBadgeNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }

    private void updateNumberValue() {
        txtBadgeNumber.setText(formatter.formatNumber(numberValue));
    }

    private void updateBadgeDrawable() {
        if (badgeDrawable != null) {
            imgBadgeIcon.setImageDrawable(badgeDrawable);
        }
    }

    public int getImageSize() {
        return imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
        updateImageSize();
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        updateTextSize();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        updateState();
    }

    public long getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(long numberValue) {
        this.numberValue = numberValue;
        updateNumberValue();
    }

    public Drawable getBadgeDrawable() {
        return badgeDrawable;
    }

    public void setBadgeDrawable(Drawable badgeDrawable) {
        this.badgeDrawable = badgeDrawable;
        updateBadgeDrawable();
    }

    public static class NumberFormatter {
        private String signThousand;
        private String signMillion;
        private String signBillion;
        private String signTrillion;

        public NumberFormatter(Context ctx) {
            signThousand = ctx.getString(R.string.sign_thousand);
            signMillion = ctx.getString(R.string.sign_million);
            signBillion = ctx.getString(R.string.sign_billion);
            signTrillion = ctx.getString(R.string.sign_trillion);
        }

        public String formatNumber(long number) {
            int grade = (String.valueOf(number).length() - 1) / 3;
            int topGradeNumber = (int) (number / Math.pow(10, 3 * grade));

            String result = String.valueOf(topGradeNumber);
            if (result.length() == 1 && grade > 0) {
                int subTopNumber = (int) (number % Math.pow(10, 3 * grade) / Math.pow(10, 3 * grade - 1));
                if (subTopNumber > 0) {
                    result += "." + String.valueOf(subTopNumber);
                }
            }

            String gradeSign;
            switch (grade) {
                case 1:
                    gradeSign = signThousand;
                    break;
                case 2:
                    gradeSign = signMillion;
                    break;
                case 3:
                    gradeSign = signBillion;
                    break;
                case 4:
                    gradeSign = signTrillion;
                    break;
                default:
                    gradeSign = "";
            }
            result += gradeSign;

            return result;
        }
    }
}
