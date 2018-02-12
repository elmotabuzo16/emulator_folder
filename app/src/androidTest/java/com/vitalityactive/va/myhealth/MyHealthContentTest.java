package com.vitalityactive.va.myhealth;

import android.support.test.InstrumentationRegistry;

import com.vitalityactive.va.BaseAndroidTest;
import com.vitalityactive.va.R;
import com.vitalityactive.va.myhealth.content.MyHealthContent;
import com.vitalityactive.va.myhealth.content.VitalityAgeData;
import com.vitalityactive.va.myhealth.dto.VitalityAgeHealthAttributeDTO;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.testutilities.annotations.UnitTestRequiringAndroidContext;
import com.vitalityactive.va.vhc.dto.HealthAttributeFeedbackDTO;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@UnitTestRequiringAndroidContext
public class MyHealthContentTest extends BaseAndroidTest {

    @Before
    public void setUp() {
    }



    @Test
    public void can_return_feedbacktip_placeholder_for_what_youaredoingwell_section() {
        String expected = getString(R.string.feedback_tips_whatdoingwell_missing, null);
        String content = getString(MyHealthContent.VitalityAgeTipsFeedbackPlaceholder.getPlaceholder(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_GOOD), null);
        assertNotNull(content);
        assertEquals(expected, content);
    }

    @Test
    public void can_return_feedbacktip_placeholder_for_what_youtoimprove_section() {
        String expected = getString(R.string.feedback_tips_whattoimprove_missing, null);
        String content = getString(MyHealthContent.VitalityAgeTipsFeedbackPlaceholder.getPlaceholder(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_BAD), null);
        assertNotNull(content);
        assertEquals(expected, content);
    }


    @Test
    public void can_return_feedbacktip_placeholder_for_what_notprovided_section() {
        String expected = getString(R.string.feedback_tips_whatnotprovided_missing, null);
        String content = getString(MyHealthContent.VitalityAgeTipsFeedbackPlaceholder.getPlaceholder(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_UNKNOWN), null);
        assertNotNull(content);
        assertEquals(expected, content);
    }

    @Test
    public void can_return_vitality_age_feedback_from_vitality_age_above() {
        String expectedVitalityAgeValue = "35";
        String expectedVariance = "4";
        String expectedSummary = "Too High";
        String expectedContent = getString(R.string.my_health_vitality_age_older_description_long_622, expectedVariance);
        VitalityAgeData vitalityAgeDetail = MyHealthContent.getVitalityAge(InstrumentationRegistry.getTargetContext(), expectedVitalityAgeValue, expectedSummary, VitalityAgeConstants.VA_ABOVE, expectedVariance);
        assertNotNull(vitalityAgeDetail);
        assertEquals(expectedContent, vitalityAgeDetail.itemFeedbackExplanation);
        assertEquals(expectedSummary, vitalityAgeDetail.itemFeedbackSummary);
        assertEquals(expectedVitalityAgeValue, vitalityAgeDetail.itemVitalityAgeValue);
        assertEquals(VitalityAgeConstants.VA_ABOVE, vitalityAgeDetail.itemFeedbackType);
    }

    @Test
    public void can_return_vitality_age_feedback_from_vitality_age_below() {
        String expectedVitalityAgeValue = "35";
        String expectedVariance = "4";
        String expectedSummary = "Looking Great!";
        String expectedContent = getString(R.string.my_health_vitality_age_younger_description_long_631, expectedVariance);
        VitalityAgeData vitalityAgeDetail = MyHealthContent.getVitalityAge(InstrumentationRegistry.getTargetContext(), expectedVitalityAgeValue, expectedSummary, VitalityAgeConstants.VA_BELOW, expectedVariance);
        assertNotNull(vitalityAgeDetail);
        assertEquals(expectedContent, vitalityAgeDetail.itemFeedbackExplanation);
        assertEquals(expectedSummary, vitalityAgeDetail.itemFeedbackSummary);
        assertEquals(expectedVitalityAgeValue, vitalityAgeDetail.itemVitalityAgeValue);
        assertEquals(VitalityAgeConstants.VA_BELOW, vitalityAgeDetail.itemFeedbackType);
    }

