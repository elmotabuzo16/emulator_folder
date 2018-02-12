package com.vitalityactive.va.vna;

import com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork;

import org.junit.Test;

@UITestWithMockedNetwork
public class LandingActivityTests extends BaseTests {
    @Test
    public void can_load_landing_activity() {
        setupVnaQuestionnaireDataAndLaunchLandingActivity();
    }
}
