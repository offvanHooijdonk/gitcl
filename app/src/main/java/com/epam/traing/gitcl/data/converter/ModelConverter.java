package com.epam.traing.gitcl.data.converter;

import android.util.Log;

import com.epam.traing.gitcl.app.Application;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.model.RepoModel;
import com.epam.traing.gitcl.network.json.AccountJson;
import com.epam.traing.gitcl.network.json.RepoJson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by off on 19.02.2017.
 */

public class ModelConverter {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT);

    public AccountModel toAccountModel(AccountJson json) {
        AccountModel model = new AccountModel();
        model.setAccountName(json.getLogin());
        model.setPersonName(json.getPersonName());
        model.setAvatar(json.getAvatarUrl());
        model.setEmail(json.getEmail());
        model.setLocation(json.getLocation());
        model.setSearchScore(json.getSearchScore());
        return model;
    }

    public List<AccountModel> toAccountModelList(List<AccountJson> jsonList) {
        List<AccountModel> models = new ArrayList<>();
        for (AccountJson json : jsonList) {
            models.add(toAccountModel(json));
        }

        return models;
    }

    public RepoModel toRepoModel(RepoJson json) {
        RepoModel model = new RepoModel();
        model.setId(json.getId());
        model.setName(json.getName());
        model.setOwnerName(json.getOwner().getAccountName());
        model.setLanguage(json.getLanguage());
        model.setPrivateRepo(json.isPrivateRepo());
        model.setFork(json.isFork());
        model.setStargazersCount(json.getStargazersCount());
        model.setForksCount(json.getForksCount());
        model.setWatchersCount(json.getWatchersCount());
        model.setCreateDate(parseDateString(json.getCreateDate()));
        model.setUpdateDate(parseDateString(json.getUpdateDate()));
        model.setPushDate(parseDateString(json.getPushDate()));
        model.setDefaultBranch(json.getDefaultBranch());
        model.setSearchScore(json.getSearchScore());
        return model;
    }

    public List<RepoModel> toRepoModelList(List<RepoJson> jsonList) {
        List<RepoModel> models = new ArrayList<>();

        for (RepoJson json : jsonList) {
            models.add(toRepoModel(json));
        }

        return models;
    }

    private long parseDateString(String dateString) {
        if (dateString == null) return 0;
        long timeMillis;
        dateString = dateString.replace("T", " ");
        dateString = dateString.replace("Z", "");
        try {
            timeMillis = sdf.parse(dateString).getTime();
        } catch (ParseException e) {
            Log.w(Application.LOG, "Error parsing feed date", e);
            timeMillis = 0;
        }

        return timeMillis;
    }
}
