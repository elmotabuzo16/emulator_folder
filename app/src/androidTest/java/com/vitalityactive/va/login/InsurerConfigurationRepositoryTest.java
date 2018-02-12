package com.vitalityactive.va.login;

import android.support.test.InstrumentationRegistry;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.register.entity.Username;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;

import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RepositoryTests
public class InsurerConfigurationRepositoryTest extends RepositoryTestBase {
    private InsurerConfigurationRepository repository;

    @Override
    public void setUp() throws IOException {
        super.setUp();
        repository = new InsurerConfigurationRepository(dataStore);
    }

    @Test
    public void get_configured_event_type_keys() throws Exception {
        persistLoginResponse();
        List<Integer> eventTypes = repository.getConfiguredEventTypeKeys();

        Collections.sort(eventTypes, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });

        assertEquals(Integer.valueOf(87), eventTypes.get(0));
        assertEquals(Integer.valueOf(135), eventTypes.get(13));
    }

    @Test
    public void get_blood_pressure_event_type_key() throws Exception {
        persistLoginResponse();

        assertEquals(EventType._BLOODPRESSURE, repository.getBloodPressureEventTypeKey());
    }

    private void persistLoginResponse() throws java.io.IOException {
        new LoginRepositoryImpl(dataStore, new AppConfigRepository(dataStore, InstrumentationRegistry.getTargetContext())).persistLoginResponse(getResponse(LoginServiceResponse.class, "login/Login_successful.json"), new Username("frikkie@poggenpoel.com"));
    }
}
