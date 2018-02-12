package com.vitalityactive.va.dependencyinjection.mwb;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by christian.j.p.capin on 1/30/2018.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface MWBCaptureScope {
}