    @Test
    public void can_return_vitality_age_feedback_from_vitality_age_normal() {
        String expectedVitalityAgeValue = "35";
        String expectedVariance = null;
        String expectedSummary = "Looking Great!";
        String expectedContent = getString(R.string.my_health_vitality_age_healthy_description_long_625, expectedVariance);
        VitalityAgeData vitalityAgeDetail = MyHealthContent.getVitalityAge(InstrumentationRegistry.getTargetContext(), expectedVitalityAgeValue, expectedSummary, VitalityAgeConstants.VA_HEALTHY, expectedVariance);
        assertNotNull(vitalityAgeDetail);
        assertEquals(expectedContent, vitalityAgeDetail.itemFeedbackExplanation);
        assertEquals(expectedSummary, vitalityAgeDetail.itemFeedbackSummary);
        assertEquals(expectedVitalityAgeValue, vitalityAgeDetail.itemVitalityAgeValue);
        assertEquals(VitalityAgeConstants.VA_HEALTHY, vitalityAgeDetail.itemFeedbackType);
    }

    @Test
    public void can_return_vitality_age_feedback_from_vitality_age_normal_ignores_variance() {
        String expectedVitalityAgeValue = "35";
        String expectedVariance = "5";
        String expectedSummary = "Looking Great!";
        String expectedContent = getString(R.string.my_health_vitality_age_healthy_description_long_625, expectedVariance);
        VitalityAgeData vitalityAgeDetail = MyHealthContent.getVitalityAge(InstrumentationRegistry.getTargetContext(), expectedVitalityAgeValue, expectedSummary, VitalityAgeConstants.VA_HEALTHY, expectedVariance);
        assertNotNull(vitalityAgeDetail);
        assertEquals(expectedContent, vitalityAgeDetail.itemFeedbackExplanation);
        assertEquals(expectedSummary, vitalityAgeDetail.itemFeedbackSummary);
        assertEquals(expectedVitalityAgeValue, vitalityAgeDetail.itemVitalityAgeValue);
        assertEquals(VitalityAgeConstants.VA_HEALTHY, vitalityAgeDetail.itemFeedbackType);
    }


    @Test
    public void can_return_vitality_age_feedback_from_vitality_age_below_summary() {
        String expectedVitalityAgeValue = "35";
        String expectedVariance = "4";
        String expectedSummary = "Looking Great!";
        String expectedContent = getString(R.string.my_health_vitality_age_younger_description_short_630, expectedVariance);
        VitalityAgeData vitalityAgeDetail = MyHealthContent.getVitalityAgeSummary(InstrumentationRegistry.getTargetContext(), expectedVitalityAgeValue, expectedSummary, VitalityAgeConstants.VA_BELOW, expectedVariance);
        assertNotNull(vitalityAgeDetail);
        assertEquals(expectedContent, vitalityAgeDetail.itemFeedbackExplanation);
        assertEquals(expectedSummary, vitalityAgeDetail.itemFeedbackSummary);
        assertEquals(expectedVitalityAgeValue, vitalityAgeDetail.itemVitalityAgeValue);
        assertEquals(VitalityAgeConstants.VA_BELOW, vitalityAgeDetail.itemFeedbackType);
    }

    @Test
    public void can_return_vitality_age_feedback_from_vitality_age_above_summary() {
        String expectedVitalityAgeValue = "35";
        String expectedVariance = "4";
        String expectedSummary = "Looking Great!";
        String expectedContent = getString(R.string.my_health_vitality_age_older_description_short_621, expectedVariance);
        VitalityAgeData vitalityAgeDetail = MyHealthContent.getVitalityAgeSummary(InstrumentationRegistry.getTargetContext(), expectedVitalityAgeValue, expectedSummary, VitalityAgeConstants.VA_ABOVE, expectedVariance);
        assertNotNull(vitalityAgeDetail);
        assertEquals(expectedContent, vitalityAgeDetail.itemFeedbackExplanation);
        assertEquals(expectedSummary, vitalityAgeDetail.itemFeedbackSummary);
        assertEquals(expectedVitalityAgeValue, vitalityAgeDetail.itemVitalityAgeValue);
        assertEquals(VitalityAgeConstants.VA_ABOVE, vitalityAgeDetail.itemFeedbackType);
    }

