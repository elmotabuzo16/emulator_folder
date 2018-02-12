package com.vitalityactive.va.utilities;

import com.vitalityactive.va.VitalityActiveApplication;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.persistence.DataStore;

import javax.inject.Inject;
import javax.inject.Named;

public class DataStoreClearer {
    @Inject
    DataStore dataStore;
    @Inject
    @Named(DependencyNames.VHR)
    DataStore vhrDataStore;
    @Inject
    @Named(DependencyNames.VNA)
    DataStore vnaDataStore;

    public DataStoreClearer(VitalityActiveApplication application) {
        application.getDependencyInjector().inject(this);
    }

    public static void clear(VitalityActiveApplication application) {
        new DataStoreClearer(application).clearAll();
    }

    public void clearAll() {
        dataStore.clear();
        vhrDataStore.clear();
        vnaDataStore.clear();
    }
}
