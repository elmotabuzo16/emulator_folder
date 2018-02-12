package com.vitalityactive.va.dependencyinjection;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.VitalityActiveApplication;

import java.util.UUID;

public class ModuleCollection {
    public DefaultModule defaultModule;
    public NetworkModule networkModule;
    public PersistenceModule persistenceModule;
    public FlavorSpecificModule flavorSpecificModule;

    public ModuleCollection(VitalityActiveApplication vitalityActiveApplication) {
        defaultModule = new DefaultModule(vitalityActiveApplication);
        networkModule = new NetworkModule(getBaseURL(defaultModule.getSharedPreferences()), UUID.randomUUID().toString());
        persistenceModule = new PersistenceModule(vitalityActiveApplication);
        flavorSpecificModule = new FlavorSpecificModule();
    }

    @NonNull
    private String getBaseURL(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString(DeviceSpecificPreferences.CURRENT_BASE_URL, BuildConfig.TEST_BASE_URL);
    }
}
