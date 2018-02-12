package com.vitalityactive.va;

import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.constants.ProductFeatureLinkType;
import com.vitalityactive.va.login.LoginRepositoryImpl;
import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.onboarding.LoginRepository;
import com.vitalityactive.va.register.entity.Username;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

@RepositoryTests
public class InsurerConfigurationRepositoryTest extends RepositoryTestBase {
    private static final String TAG = "InsurerConfigRepoTest";
    private InsurerConfigurationRepository repository;
    private List<LoginServiceResponse.ProductFeature> featureList;

    @Override
    public void setUp() throws IOException {
        super.setUp();
        setupLoginResponse();
        repository = new InsurerConfigurationRepository(dataStore);
    }

    private void setupLoginResponse() throws IOException {
        LoginRepository loginRepository = new LoginRepositoryImpl(dataStore, new AppConfigRepository(dataStore, InstrumentationRegistry.getTargetContext()));
        LoginServiceResponse response = getResponse(LoginServiceResponse.class, "login/Login_successful.json");
        featureList = response.vitalityMembership.get(0).membershipProducts.get(0).productFeatures;
        boolean persisted = loginRepository.persistLoginResponse(response, new Username("someone@somewhere.com"));
        assertTrue(persisted);
    }

    @Test
    public void getProductFeatureTypeKeyFromEventTypeKey_worksForAllFeatureItems() {
        for (LoginServiceResponse.ProductFeature feature : featureList) {
            if (feature == null || feature.featureLinks == null) {
                continue;
            }

            for (LoginServiceResponse.FeatureLink featureLink : feature.featureLinks) {
                if (featureLink.typeKey == ProductFeatureLinkType._EVENTTYPE) {
                    int actual = repository.getProductFeatureTypeKeyFromEventTypeKey(featureLink.linkedKey);
                    int expected = feature.type;
                    String message = String.format(Locale.getDefault(), "feature %d", featureLink.linkedKey);
                    assertEquals("failed: " + message, expected, actual);
                    Log.d(TAG, message + "=" + actual);
                }
            }
        }
    }

    @Test
    public void getProductFeatureTypeKeyFromEventTypeKey_0_if_not_found() {
        int actual = repository.getProductFeatureTypeKeyFromEventTypeKey(-1);
        assertEquals(0, actual);
    }

    @Test
    public void getProductFeatureTypeKeyFromEventTypeKey_sanity_checks() {
        assertEquals(1, repository.getProductFeatureTypeKeyFromEventTypeKey(88));
        assertEquals(2, repository.getProductFeatureTypeKeyFromEventTypeKey(87));
        assertEquals(10, repository.getProductFeatureTypeKeyFromEventTypeKey(135));
    }

    @Test
    public void getProductFeatureTypeKeyFromHealthAttributeTypeKey_worksForAllFeatureItems() {
        for (LoginServiceResponse.ProductFeature feature : featureList) {
            if (feature == null || feature.featureLinks == null) {
                continue;
            }

            for (LoginServiceResponse.FeatureLink featureLink : feature.featureLinks) {
                if (featureLink.typeKey == ProductFeatureLinkType._HEALTHATTRIBUTETYPE) {
                    int actual = repository.getProductFeatureTypeKeyFromHealthAttributeTypeKey(featureLink.linkedKey);
                    int expected = feature.type;
                    String message = String.format(Locale.getDefault(), "feature %d", featureLink.linkedKey);
                    assertEquals("failed: " + message, expected, actual);
                    Log.d(TAG, message + " <=> health key " + actual);
                }
            }
        }
    }

    @Test
    public void getProductFeatureTypeKeyFromHealthAttributeTypeKey_0_if_not_found() {
        int actual = repository.getProductFeatureTypeKeyFromHealthAttributeTypeKey(-1);
        assertEquals(0, actual);
    }

    @Test
    public void getProductFeatureTypeKeyFromHealthAttributeTypeKey_sanity_checks() {
        assertEquals(1, repository.getProductFeatureTypeKeyFromHealthAttributeTypeKey(11));
        assertEquals(2, repository.getProductFeatureTypeKeyFromHealthAttributeTypeKey(12));
        assertEquals(63, repository.getProductFeatureTypeKeyFromHealthAttributeTypeKey(40));
    }
}
