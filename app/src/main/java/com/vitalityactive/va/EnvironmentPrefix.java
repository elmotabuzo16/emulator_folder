package com.vitalityactive.va;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.vitalityactive.va.EnvironmentPrefix.CA_TEST;
import static com.vitalityactive.va.EnvironmentPrefix.DEV;
import static com.vitalityactive.va.EnvironmentPrefix.EAGLE;
import static com.vitalityactive.va.EnvironmentPrefix.QA;
import static com.vitalityactive.va.EnvironmentPrefix.QA_CA;
import static com.vitalityactive.va.EnvironmentPrefix.QA_FF;
import static com.vitalityactive.va.EnvironmentPrefix.TEST;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
        DEV, TEST, CA_TEST, QA, QA_CA, EAGLE, QA_FF
})
public @interface EnvironmentPrefix {
    String DEV = "dev--";
    String TEST = "test--";
    String CA_TEST = "ca-test--";
    String QA = "qa--";
    String QA_CA = "qaca--";
    String EAGLE = "eagle--";
    String QA_FF = "qaff--";
}
