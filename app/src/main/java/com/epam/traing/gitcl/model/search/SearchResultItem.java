package com.epam.traing.gitcl.model.search;

import android.support.annotation.NonNull;

/**
 * Created by Yahor_Fralou on 7/6/2017 6:23 PM.
 */

public class SearchResultItem implements Comparable<SearchResultItem> {
    public static final int HISTORY = 0;
    public static final int ACCOUNT = 1;
    public static final int REPOSITORY = 2;

    private Integer type;
    private Object item;
    private Float searchScore = 0.0f;

    public SearchResultItem(int type, Object item, float searchScore) {
        this.type = type;
        this.item = item;
        this.searchScore = searchScore;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Float getSearchScore() {
        return searchScore;
    }

    public void setSearchScore(Float searchScore) {
        this.searchScore = searchScore;
    }

    @Override
    public int compareTo(@NonNull SearchResultItem iw) {
        return getType().compareTo(iw.getType());
    }
}
