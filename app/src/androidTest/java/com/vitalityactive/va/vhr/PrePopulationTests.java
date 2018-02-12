package com.vitalityactive.va.vhr;

import com.vitalityactive.va.R;
import com.vitalityactive.va.testutilities.ForceLocaleTestRule;
import com.vitalityactive.va.testutilities.VitalityActive;
import com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork;

import org.junit.ClassRule;
import org.junit.Test;

import java.util.Locale;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.testutilities.VitalityActive.Matcher.withRecyclerView;
import static com.vitalityactive.va.testutilities.VitalityActive.Matcher.withRegexText;

@UITestWithMockedNetwork
public class PrePopulationTests extends BaseTests {

    @ClassRule
    public static final ForceLocaleTestRule localeTestRule = new ForceLocaleTestRule(Locale.ENGLISH);

    private void loadDataAndStartQuestionnaire(String file) {
        setupVHRCompletedQuestionnaireDataAndLaunchLandingActivity(file);
        clickOnFirstEditButton();
    }

    @Test
    public void pre_populated_values_and_unit_are_loaded_and_selected() {
        loadDataAndStartQuestionnaire("vhr/pre_population_with_unit_of_measure.json");
        onView(withText("75.0")).check(matches(isDisplayed()));
        onView(withText("lb")).check(matches(isDisplayed()));
    }

    @Test
    public void pre_populated_values_and_unit_are_loaded_for_single_unit_fields() {
        loadDataAndStartQuestionnaire("vhr/pre_population_with_unit_of_measure.json");
        onView(withText("1.8")).check(matches(isDisplayed()));
        onView(withText("m")).check(matches(isDisplayed()));
    }

    @Test
    public void invalid_pre_populated_questions_with_single_unit() {
        loadDataAndStartQuestionnaire("vhr/pre_population_with_unit_of_measure_invalid.json");
        VitalityActive.Matcher.checkViewIsNotDisplayed(withText("75000"));
        VitalityActive.Matcher.checkViewIsNotDisplayed(withText("g"));
    }

    @Test
    public void invalid_pre_populated_questions_with_multiple_units() {
        loadDataAndStartQuestionnaire("vhr/pre_population_with_unit_of_measure_invalid.json");
        VitalityActive.Matcher.checkViewIsNotDisplayed(withText("180"));
        VitalityActive.Matcher.checkViewIsNotDisplayed(withText("cm"));
    }

    @Test
    public void with_pre_populated_event_source() {
        loadDataAndStartQuestionnaire("vhr/pre_populated_with_eventSource.json");
        VitalityActive.Matcher.checkViewIsNotDisplayed(withText("22 June 2017"));
    }

    @Test
    public void with_pre_populated_height_event_source_displayed() {
        loadDataAndStartQuestionnaire("vhr/pre_populated_with_eventSource.json");
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(0, -1))
                .check(matches(hasDescendant(withRegexText(R.string.assessment_vhr_pre_population_details_570))));
    }

    @Test
    public void with_pre_populated_weight_event_source_displayed() {
        loadDataAndStartQuestionnaire("vhr/pre_populated_with_eventSource.json");
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(1, -1))
                .check(matches(hasDescendant(withRegexText(R.string.assessment_vhr_pre_population_details_570))));
    }
}
