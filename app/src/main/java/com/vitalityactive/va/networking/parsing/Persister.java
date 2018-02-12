package com.vitalityactive.va.networking.parsing;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.Model;

import java.util.ArrayList;
import java.util.List;

public class Persister {
    private final DataStore dataStore;

    public Persister(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public <T extends Model> boolean addModel(T model) {
        return model != null && dataStore.add(model);
    }

    public <T extends Model, U> boolean addModels(List<U> models, InstanceCreator<T, U> instanceCreator) {
        if (models == null) {
            return false;
        }
        List<T> realmModels = getRealmModels(models, instanceCreator);
        return !realmModels.isEmpty() && dataStore.add(realmModels);
    }

    public  <T extends Model, U> boolean addOrUpdateModels(@Nullable List<U> models, @NonNull InstanceCreator<T, U> instanceCreator) {
        return models != null && dataStore.addOrUpdate(getRealmModels(models, instanceCreator));
    }

    @NonNull
    private <T extends Model, U> List<T> getRealmModels(List<U> models, InstanceCreator<T, U> instanceCreator) {
        List<T> realmModels = new ArrayList<>();
        for (U model : models) {
            T realmModel = instanceCreator.create(model);
            if (realmModel != null) {
                realmModels.add(realmModel);
            }
        }
        return realmModels;
    }

    public interface InstanceCreator<T extends Model, U> {
        T create(U model);
    }
}
