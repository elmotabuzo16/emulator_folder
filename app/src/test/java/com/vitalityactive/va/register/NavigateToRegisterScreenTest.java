package com.vitalityactive.va.register;

import com.vitalityactive.va.NavigationTestBase;

import org.junit.Test;

import static org.mockito.Mockito.verify;

public class NavigateToRegisterScreenTest extends NavigationTestBase {
    @Test
    public void doesItNavigateToRegisterWhenRegisterButtonTapped() {
        navigationCoordinator.navigateFromLoginAfterRegisterTapped(mockActivity, "");

        verify(mockNavigator).showRegistration(mockActivity, "");
    }
}
