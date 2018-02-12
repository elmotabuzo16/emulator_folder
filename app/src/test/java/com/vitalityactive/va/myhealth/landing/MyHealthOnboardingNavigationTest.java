package com.vitalityactive.va.myhealth.landing;


import android.app.Activity;

import com.vitalityactive.va.NavigationTestBase;

import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MyHealthOnboardingNavigationTest extends NavigationTestBase {

    @Test
    public void should_show_onboarding_when_not_seen() {
        when(mockPreferences.hasCurrentUserHasSeenMyHealthOnboarding()).thenReturn(false);
        navigationCoordinator.navigateToMyHealthOnboarding(null);
        verify(mockNavigator, times(1)).showMyHealthOnboardingScreen(any(Activity.class), anyInt());
    }
}
