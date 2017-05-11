package com.epam.traing.gitcl.helper;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by Yahor_Fralou on 5/11/2017 11:20 AM.
 */

public class CustomGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        Glide.get(context).setMemoryCategory(MemoryCategory.LOW);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
