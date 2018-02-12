package com.vitalityactive.va.utilities;

import android.annotation.TargetApi;
import android.hardware.fingerprint.FingerprintManager;
import android.os.AsyncTask;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Log;

import com.vitalityactive.va.DeviceSpecificPreferences;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class VAFingerprintManager {

    private static VAFingerprintManager instance;

    private final String FINGERPRINT_KEY_NAME = "fingerPrintStorage";
    private FingerprintManager fingerprintManager;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Cipher cipher;
    private SecretKey key;

    private DeviceSpecificPreferences deviceSpecificPreferences;


    private VAFingerprintManager(){}

    public static VAFingerprintManager getInstance(DeviceSpecificPreferences deviceSpecificPreferences){
        if(instance == null){
            instance = new VAFingerprintManager();
            instance.setDeviceSpecificPreferences(deviceSpecificPreferences);
        }

        return instance;
    }

    public void setDeviceSpecificPreferences(DeviceSpecificPreferences deviceSpecificPreferences){
        this.deviceSpecificPreferences = deviceSpecificPreferences;
    }

    public boolean hasFingerprintChanges(){

        key = deviceSpecificPreferences.getFingerprintSecretKey();

        if(key == null){
            generateKey();
        }

        return !cipherInit();
    }

    public void regenerateKey(){
        key = null;
        generateKey();
        cipherInit();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void generateKey() {

        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }


        try {
            keyStore.load(null);

            KeyGenParameterSpec.Builder builder = new
                    KeyGenParameterSpec.Builder(FINGERPRINT_KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT);

            builder.setBlockModes(KeyProperties.BLOCK_MODE_CBC);
            builder.setUserAuthenticationRequired(true);
            builder.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setInvalidatedByBiometricEnrollment(true);
            }

            keyGenerator.init(builder.build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            if (key == null) {
                keyStore.load(null);
                key = (SecretKey) keyStore.getKey(FINGERPRINT_KEY_NAME,
                        null);
                deviceSpecificPreferences.setFingerprintSecretKey(key);
            }

            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            e.printStackTrace();
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
}
