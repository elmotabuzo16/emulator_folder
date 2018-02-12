package com.vitalityactive.va.testutilities.dependencyinjection;

import android.content.Context;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.MockJUnitRunner;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.dependencyinjection.PersistenceModule;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.persistence.RealmDataStore;
import com.vitalityactive.va.utilities.DataStoreClearer;

import java.util.List;

import javax.inject.Named;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class PersistenceWithInMemoryRealmModule extends PersistenceModule {
    public final InMemoryRealm defaultRealm;
    public final InMemoryRealm vhrRealm;
    public final InMemoryRealm vnaRealm;

    public PersistenceWithInMemoryRealmModule(Context context) {
        super(context);
        Realm.init(MockJUnitRunner.getInstance().getTargetContext());
        defaultRealm = new InMemoryRealm("PersistenceWithInMemoryRealmModule");
        vhrRealm = new InMemoryRealm("vhr");
        vnaRealm = new InMemoryRealm("vna");
    }

    @Override
    public RealmConfiguration provideDefaultRealmConfiguration(DeviceSpecificPreferences preferences) {
        return defaultRealm.configuration;
    }

    @Override
    public DataStore provideDefaultDataStore(RealmConfiguration realmConfiguration) {
        return defaultRealm.dataStore;
    }

    @Override
    public DataStore provideVHRDataStore(@Named(DependencyNames.VHR) RealmConfiguration realmConfiguration) {
        return vhrRealm.dataStore;
    }

    @Override
    public DataStore provideVNADataStore(@Named(DependencyNames.VNA) RealmConfiguration realmConfiguration) {
        return vnaRealm.dataStore;
    }

    public void closeRealm() {
        defaultRealm.close();
        vhrRealm.close();
        vnaRealm.close();
    }

    public RealmDataStore getDataStore() {
        return defaultRealm.dataStore;
    }

    public void addData(List<Model> models) {
        defaultRealm.dataStore.add(models);
    }

    public void clearAllDataStores() {
        DataStoreClearer.clear(MockJUnitRunner.getInstance().getApplication());
    }

    public static class InMemoryRealm {
        public final RealmDataStore dataStore;
        private final RealmConfiguration configuration;
        private final Realm realm;

        InMemoryRealm(String filename) {
            configuration = new RealmConfiguration.Builder()
                    // Note - reference counting on in-memory Realms is broken at the moment. If the counter reaches 0, the next call
                    // to getInstance() will create an entirely new Realm so trying to find anything in it will fail.
                    .inMemory()
                    .name(filename)
                    .build();
            realm = Realm.getInstance(configuration);
            dataStore = new RealmDataStore(configuration);
        }

        private void close() {
            realm.close();
        }
    }
}
