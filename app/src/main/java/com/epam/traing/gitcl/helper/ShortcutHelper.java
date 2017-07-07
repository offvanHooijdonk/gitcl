package com.epam.traing.gitcl.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.model.AccountModel;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Yahor_Fralou on 7/7/2017 6:21 PM.
 */


public class ShortcutHelper {
    private static final String SHORTCUT_ID_CURRENT_ACCOUNT = "current_account";

    private Context ctx;
    private ShortcutManager shortcutManager;

    public ShortcutHelper(Context ctx) {
        this.ctx = ctx;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            this.shortcutManager = this.ctx.getSystemService(ShortcutManager.class);
        }
    }

    @RequiresApi(25)
    public void setupCurrentAccountShortcut(AccountModel account) {
        Bitmap iconBitmap;
        if (account.getAvatar() != null) {
            try {
                iconBitmap = Glide.with(ctx)
                        .load(account.getAvatar())
                        .asBitmap()
                        .transform(new CropCircleTransformation(ctx))
                        .into(100, 100)// TODO improve, remove hard-coded numbers
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                iconBitmap = null;
            }
        } else {
            iconBitmap = null;
        }

        if (iconBitmap == null) {
            // TODO if the image must fit to 48*48 dp ?
            iconBitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_github_96);
        }

        ShortcutInfo shortcut = new ShortcutInfo.Builder(ctx, SHORTCUT_ID_CURRENT_ACCOUNT)
                .setShortLabel(account.getAccountName())
                .setLongLabel(account.getAccountName())
                .setIcon(Icon.createWithBitmap(iconBitmap))
                .setIntent(getCurrentAccountIntent(account))
                .build();

        shortcutManager.removeDynamicShortcuts(Collections.singletonList(SHORTCUT_ID_CURRENT_ACCOUNT));
        shortcutManager.addDynamicShortcuts(Collections.singletonList(shortcut));
    }

    private Intent getCurrentAccountIntent(AccountModel account) {
        // TODO improve AccountActivity to take just account id
        return new Intent("com.epam.traing.gitcl.VIEW_ACCOUNT");
    }
}