    @Test
    public void can_return_vitality_age_feedback_from_vitality_age_normal_summary() {
        String expectedVitalityAgeValue = "35";
        String expectedVariance = null;
        String expectedSummary = "Looking Great!";
        String expectedContent = getString(R.string.my_health_vitality_age_healthy_description_short_624, expectedVariance);
        VitalityAgeData vitalityAgeDetail = MyHealthContent.getVitalityAgeSummary(InstrumentationRegistry.getTargetContext(), expectedVitalityAgeValue, expectedSummary, VitalityAgeConstants.VA_HEALTHY, expectedVariance);
        assertNotNull(vitalityAgeDetail);
        assertEquals(expectedContent, vitalityAgeDetail.itemFeedbackExplanation);
        assertEquals(expectedSummary, vitalityAgeDetail.itemFeedbackSummary);
        assertEquals(expectedVitalityAgeValue, vitalityAgeDetail.itemVitalityAgeValue);
        assertEquals(VitalityAgeConstants.VA_HEALTHY, vitalityAgeDetail.itemFeedbackType);
    }

    @Test
    public void can_return_vitality_age_feedback_from_vitality_age_normal_summary_ignores_variance() {
        String expectedVitalityAgeValue = "35";
        String expectedVariance = "35";
        String expectedSummary = "Looking Great!";
        String expectedContent = getString(R.string.my_health_vitality_age_healthy_description_short_624, expectedVariance);
        VitalityAgeData vitalityAgeDetail = MyHealthContent.getVitalityAgeSummary(InstrumentationRegistry.getTargetContext(), expectedVitalityAgeValue, expectedSummary, VitalityAgeConstants.VA_HEALTHY, expectedVariance);
        assertNotNull(vitalityAgeDetail);
        assertEquals(expectedContent, vitalityAgeDetail.itemFeedbackExplanation);
        assertEquals(expectedSummary, vitalityAgeDetail.itemFeedbackSummary);
        assertEquals(expectedVitalityAgeValue, vitalityAgeDetail.itemVitalityAgeValue);
        assertEquals(VitalityAgeConstants.VA_HEALTHY, vitalityAgeDetail.itemFeedbackType);
    }

    private VitalityAgeHealthAttributeDTO createOutdatedVitalityAgeHealthAttributeDTO(int typeKey, String vitalityAgeRange) {
        HealthAttributeFeedbackDTO healthAttributeFeedbackDTO = createHealthAttributeFeedbackDTO(typeKey);
        HealthAttributeFeedbackDTO healthAttributeFeedbackDTOoutdated = createHealthAttributeFeedbackDTO(VitalityAgeConstants.VA_OUTDATED);
        return new VitalityAgeHealthAttributeDTO("attributeTypeCode", Long.valueOf(VitalityAgeConstants.VITALITY_AGE_TYPE_KEY).intValue(), "attributeTypeName", Arrays.asList(healthAttributeFeedbackDTO, healthAttributeFeedbackDTOoutdated), "measuredOn", 1, "unitofMeasure", vitalityAgeRange);
    }

    private VitalityAgeHealthAttributeDTO createOutdatedVitalityAgeHealthAttributeDTOWithNullFeedback(int typeKey, String vitalityAgeRange) {
        HealthAttributeFeedbackDTO healthAttributeFeedbackDTO = createHealthAttributeFeedbackDTO(typeKey);
        HealthAttributeFeedbackDTO healthAttributeFeedbackDTOoutdated = createHealthAttributeFeedbackDTO(VitalityAgeConstants.VA_OUTDATED);
        return new VitalityAgeHealthAttributeDTO("attributeTypeCode", Long.valueOf(VitalityAgeConstants.VITALITY_AGE_TYPE_KEY).intValue(), "attributeTypeName", null, "measuredOn", 1, "unitofMeasure", vitalityAgeRange);
    }

    private HealthAttributeFeedbackDTO createHealthAttributeFeedbackDTO(int typeKey) {
        return new HealthAttributeFeedbackDTO(typeKey, "feedbackTypeName", "feedbackTypeCode", 1, "feedbackTypeTypeName", "feedbackTypeTypeCode", typeKey);
    }

    private String getString(int resourceId, String var) {
        if (var != null) {
            return InstrumentationRegistry.getTargetContext().getResources().getString(resourceId, var);
        } else {
            return InstrumentationRegistry.getTargetContext().getResources().getString(resourceId);
        }
    }

    @Override
    protected void launchActivity() {

    }
}
