package com.vitalityactive.va.utilities;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Log;

import com.github.ajalt.reprint.core.AuthenticationFailureReason;
import com.github.ajalt.reprint.core.AuthenticationListener;
import com.github.ajalt.reprint.core.Reprint;
import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.login.FingerprintAlertDialogFragment;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Created by peter.ian.t.betos on 18/12/2017.
 */

public class FingerprintUtility {


    public boolean fingerprintEnabled() {
        // This setting is added for full override of the fingerprint functionality disablement
        // in case you need to disable it (especially for troubleshooting).
        if (!BuildConfig.FINGERPRINT_ENABLED) {
            return false;
        }

        if (!Reprint.hasFingerprintRegistered()) {
            return false;
        }

        if (!Reprint.isHardwarePresent()) {
            return false;
        }
        return true;
    }

    public boolean isFingerprintHasChanged(DeviceSpecificPreferences deviceSpecificPreferences){
        VAFingerprintManager vaFingerprintManager = VAFingerprintManager.getInstance(deviceSpecificPreferences);
        return vaFingerprintManager.hasFingerprintChanges();
    }

    public final void startAuthentication(Context context, final FingerprintAlertDialogFragment diaglog) {
        Reprint.authenticate(new AuthenticationListener() {
            public void onSuccess(int moduleTag) {
                diaglog.fingerPrintRecognized();
            }

            public void onFailure(AuthenticationFailureReason failureReason, boolean fatal,
                                  CharSequence errorMessage, int moduleTag, int errorCode) {
                diaglog.fingerPrintNotRecognized();
            }
        });

    }

    public void regenerateFingerprintKeys(DeviceSpecificPreferences deviceSpecificPreferences){
        VAFingerprintManager vaFingerprintManager = VAFingerprintManager.getInstance(deviceSpecificPreferences);
        vaFingerprintManager.regenerateKey();
    }

}
