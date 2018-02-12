package com.vitalityactive.va;

import android.support.annotation.CallSuper;
import android.support.test.InstrumentationRegistry;

import com.google.gson.Gson;
import com.vitalityactive.va.dependencyinjection.NetworkModule;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.RealmDataStore;
import com.vitalityactive.va.testutilities.ModelVerifier;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.io.IOException;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;

public class RepositoryTestBase {
    @Rule
    public TestName testName = new TestName();

    protected RealmConfiguration realmConfiguration;
    protected DataStore dataStore;
    private Realm realm;

    private static Gson getGson() {
        return new NetworkModule(BuildConfig.TEST_BASE_URL, UUID.randomUUID().toString()).provideGson();
    }

    public static <T> T getResponse(Class<T> responseClass, String path) throws IOException {
        return getGson().fromJson(new TestUtilities().readFile(path), responseClass);
    }

    @Before
    @CallSuper
    public void setUp() throws IOException {
        Realm.init(InstrumentationRegistry.getTargetContext());

        realmConfiguration = new RealmConfiguration.Builder()
                // Note - reference counting on in-memory Realms is broken at the moment. If the counter reaches 0, the next call
                // to getInstance() will create an entirely new Realm so trying to find anything in it will fail.
                .inMemory()
                .name(testName.getMethodName())
                .build();

        realm = Realm.getInstance(realmConfiguration);

        dataStore = new RealmDataStore(realmConfiguration);
    }

    @After
    @CallSuper
    public void tearDown() {
        realm.close();
    }

    protected Realm getRealm() {
        return realm;
    }

    protected <T extends RealmModel> void assertModelIsNotCreated(Class<T> modelClass) {
        new ModelVerifier<>(modelClass, getRealm()).assertModelIsNotCreated();
    }

    protected <T extends RealmModel> void assertOneModelIsCreated(Class<T> modelClass) {
        new ModelVerifier<>(modelClass, getRealm()).assertOneModel();
    }

    protected <T extends RealmModel> void assertModelCount(Class<T> modelClass, int expectedCount) {
        new ModelVerifier<>(modelClass, getRealm()).assertNumberOfModels(expectedCount);
    }
}
