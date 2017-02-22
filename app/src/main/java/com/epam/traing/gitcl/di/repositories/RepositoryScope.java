package com.epam.traing.gitcl.di.repositories;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Yahor_Fralou on 2/22/2017 12:04 PM.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface RepositoryScope {
}
