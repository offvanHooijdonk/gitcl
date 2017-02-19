package com.epam.traing.gitcl.data.converter;

import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.network.json.AccountJson;

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

}
