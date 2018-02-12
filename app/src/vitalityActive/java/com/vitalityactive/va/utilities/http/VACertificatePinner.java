package com.vitalityactive.va.utilities.http;

import com.vitalityactive.va.BuildConfig;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;

/**
 * Created by peter.ian.t.betos on 16/01/2018.
 */

public class VACertificatePinner {

    public static void enableCertificatePinning(OkHttpClient.Builder builder) {
        CertificatePinner certificatePinner = new CertificatePinner.Builder()
                .add(BuildConfig.DEV_BASE_HOSTNAME, BuildConfig.DEV_CERT_PIN)
                .add(BuildConfig.TEST_BASE_HOSTNAME,  BuildConfig.TEST_CERT_PIN)
                .add(BuildConfig.QA_BASE_HOSTNAME, BuildConfig.QA_CERT_PIN)
                .build();
        builder.certificatePinner(certificatePinner);
    }
}
