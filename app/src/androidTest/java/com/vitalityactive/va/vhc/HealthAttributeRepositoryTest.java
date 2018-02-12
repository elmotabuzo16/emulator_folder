package com.vitalityactive.va.vhc;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.constants.ProductFeatureType;
import com.vitalityactive.va.login.LoginRepositoryImpl;
import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.models.vhc.HealthAttributeTypeValidValues;
import com.vitalityactive.va.register.entity.Username;
import com.vitalityactive.va.testutilities.ModelVerifier;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;
import com.vitalityactive.va.vhc.dto.HealthAttributeDTO;
import com.vitalityactive.va.vhc.dto.HealthAttributeGroupDTO;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepository;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepositoryImpl;
import com.vitalityactive.va.vhc.service.HealthAttributeResponse;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
@RepositoryTests
public class HealthAttributeRepositoryTest extends RepositoryTestBase {
    private HealthAttributeRepository repository;

    @Override
    public void setUp() throws IOException {
        super.setUp();
        repository = new HealthAttributeRepositoryImpl(dataStore, new InsurerConfigurationRepository(dataStore), new MockVHCContent());
    }

    @Test
    public void urinary_protein_is_persisted_correctly() throws Exception {
        persistLoginResponse();
        persistVHCLandingResponse("vhc/vhc_landing_urinary_protein_response.json");

        new ModelVerifier<>(HealthAttributeTypeValidValues.class, getRealm(), 1, new ModelVerifier.Verifier<HealthAttributeTypeValidValues>() {
            @Override
            public void verifyModel(HealthAttributeTypeValidValues model, int index) throws Exception {
                assertEquals(6, model.getValidOptions().size());
                assertEquals("+-", model.getValidOptions().get(1).getValue());
                assertEquals("++++", model.getValidOptions().get(5).getValue());
            }
        });
    }

    @Test
    public void attribute_group_is_healthy_if_at_least_one_attribute_is_healthy() throws IOException {
        persistLoginResponse();
        persistVHCLandingResponse("vhc/vhc_landing_cholesterol_response_healthy.json");

        List<HealthAttributeGroupDTO> healthAttributeGroups = getHealthAttributeGroupDTOs();

        assertTrue("Group unhealthy, with one healthy attributes", healthAttributeGroups.get(0).inHealthyRange());
    }

    @Test
    public void attribute_group_is_healthy_if_all_attributes_are_unhealthy() throws IOException {
        persistLoginResponse();
        persistVHCLandingResponse("vhc/vhc_landing_cholesterol_response_unhealthy.json");

        List<HealthAttributeGroupDTO> healthAttributeGroups = getHealthAttributeGroupDTOs();

        assertFalse("Group healthy, with all unhealthy attributes", healthAttributeGroups.get(0).inHealthyRange());
    }

    @Test
    public void attribute_group_contains_max_of_attribute_potential_points() throws IOException {
        persistLoginResponse();
        persistVHCLandingResponse("vhc/vhc_landing_cholesterol_response_healthy.json");

        List<HealthAttributeGroupDTO> healthAttributeGroups = getHealthAttributeGroupDTOs();

        assertTrue("Group did not contain max potential points of all attributes", healthAttributeGroups.get(0).getMaxPotentialPoints() == 2000);
    }

    @Test
    public void health_attribute_shows_correct_points_message() throws IOException {
        persistLoginResponse();

        persistVHCLandingResponse("vhc/vhc_detail_points.json");
        HealthAttributeDTO healthAttribute = repository.getHealthAttribute(EventType._LDLCHOLESTROL, ProductFeatureType._VHCCHOLESTEROL);
        assertEquals("Max points", healthAttribute.getPointsStatus());

        healthAttribute = repository.getHealthAttribute(EventType._WAISTCIRCUM, ProductFeatureType._VHCWAISTCIRCUM);
        assertEquals("Earn 500", healthAttribute.getPointsStatus());

        healthAttribute = repository.getHealthAttribute(EventType._HEIGHTCAPTURED, ProductFeatureType._VHCBMI);
        assertEquals("Response reason", healthAttribute.getPointsStatus());

    }

    private List<HealthAttributeGroupDTO> getHealthAttributeGroupDTOs() {
        List<Integer> productFeatures = new ArrayList<>();
        productFeatures.add(ProductFeatureType._VHCCHOLESTEROL);

        return repository.getHealthAttributeGroups(productFeatures);
    }

    private void persistVHCLandingResponse(String path) throws java.io.IOException {
        HealthAttributeResponse response = getResponse(HealthAttributeResponse.class, path);
        repository.persistHealthAttributeResponse(response);
    }

    private void persistLoginResponse() throws java.io.IOException {
        new LoginRepositoryImpl(dataStore, new AppConfigRepository(dataStore, InstrumentationRegistry.getTargetContext()))
                .persistLoginResponse(getResponse(LoginServiceResponse.class, "login/Login_successful.json"), new Username("Frikkie@Poggenpoel.com"));
    }
}
