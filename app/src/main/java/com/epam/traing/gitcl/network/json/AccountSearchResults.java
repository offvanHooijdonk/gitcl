package com.epam.traing.gitcl.network.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yahor_Fralou on 3/23/2017 3:58 PM.
 */

public class AccountSearchResults {
    @SerializedName("total_count")
    private long totalCount;
    @SerializedName("incomplete_results")
    private boolean incompleteResults;
    @SerializedName("items")
    private List<AccountJson> items;

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public void setIncompleteResults(boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }

    public List<AccountJson> getItems() {
        return items;
    }

    public void setItems(List<AccountJson> items) {
        this.items = items;
    }
}
