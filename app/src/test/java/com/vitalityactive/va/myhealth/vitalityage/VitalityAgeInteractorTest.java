package com.vitalityactive.va.myhealth.vitalityage;

import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.myhealth.dto.MyHealthRepository;
import com.vitalityactive.va.myhealth.dto.VitalityAgeRepository;
import com.vitalityactive.va.myhealth.entity.VitalityAge;
import com.vitalityactive.va.persistence.DataStore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VitalityAgeInteractorTest extends BaseTest {

    VitalityAgeInteractorImpl vitalityAgeInteractorImpl;
    VitalityAge vitalityAge = null;
    @Mock
    VitalityAgeRepository mockVitalityAgeRepository;
    @Mock
    DataStore mockDataStore;
    @Mock
    InsurerConfigurationRepository mockInsurerConfigurationRepository;
    @Mock
    EventDispatcher mockEventDispatcher;
    @Mock
    MyHealthRepository mockMyHealthRepository;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        vitalityAge = new VitalityAge.Builder()
                .age("34")
                .effectiveType(2)
                .feedbackTitle("")
                .feedbackContent("")
                .build();
        when(mockVitalityAgeRepository.getVitalityAge()).thenReturn(vitalityAge);
        vitalityAgeInteractorImpl = new VitalityAgeInteractorImpl(mockVitalityAgeRepository,mockMyHealthRepository);
    }

    @Test
    public void vitality_age_is_fetched_from_repo() {
        vitalityAgeInteractorImpl.getPersistedVitalityAge();
        verify(mockVitalityAgeRepository, times(1)).getVitalityAge();
    }
}
