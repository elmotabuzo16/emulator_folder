package com.vitalityactive.va.vna;

import android.app.Activity;

import com.vitalityactive.va.NavigationTestBase;

import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NavigationTests extends NavigationTestBase {
    @Test
    public void from_home_have_not_seen_onboarding_show_onboarding() {
        when(mockPreferences.hasCurrentUserSeenVNAOnboarding()).thenReturn(false);

        navigationCoordinator.navigateAfterVNACardTapped(null);

        verify(mockNavigator, times(1)).showVNAOnboarding(any(Activity.class));
    }

    @Test
    public void from_home_have_not_seen_onboarding_persist_have_seen() {
        when(mockPreferences.hasCurrentUserSeenVNAOnboarding()).thenReturn(false);

        navigationCoordinator.navigateAfterVNACardTapped(null);

        verify(mockPreferences, times(1)).setCurrentUserHasSeenVNAOnboarding();
    }

    @Test
    public void from_home_have_seen_onboarding_show_landing() {
        when(mockPreferences.hasCurrentUserSeenVNAOnboarding()).thenReturn(true);

        navigationCoordinator.navigateAfterVNACardTapped(null);

        verify(mockNavigator, times(1)).showVNALanding(any(Activity.class));
    }
}
