package com.vitalityactive.va.vitalitystatus;

import android.app.Activity;

import com.vitalityactive.va.NavigationTestBase;

import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VitalityStatusOnboardingNavigationTest extends NavigationTestBase {

    @Test
    public void should_show_onboarding_when_not_seen() {
        when(mockPreferences.hasCurrentUserHasSeenVitalityStatusOnboarding()).thenReturn(false);
        navigationCoordinator.navigateAfterVitalityStatusTapped(null);

        verify(mockNavigator, times(1)).showVitalityStatusOnboarding(any(Activity.class));
        verify(mockPreferences, times(1)).setCurrentUserHasSeenVitalityStatusOnboarding();
    }

    @Test
    public void should_not_show_onboarding_when_seen() {
        when(mockPreferences.hasCurrentUserHasSeenVitalityStatusOnboarding()).thenReturn(true);
        navigationCoordinator.navigateAfterVitalityStatusTapped(null);

        verify(mockNavigator, times(0)).showVitalityStatusOnboarding(any(Activity.class));
    }
}
