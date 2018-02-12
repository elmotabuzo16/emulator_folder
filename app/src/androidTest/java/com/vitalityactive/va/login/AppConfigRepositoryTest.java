package com.vitalityactive.va.login;

import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.networking.model.AppConfigResponse;
import com.vitalityactive.va.persistence.models.AppConfigFeature;
import com.vitalityactive.va.persistence.models.AppConfigFeatureParameter;
import com.vitalityactive.va.persistence.models.AppConfigVersion;
import com.vitalityactive.va.testutilities.ModelVerifier;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
@RepositoryTests
public class AppConfigRepositoryTest extends RepositoryTestBase {
    private AppConfigRepository repository;

    public static void verifyAppConfigFeatures(Realm realm) throws Exception {
        new AppConfigFeatureModelVerifier(AppConfigFeature.class, realm, 7, new ModelVerifier.Verifier<AppConfigFeature>() {
            @Override
            public void verifyModel(AppConfigFeature model, int index) throws Exception {
                if (index == 0) {
                    assertEquals("liferayGroupId", model.getFeatureParameters().get(2).getName());
                    assertEquals("34799", model.getFeatureParameters().get(2).getValue());
                    assertEquals("1", model.getFeatureParameters().get(2).getFeatureType());
                } else if (index == 4) {
                    assertEquals("nonsmokersdeclaration_en_US", model.getFeatureParameters().get(0).getName());
                    assertEquals("nonsmokersdeclaration_en_US.json", model.getFeatureParameters().get(0).getValue());
                    assertEquals("5", model.getType());
                } else if (index == 6) {
                    assertEquals("vhc_en_US", model.getFeatureParameters().get(0).getName());
                    assertEquals("vhc_en_US.json", model.getFeatureParameters().get(0).getValue());
                    assertEquals("7", model.getType());
                }
            }
        });
    }

    public static void verifyAppConfigVersion(Realm realm) throws Exception {
        new ModelVerifier<>(AppConfigVersion.class, realm, 1, new ModelVerifier.Verifier<AppConfigVersion>() {
            @Override
            public void verifyModel(AppConfigVersion model, int index) {
                assertEquals("2017-12-18", model.getEffectiveFrom());
                assertEquals("9999-01-01", model.getEffectiveTo());
                assertEquals("1.0", model.getReleaseVersion());
            }
        });
    }

    public void setUp() throws IOException {
        super.setUp();
        repository = new AppConfigRepository(dataStore, InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void app_config_response_is_persisted_correctly() throws Exception {
        AppConfigResponse response = getResponse(AppConfigResponse.class, "app_config/AppConfig_successful.json");
        boolean persisted = repository.persistAppConfig(response.application);

        assertTrue(persisted);
        verifyAppConfigFeatures(getRealm());
        verifyAppConfigVersion(getRealm());
    }

    @Test
    public void app_config_is_updated_instead_of_duplicated() throws Exception {
        AppConfigResponse response = getResponse(AppConfigResponse.class, "app_config/AppConfig_successful.json");
        repository.persistAppConfig(response.application);

        AppConfigResponse response1 = getResponse(AppConfigResponse.class, "app_config/AppConfig_successful_fewer_features_and_feature_parameters.json");
        repository.persistAppConfig(response1.application);

        verifyAppConfigFeaturesAreNotDuplicatedAndOrphansAreRemoved();
        verifyAppConfigVersionIsNotDuplicated();
        verifyOrphanedFeatureParametersAreRemoved();
    }

    private void verifyAppConfigVersionIsNotDuplicated() {
        new ModelVerifier<>(AppConfigVersion.class, getRealm()).assertNumberOfModels(1);
    }

    private void verifyAppConfigFeaturesAreNotDuplicatedAndOrphansAreRemoved() {
        new ModelVerifier<>(AppConfigFeature.class, getRealm()).assertNumberOfModels(6);
    }

    private void verifyOrphanedFeatureParametersAreRemoved() {
        new ModelVerifier<>(AppConfigFeatureParameter.class, getRealm()).assertNumberOfModels(8);
    }

    private static class AppConfigFeatureModelVerifier extends ModelVerifier<AppConfigFeature> {
        AppConfigFeatureModelVerifier(Class<AppConfigFeature> modelClass, Realm realm, int numberOfModels, Verifier<AppConfigFeature> verifier) throws Exception {
            super(modelClass, realm, numberOfModels, verifier);
        }

        @NonNull
        @Override
        protected RealmResults<AppConfigFeature> getModels() {
            return super.getModels().sort("type");
        }
    }
}
