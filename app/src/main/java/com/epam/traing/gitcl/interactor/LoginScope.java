package com.epam.traing.gitcl.interactor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by off on 28.01.2017.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginScope {
}
