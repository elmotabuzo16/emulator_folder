package com.vitalityactive.va.testutilities;

import android.support.annotation.NonNull;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;

import static org.junit.Assert.assertEquals;

public class ModelVerifier<T extends RealmModel> {

    private final Class<T> modelClass;
    private Realm realm;

    public ModelVerifier(Class<T> modelClass, Realm realm) {
        this.modelClass = modelClass;
        this.realm = realm;
    }

    public ModelVerifier(Class<T> modelClass, Realm realm, int numberOfModels, Verifier<T> verifier) throws Exception {
        this(modelClass, realm);
        assertNumberOfModels(numberOfModels);
        int index = 0;
        for (T model : getModels()) {
            verifier.verifyModel(model, index++);
        }
    }

    public void assertOneModel() {
        assertNumberOfModels(1);
    }

    public void assertModelIsNotCreated() {
        assertNumberOfModels(0);
    }

    public void assertNumberOfModels(int expected) {
        assertEquals(expected, getModels().size());
    }

    @NonNull
    protected RealmResults<T> getModels() {
        return realm.where(modelClass).findAll();
    }

    public T getModel() {
        return getModels().first();
    }

    public interface Verifier<T> {
        void verifyModel(T model, int index) throws Exception;
    }
}
