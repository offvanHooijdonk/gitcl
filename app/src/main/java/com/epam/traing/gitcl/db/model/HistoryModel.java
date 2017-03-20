package com.epam.traing.gitcl.db.model;

import com.epam.traing.gitcl.db.tables.HistoryTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by Yahor_Fralou on 3/20/2017 6:15 PM.
 */

@StorIOSQLiteType(table = HistoryTable.TABLE)
public class HistoryModel {

    @StorIOSQLiteColumn(name = HistoryTable.COLUMN_TEXT, key = true)
    String text;
    @StorIOSQLiteColumn(name = HistoryTable.COLUMN_SEARCH_DATE)
    long searchDate;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(long searchDate) {
        this.searchDate = searchDate;
    }
}
