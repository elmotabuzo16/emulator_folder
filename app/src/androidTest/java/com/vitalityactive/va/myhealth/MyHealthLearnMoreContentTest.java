package com.vitalityactive.va.myhealth;

import android.support.test.InstrumentationRegistry;

import com.vitalityactive.va.R;
import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.myhealth.content.MyHealthLearnMoreContent;
import com.vitalityactive.va.myhealth.dto.MyHealthRepositoryImpl;
import com.vitalityactive.va.myhealth.entity.VitalityAge;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;
import com.vitalityactive.va.persistence.DataStore;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MyHealthLearnMoreContentTest extends BaseTests {
    private static final String VARIANCE = "4";
    private static final String VITALITY_AGE_LOW = "24";
    private static final String VITALITY_AGE_TOO_HIGH = "34";

    @Before
    public void setUp() throws InstantiationException, IllegalAccessException, IOException {
        super.setUp();
    }

    @Test
    public void can_fetch_correct_learnmore_content_for_va_below() {
        String actual = MyHealthLearnMoreContent.getLearnMoreHeader(InstrumentationRegistry.getTargetContext(), getVitalityAge(VARIANCE, VITALITY_AGE_LOW, VitalityAgeConstants.VA_BELOW, 0));
        String expected = InstrumentationRegistry.getTargetContext().getResources().getString(R.string.my_health_vitality_age_younger_description_long_631, VARIANCE);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void can_fetch_correct_learnmore_content_for_va_high() {
        String actual = MyHealthLearnMoreContent.getLearnMoreHeader(InstrumentationRegistry.getTargetContext(), getVitalityAge(VARIANCE, VITALITY_AGE_TOO_HIGH, VitalityAgeConstants.VA_ABOVE, 0));
        String expected = InstrumentationRegistry.getTargetContext().getResources().getString(R.string.my_health_learn_more_too_high_content_753, VARIANCE);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void can_fetch_correct_learnmore_content_for_va_normal() {
        String actual = MyHealthLearnMoreContent.getLearnMoreHeader(InstrumentationRegistry.getTargetContext(), getVitalityAge(VARIANCE, VITALITY_AGE_TOO_HIGH, VitalityAgeConstants.VA_HEALTHY, 0));
        String expected = InstrumentationRegistry.getTargetContext().getResources().getString(R.string.my_health__learn_more_good_content_757, VARIANCE);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void can_fetch_correct_learnmore_content_for_va_healthy_and_outdated() {
        String actual = MyHealthLearnMoreContent.getLearnMoreHeader(InstrumentationRegistry.getTargetContext(), getVitalityAge(VARIANCE, VITALITY_AGE_LOW, VitalityAgeConstants.VA_OUTDATED, VitalityAgeConstants.VA_HEALTHY));
        String expected = InstrumentationRegistry.getTargetContext().getResources().getString(R.string.my_health_learn_more_vitality_age_outdated_description_1101, VITALITY_AGE_LOW);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void can_fetch_correct_learnmore_content_for_va_below_and_outdated() {
        String actual = MyHealthLearnMoreContent.getLearnMoreHeader(InstrumentationRegistry.getTargetContext(), getVitalityAge(VARIANCE, VITALITY_AGE_LOW, VitalityAgeConstants.VA_OUTDATED, VitalityAgeConstants.VA_BELOW));
        String expected = InstrumentationRegistry.getTargetContext().getResources().getString(R.string.my_health_learn_more_vitality_age_outdated_description_1101, VITALITY_AGE_LOW);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void can_fetch_correct_learnmore_content_for_va_high_and_outdated() {
        String actual = MyHealthLearnMoreContent.getLearnMoreHeader(InstrumentationRegistry.getTargetContext(), getVitalityAge(VARIANCE, VITALITY_AGE_TOO_HIGH, VitalityAgeConstants.VA_OUTDATED, VitalityAgeConstants.VA_ABOVE));
        String expected = InstrumentationRegistry.getTargetContext().getResources().getString(R.string.my_health_learn_more_vitality_age_above_outdated_description, VITALITY_AGE_TOO_HIGH);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void can_handle_learnmore_content_for_va_null() {
        String actual = MyHealthLearnMoreContent.getLearnMoreHeader(InstrumentationRegistry.getTargetContext(), getVitalityAge(VARIANCE, null, VitalityAgeConstants.VA_OUTDATED, VitalityAgeConstants.VA_ABOVE));
        String expected = InstrumentationRegistry.getTargetContext().getResources().getString(R.string.my_health_learn_more_vitality_age_above_outdated_description, "");
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    private VitalityAge getVitalityAge(String variance, String value, int effectiveType, int actualType) {
        return new VitalityAge.Builder()
                .variance(variance)
                .age(value)
                .effectiveType(effectiveType)
                .actualType(actualType)
                .build();
    }

}
