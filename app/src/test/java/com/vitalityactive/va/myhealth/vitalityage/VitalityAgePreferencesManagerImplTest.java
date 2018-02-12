package com.vitalityactive.va.myhealth.vitalityage;


import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.InsurerConfigurationRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VitalityAgePreferencesManagerImplTest extends BaseTest {

    VitalityAgePreferencesManagerImpl vitalityAgePreferencesManagerImpl;
    Calendar today = null;
    Calendar nextYear = null;
    @Mock
    private DeviceSpecificPreferences deviceSpecificPreferences;
    @Mock
    private InsurerConfigurationRepository insurerConfigurationRepository;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setDates();
        when(insurerConfigurationRepository.getCurrentMembershipPeriodStart()).thenReturn(today.getTime());
        when(insurerConfigurationRepository.getCurrentMembershipPeriodEnd()).thenReturn(nextYear.getTime());
        vitalityAgePreferencesManagerImpl = new VitalityAgePreferencesManagerImpl(deviceSpecificPreferences, insurerConfigurationRepository);
    }

    @Test
    public void current_membership_period_not_altered() {
        vitalityAgePreferencesManagerImpl.onLogin();
        verify(deviceSpecificPreferences, times(1)).getCurrentUserMembershipPeriod();
        verify(deviceSpecificPreferences, times(0)).setCurrentUserMembershipPeriod(anyString(), anyString());
    }

    @Test
    public void does_not_reset_on_same_membership_period() {
        Set<String> membershipDates = new HashSet<>(Arrays.asList(String.valueOf(today.getTime().getTime()), String.valueOf(nextYear.getTime().getTime())));
        when(deviceSpecificPreferences.getCurrentUserMembershipPeriod()).thenReturn(membershipDates);
        vitalityAgePreferencesManagerImpl.onLogin();
        verify(deviceSpecificPreferences, times(1)).getCurrentUserMembershipPeriod();
        verify(deviceSpecificPreferences, times(0)).setCurrentUserMembershipPeriod(anyString(), anyString());
        verify(deviceSpecificPreferences, times(0)).removeSharedPreference(anyString());
    }

    @Test
    public void resets_on_new_membership_period() {
        Set<String> membershipDates = new HashSet<>(Arrays.asList(String.valueOf(today.getTime().getTime()), String.valueOf(nextYear.getTime().getTime())));
        when(deviceSpecificPreferences.getCurrentUserMembershipPeriod()).thenReturn(membershipDates);
        today = nextYear;
        nextYear.add(Calendar.YEAR, 1);
        when(insurerConfigurationRepository.getCurrentMembershipPeriodStart()).thenReturn(today.getTime());
        when(insurerConfigurationRepository.getCurrentMembershipPeriodEnd()).thenReturn(nextYear.getTime());
        vitalityAgePreferencesManagerImpl.onLogin();
        verify(deviceSpecificPreferences, times(1)).getCurrentUserMembershipPeriod();
        verify(deviceSpecificPreferences, times(1)).setCurrentUserMembershipPeriod(anyString(), anyString());
        verify(deviceSpecificPreferences, atLeastOnce()).removeSharedPreference(anyString());
    }

    @After
    public void setDates() {
        today = Calendar.getInstance();
        nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
    }

}
