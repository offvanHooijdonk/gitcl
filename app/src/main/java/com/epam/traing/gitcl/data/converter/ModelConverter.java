package com.epam.traing.gitcl.data.converter;

import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.model.RepoModel;
import com.epam.traing.gitcl.network.json.AccountJson;
import com.epam.traing.gitcl.network.json.RepoJson;

/**
 * Created by off on 19.02.2017.
 */

public class ModelConverter {

    public AccountModel toAccountModel(AccountJson json) {
        AccountModel model = new AccountModel();
        model.setAccountName(json.getLogin());
        model.setPersonName(json.getPersonName());
        model.setAvatar(json.getAvatarUrl());
        model.setEmail(json.getEmail());
        return model;
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
        return model;
    }
}
