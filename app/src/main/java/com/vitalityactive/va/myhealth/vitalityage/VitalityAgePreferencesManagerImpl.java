package com.vitalityactive.va.myhealth.vitalityage;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.InsurerConfigurationRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class VitalityAgePreferencesManagerImpl implements VitalityAgePreferencesManager {

    private final DeviceSpecificPreferences deviceSpecificPreferences;
    private final InsurerConfigurationRepository insurerConfigurationRepository;

    public VitalityAgePreferencesManagerImpl(DeviceSpecificPreferences deviceSpecificPreferences, InsurerConfigurationRepository insurerConfigurationRepository) {
        this.deviceSpecificPreferences = deviceSpecificPreferences;
        this.insurerConfigurationRepository = insurerConfigurationRepository;
    }

    @Override
    public void onLogin() {
        try {
            Date startDate = insurerConfigurationRepository.getCurrentMembershipPeriodStart();
            Date endDate = insurerConfigurationRepository.getCurrentMembershipPeriodEnd();
            Set<String> currentDates = deviceSpecificPreferences.getCurrentUserMembershipPeriod();
            if (currentDates != null && currentDates.size() == 2) {
                List<Date> orderedPreferencesDates = getOrderedDates(currentDates);
                Date preferencesStartDate = orderedPreferencesDates.get(0);
                Date preferencesEndDate = orderedPreferencesDates.get(1);
                if (!preferencesStartDate.equals(startDate) || !preferencesEndDate.equals(endDate)) {
                    deviceSpecificPreferences.setCurrentUserMembershipPeriod(String.valueOf(startDate.getTime()), String.valueOf(endDate.getTime()));
                    resetMemberShipPeriodPreferences();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Date> getOrderedDates(Set<String> currentDates) throws Exception {
        List<Date> membershipDates = new ArrayList<>();
        List<String> currentDatesAsList = Arrays.asList(currentDates.toArray(new String[currentDates.size()]));
        Date firstDate = new Date(Long.parseLong(currentDatesAsList.get(0)));
        Date nextDate = new Date(Long.parseLong(currentDatesAsList.get(1)));
        membershipDates.add(firstDate.before(nextDate) ? firstDate : nextDate);
        membershipDates.add(nextDate.after(firstDate) ? nextDate : firstDate);
        return membershipDates;
    }

    public void resetMemberShipPeriodPreferences() {
        deviceSpecificPreferences.removeSharedPreference(DeviceSpecificPreferences.HAS_CURRENT_USER_COMPLETED_VHC_BEFORE);
        deviceSpecificPreferences.removeSharedPreference(DeviceSpecificPreferences.HAS_CURRENT_USER_SEEN_VITALITY_AGE);
    }
}
