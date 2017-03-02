package com.epam.traing.gitcl.db.model;

import com.epam.traing.gitcl.db.tables.RepoTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by Yahor_Fralou on 2/22/2017 3:56 PM.
 */

@StorIOSQLiteType(table = RepoTable.TABLE)
public class RepoModel {
    @StorIOSQLiteColumn(name = RepoTable.COLUMN_ID, key = true)
    long id;
    @StorIOSQLiteColumn(name = RepoTable.COLUMN_NAME)
    String name;
    @StorIOSQLiteColumn(name = RepoTable.COLUMN_OWNER_NAME)
    String ownerName;
    @StorIOSQLiteColumn(name = RepoTable.COLUMN_IS_PRIVATE)
    boolean privateRepo;
    @StorIOSQLiteColumn(name = RepoTable.COLUMN_IS_FORK)
    boolean fork;
    @StorIOSQLiteColumn(name = RepoTable.COLUMN_STARGAZERS_COUNT)
    long stargazersCount;
    @StorIOSQLiteColumn(name = RepoTable.COLUMN_WATCHERS_COUNT)
    long watchersCount;
    @StorIOSQLiteColumn(name = RepoTable.COLUMN_FORKS_COUNT)
    long forksCount;
    @StorIOSQLiteColumn(name = RepoTable.COLUMN_LANGUAGE)
    String language;
    @StorIOSQLiteColumn(name = RepoTable.COLUMN_CREATE_DATE)
    long createDate;
    @StorIOSQLiteColumn(name = RepoTable.COLUMN_UPDATE_DATE)
    long updateDate;
    @StorIOSQLiteColumn(name = RepoTable.COLUMN_PUSH_DATE)
    long pushDate;
    @StorIOSQLiteColumn(name = RepoTable.COLUMN_VERBOSE_UPDATE_DATE)
    long verboseUpdateDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public boolean isPrivateRepo() {
        return privateRepo;
    }

    public void setPrivateRepo(boolean privateRepo) {
        this.privateRepo = privateRepo;
    }

    public boolean isFork() {
        return fork;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }

    public long getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(long stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public long getWatchersCount() {
        return watchersCount;
    }

    public void setWatchersCount(long watchersCount) {
        this.watchersCount = watchersCount;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public long getForksCount() {
        return forksCount;
    }

    public void setForksCount(long forksCount) {
        this.forksCount = forksCount;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public long getPushDate() {
        return pushDate;
    }

    public void setPushDate(long pushDate) {
        this.pushDate = pushDate;
    }

    public long getVerboseUpdateDate() {
        return verboseUpdateDate;
    }

    public void setVerboseUpdateDate(long verboseUpdateDate) {
        this.verboseUpdateDate = verboseUpdateDate;
    }
}
