package com.vitalityactive.va.dependencyinjection;

import android.content.Context;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.RealmDataStore;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.RealmConfiguration;

@Module
public class PersistenceModule {
    private Context context;

    public PersistenceModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public RealmConfiguration provideDefaultRealmConfiguration(DeviceSpecificPreferences preferences) {
        return new RealmConfiguration.Builder()
                .encryptionKey(preferences.getSecretKey())
                .deleteRealmIfMigrationNeeded()
                .build();
    }

    @Provides
    @Singleton
    @Named(DependencyNames.VHR)
    public RealmConfiguration provideVHRRealmConfiguration(DeviceSpecificPreferences preferences) {
        return new RealmConfiguration.Builder()
                .name("vhr.realm")
                .encryptionKey(preferences.getSecretKey())
                .deleteRealmIfMigrationNeeded()
                .build();
    }
    @Provides
    @Singleton
    @Named(DependencyNames.MWB)
    public RealmConfiguration provideMWBRealmConfiguration(DeviceSpecificPreferences preferences) {
        return new RealmConfiguration.Builder()
                .name("mwb.realm")
                .encryptionKey(preferences.getSecretKey())
                .deleteRealmIfMigrationNeeded()
                .build();
    }
    @Provides
    @Singleton
    @Named(DependencyNames.VNA)
    public RealmConfiguration provideVNARealmConfiguration(DeviceSpecificPreferences preferences) {
        return new RealmConfiguration.Builder()
                .name("vna.realm")
                .encryptionKey(preferences.getSecretKey())
                .deleteRealmIfMigrationNeeded()
                .build();
    }

    @Provides
    @Singleton
    @Named(DependencyNames.PARTNER_JOURNEY)
    public RealmConfiguration providepartnerJourneyRealmConfiguration() {
        return new RealmConfiguration.Builder()
                .name("partner-journey.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
    }

    @Provides
    @Singleton
    public DataStore provideDefaultDataStore(RealmConfiguration realmConfiguration) {
        return new RealmDataStore(realmConfiguration);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.VHR)
    public DataStore provideVHRDataStore(@Named(DependencyNames.VHR) RealmConfiguration realmConfiguration) {
        return new RealmDataStore(realmConfiguration);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.VNA)
    public DataStore provideVNADataStore(@Named(DependencyNames.VNA) RealmConfiguration realmConfiguration) {
        return new RealmDataStore(realmConfiguration);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.PARTNER_JOURNEY)
    public DataStore providePartnerJourneyDataStore(@Named(DependencyNames.PARTNER_JOURNEY) RealmConfiguration realmConfiguration) {
        return new RealmDataStore(realmConfiguration);
    }

    public RealmConfiguration getRealmConfigurationForVitalityAgeRepository() {
        return new RealmConfiguration.Builder().name("vitality_age.realm").deleteRealmIfMigrationNeeded().build();
    }

    private RealmConfiguration getRealmConfigurationForActiveRewardsRepository() {
        String key = "b44fd78f642ed4012400646ba53656629a37fafe3fc3c58cb55c21d9acc707c96e0f9c4b6a90892238dfae55060b15607a65b3a2569beaaecc30a4d5488154b2";

        return new RealmConfiguration.Builder()
                .encryptionKey(hexStringToByteArray(key))
                .name("active_rewards.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }

        return data;
    }
}
