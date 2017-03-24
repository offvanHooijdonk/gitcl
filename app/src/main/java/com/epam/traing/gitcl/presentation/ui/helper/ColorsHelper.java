package com.epam.traing.gitcl.presentation.ui.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;

import com.epam.traing.gitcl.R;

/**
 * Created by Yahor_Fralou on 3/24/2017 1:20 PM.
 */

public class ColorsHelper {

    public static int getLanguageColor(Context ctx, @NonNull String lang) {
        int colorRes = R.color.label_lang_default;
        if (lang.equalsIgnoreCase("java")) {
            colorRes = R.color.label_java;
        } else if (lang.equalsIgnoreCase("typescript")) {
            colorRes = R.color.label_typescript;
        } else if (lang.equalsIgnoreCase("html")) {
            colorRes = R.color.label_html;
        }

        return ctx.getResources().getColor(colorRes);
    }

    public static void initRefreshLayout(Context ctx, SwipeRefreshLayout srl) {
        srl.setColorSchemeColors(ctx.getResources().getColor(R.color.refresh1),
                ctx.getResources().getColor(R.color.refresh2),
                ctx.getResources().getColor(R.color.refresh3));
    }

}
