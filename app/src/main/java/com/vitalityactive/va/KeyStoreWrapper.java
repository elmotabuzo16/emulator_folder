package com.vitalityactive.va;

import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

class KeyStoreWrapper {

    private static final String TAG = KeyStoreWrapper.class.getSimpleName();

    private KeyStore keyStore;
    private static final String RSA_KEY_ALIAS = "RSA_KEY_ALIAS";
    private static final int RSA_KEY_LENGTH = 2048;
    private static final String RSA_CIPHER = "RSA/ECB/PKCS1Padding";

    KeyStoreWrapper(Context context) {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            createNewKeysIfNoneExists(context);
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }
    }

    @SuppressWarnings("deprecation")
    private void createNewKeysIfNoneExists(Context context) {
        try {
            if (!keyStore.containsAlias(RSA_KEY_ALIAS)) {
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                AlgorithmParameterSpec params ;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    params = new KeyGenParameterSpec
                            .Builder(RSA_KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setKeySize(RSA_KEY_LENGTH)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                            .build();
                } else {
                    Calendar start = Calendar.getInstance();
                    Calendar end = Calendar.getInstance();
                    end.add(Calendar.YEAR, 1);

                    params = new android.security.KeyPairGeneratorSpec.Builder(context)
                            .setAlias(RSA_KEY_ALIAS)
                            .setKeySize(RSA_KEY_LENGTH)
                            .setSubject(new X500Principal("CN=" + RSA_KEY_ALIAS))
                            .setSerialNumber(BigInteger.ONE)
                            .setStartDate(start.getTime())
                            .setEndDate(end.getTime())
                            .build();
                }
                generator.initialize(params);
                generator.generateKeyPair();
            }
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }
    }

    String encryptString(String clearText) {
        if (!TextUtils.isEmpty(clearText)) {
            try {
                return encryptBytes(clearText.getBytes("UTF-8"));
            } catch (Exception e) {
                Log.e(TAG, "error", e);
            }
        }
        return clearText;
    }

    String encryptBytes(byte[] clearBytes) {
        return Base64.encodeToString(encrypt(clearBytes), Base64.DEFAULT);
    }

    private byte[] encrypt(byte[] clearBytes) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(RSA_KEY_ALIAS, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

            Cipher cipher = Cipher.getInstance(RSA_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
            cipherOutputStream.write(clearBytes);
            cipherOutputStream.close();
            return outputStream.toByteArray();

        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }
        return new byte[0];
    }

    String decryptString(String cipherText) {
        if (!TextUtils.isEmpty(cipherText)) {
            try {
                byte[] cipherBytes = Base64.decode(cipherText, Base64.DEFAULT);
                byte[] clearBytes = decrypt(cipherBytes);
                return new String(clearBytes, 0, clearBytes.length, "UTF-8");
            } catch (Exception e) {
                Log.e(TAG, "error", e);
            }
        }
        return cipherText;
    }

    byte[] decrypt(String cipherText) {
        return decrypt(Base64.decode(cipherText, Base64.DEFAULT));
    }

    private byte[] decrypt(byte[] cipherBytes) {
        try {
            KeyStore.Entry entry = keyStore.getEntry(RSA_KEY_ALIAS, null);
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) entry;

            Cipher output = Cipher.getInstance(RSA_CIPHER);
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());

            CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(cipherBytes), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte) nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i);
            }
            return bytes;

        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }
        return new byte[0];
    }

    void deleteKey() {
        try {
            keyStore.deleteEntry(RSA_KEY_ALIAS);
        } catch (KeyStoreException e) {
            Log.e(TAG, "error", e);
        }
    }

}
