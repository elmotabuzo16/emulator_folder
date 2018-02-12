package com.vitalityactive.va;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DeviceSpecificPreferencesTest {

    private static final java.lang.String ENCRYPTED_KEY = "W06PYvRj7sNKKa/.../UKe9H5b3xO7uPvOHSOadNlTCA==";

    private KeyStoreWrapper keyStoreWrapper;
    private SharedPreferences sharedPreferences;

    private DeviceSpecificPreferences deviceSpecificPreferences;

    @Before
    public void setUp() {
        sharedPreferences = mock(SharedPreferences.class);
        keyStoreWrapper = mock(KeyStoreWrapper.class);

        deviceSpecificPreferences = spy(new DeviceSpecificPreferences(sharedPreferences, keyStoreWrapper));
    }

    @Test
    public void should_create_new_key_if_none_exists() throws Exception {
        when(sharedPreferences.contains(anyString())).thenReturn(false);
        when(keyStoreWrapper.encryptBytes(Matchers.<byte[]>any())).thenReturn(ENCRYPTED_KEY);
        doNothing().when(deviceSpecificPreferences).setSharedPreference(anyString(), anyString());
        ArgumentCaptor<String> stringArgCaptor = ArgumentCaptor.forClass(String.class);

        assertNotNull(deviceSpecificPreferences.getSecretKey());

        verify(sharedPreferences).contains(anyString());
        verify(keyStoreWrapper).encryptBytes(Matchers.<byte[]>any());
        verify(deviceSpecificPreferences).setSharedPreference(stringArgCaptor.capture(), stringArgCaptor.capture());
        assertSame(ENCRYPTED_KEY, stringArgCaptor.getAllValues().get(1));
    }

    @Test
    public void should_return_existing_key_if_available() throws Exception {
        byte[] keyBytes = new byte[0];
        when(sharedPreferences.contains(anyString())).thenReturn(true);
        when(sharedPreferences.getString(anyString(), anyString())).thenReturn(ENCRYPTED_KEY);
        when(keyStoreWrapper.decrypt(anyString())).thenReturn(keyBytes);

        assertSame(keyBytes, deviceSpecificPreferences.getSecretKey());

        verify(sharedPreferences).contains(anyString());
        verify(sharedPreferences).getString(anyString(), anyString());
        verify(keyStoreWrapper).decrypt(ENCRYPTED_KEY);
    }

}