package com.vitalityactive.va.onboarding;

import com.vitalityactive.va.DeviceSpecificPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OnboardingPresenterTest {
    @Mock
    DeviceSpecificPreferences mockDeviceSpecificPreferences;

    @Mock
    private OnboardingPresenter.UserInterface mockUserInterface;

    private OnboardingPresenter onboardingPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        onboardingPresenter = new OnboardingPresenterImpl(mockDeviceSpecificPreferences);
        onboardingPresenter.setUserInterface(mockUserInterface);
    }

    @Test
    public void doesOnboardingShowIfNotSeenBefore() {
        givenCurrentUserShouldSeeOnboarding();
        assertTrue(onboardingPresenter.getViewModel().shouldShowOnboarding());
    }

    @Test
    public void isOnboardingSkippedIfSeenBefore() {
        givenCurrentUserHasSeenOnboarding();
        assertFalse(onboardingPresenter.getViewModel().shouldShowOnboarding());
    }

    @Test
    public void areTheCorrectNumberOfPagesReturned() {
        givenCurrentUserShouldSeeOnboarding();
        assertEquals(4, onboardingPresenter.getViewModel().getNumberOfPages());
    }

    @Test
    public void areOnboardingPagesCorrectlyIdentified() {
        givenCurrentUserShouldSeeOnboarding();
        assertTrue(onboardingPresenter.getViewModel().isOnboardingPageAtPosition(0));
        assertTrue(onboardingPresenter.getViewModel().isOnboardingPageAtPosition(1));
        assertTrue(onboardingPresenter.getViewModel().isOnboardingPageAtPosition(2));
        assertFalse(onboardingPresenter.getViewModel().isOnboardingPageAtPosition(3));
        assertFalse(onboardingPresenter.getViewModel().isOnboardingPageAtPosition(4));
        assertFalse(onboardingPresenter.getViewModel().isOnboardingPageAtPosition(-1));
    }

    @Test
    public void isOnboardingSkipped() {
        onboardingPresenter.onSkipOnboarding();

        verify(mockUserInterface).skipOnboarding();
    }

    private void givenCurrentUserHasSeenOnboarding() {
        when(mockDeviceSpecificPreferences.hasCurrentUserSeenOnboarding()).thenReturn(true);
    }

    private void givenCurrentUserShouldSeeOnboarding() {
        when(mockDeviceSpecificPreferences.hasCurrentUserSeenOnboarding()).thenReturn(false);
    }
}
