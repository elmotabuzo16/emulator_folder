package com.vitalityactive.va.dependencyinjection.pns;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PNSCaptureScope {
}
