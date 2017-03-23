package com.epam.traing.gitcl.network.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yahor_Fralou on 2/22/2017 12:40 PM.
 */

public class RepoJson {
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("owner")
    private Owner owner;
    @SerializedName("private")
    private boolean privateRepo;
    @SerializedName("fork")
    private boolean fork;
    @SerializedName("stargazers_count")
    private long stargazersCount;
    @SerializedName("forks_count")
    private long forksCount;
    @SerializedName("watchers_count")
    private long watchersCount;
    @SerializedName("language")
    private String language;
    @SerializedName("created_at")
    private String createDate;
    @SerializedName("updated_at")
    private String updateDate;
    @SerializedName("pushed_at")
    private String pushDate;
    @SerializedName("default_branch")
    private String defaultBranch;
    @SerializedName("score")
    private float searchScore;

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

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getPushDate() {
        return pushDate;
    }

    public void setPushDate(String pushDate) {
        this.pushDate = pushDate;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public float getSearchScore() {
        return searchScore;
    }

    public void setSearchScore(float searchScore) {
        this.searchScore = searchScore;
    }

    public class Owner {
        @SerializedName("login")
        private String accountName;

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }
    }
}
