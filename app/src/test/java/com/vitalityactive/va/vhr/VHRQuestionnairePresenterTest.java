package com.vitalityactive.va.vhr;

import android.content.Context;
import android.content.res.AssetManager;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.questionnaire.QuestionnaireStateManager;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.types.TestQuestionFactory;
import com.vitalityactive.va.questionnaire.types.YesNoQuestionDto;
import com.vitalityactive.va.questionnaire.validations.ValidationResult;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmitter;
import com.vitalityactive.va.vhr.questions.VHRQuestionnairePresenterImpl;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmissionServiceClient;
import com.vitalityactive.va.myhealth.dto.VitalityAgeRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.threeten.bp.zone.ZoneRulesException;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VHRQuestionnairePresenterTest {
    @Mock
    QuestionnaireStateManager questionnaireStateManager;
    @Mock
    VHRQuestionnairePresenterImpl.UserInterface userInterface;
    @Mock
    QuestionnaireSubmissionServiceClient questionnaireSetServiceClient;
    @Mock
    InsurerConfigurationRepository insurerConfigurationRepository;
    @Mock
    QuestionnaireSubmitter questionnaireSubmitter;
    @Mock
    EventDispatcher eventDispatcher;
    @Mock
    Scheduler scheduler;
    private QuestionnaireCapturePresenter presenter;
    @Mock
    VitalityAgeRepository vitalityAgeRepository;

    private static void initialiseAndroidThreeTen() throws IOException {
        try {
            Context mockContext = mock(Context.class);
            AssetManager mockAssets = mock(AssetManager.class);
            when(mockAssets.open(anyString())).thenReturn(VHRQuestionnairePresenterTest.class.getClassLoader().getResourceAsStream("TZDB.dat"));
            when(mockContext.getAssets()).thenReturn(mockAssets);
            AndroidThreeTen.init(mockContext);
        } catch (ZoneRulesException ignored) {

        }
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        initialiseAndroidThreeTen();
        presenter = new VHRQuestionnairePresenterImpl(questionnaireStateManager,
                insurerConfigurationRepository,
                questionnaireSubmitter,
                eventDispatcher,
                scheduler
        );
        presenter.setUserInterface(userInterface);

        when(questionnaireStateManager.getAllQuestionsInCurrentSection()).thenReturn(TestQuestionFactory.basicList(0));
        when(questionnaireStateManager.getCurrentSectionIndex()).thenReturn(1);
        when(questionnaireStateManager.getTotalSections()).thenReturn(3L);
    }

    @Test
    public void activity_created_load_section_questions() {
        presenter.onUserInterfaceAppeared();

        verify(userInterface, times(1)).bindQuestions(anyListOf(Question.class));
    }

    @Test
    public void activity_created_load_section_progress_1based_progress() {
        presenter.onUserInterfaceAppeared();

        verify(userInterface, times(1)).sectionProgress(2, 3);      // 1 more than index
    }

    @Test
    public void activity_created_load_can_proceed_when_all_answered() {
        when(questionnaireStateManager.getUnansweredQuestionsInCurrentSection()).thenReturn(Collections.<Question>emptyList());

        presenter.onUserInterfaceAppeared();

        verify(userInterface, times(1)).canProceedToNextSection(true);
    }

    @Test
    public void activity_created_load_can_proceed_blocked_on_unanswered_questions() {
        when(questionnaireStateManager.getUnansweredQuestionsInCurrentSection()).thenReturn(TestQuestionFactory.basicList(0));

        presenter.onUserInterfaceAppeared();

        verify(userInterface, times(1)).canProceedToNextSection(false);
    }

    @Test
    public void question_answered_proceed_blocked_on_invalid_answering_even_if_all_questions_answered() {
        when(questionnaireStateManager.getUnansweredQuestionsInCurrentSection()).thenReturn(Collections.<Question>emptyList());
        when(questionnaireStateManager.answer(any(Question.class))).thenReturn(new ValidationResult(false));

        presenter.onUserInterfaceAppeared();
        presenter.setQuestionAnswered(new YesNoQuestionDto(0, 0, 0, "", "", "", 0, String.valueOf(true), String.valueOf(false), "Yes", "No"));

        verify(userInterface, times(1)).canProceedToNextSection(false);
    }

    @Test
    public void activity_created_load_can_proceed_blocked_on_invalid_answers() {
        // all answered, but a lot invalid
        when(questionnaireStateManager.getUnansweredQuestionsInCurrentSection()).thenReturn(Collections.<Question>emptyList());
        when(questionnaireStateManager.getInvalidAnsweredQuestionsInCurrentSection()).thenReturn(TestQuestionFactory.basicList(0));

        presenter.onUserInterfaceAppeared();

        verify(userInterface, times(1)).canProceedToNextSection(false);
    }

    @Test
    public void activity_forward_adjusts_section_index() {
        presenter.onForwardButtonClicked();

        verify(questionnaireStateManager, times(1)).goToNextSection();
    }

    @Test
    public void activity_forward_when_not_on_last_sections_navigate_to_next_section() {
        when(questionnaireStateManager.goToNextSection()).thenReturn(true);

        presenter.onForwardButtonClicked();

        verify(userInterface, times(1)).navigateToNextSection();
        verify(userInterface, times(0)).navigateAfterQuestionnaireCompleted();
    }

    @Test
    public void activity_forward_when_on_last_sections_fire_questionnaire_completed() {
        when(questionnaireStateManager.goToNextSection()).thenReturn(false);
        when(insurerConfigurationRepository.shouldShowVHRPrivacyPolicy()).thenReturn(true);

        presenter.onForwardButtonClicked();

        verify(userInterface, times(0)).navigateToNextSection();
        verify(userInterface, times(1)).navigateAfterQuestionnaireCompleted();
    }

    @Test
    public void activity_back_adjusts_section_index() {
        presenter.onBackButtonClicked();

        verify(questionnaireStateManager, times(1)).goToPreviousSection();
    }

    @Test
    public void activity_back_navigates_not_on_first_section_do_not_go_back() {
        when(questionnaireStateManager.goToPreviousSection()).thenReturn(true);

        presenter.onBackButtonClicked();

        verify(userInterface, times(0)).navigateToLandingScreen();
    }

    @Test
    public void activity_back_navigates_not_on_first_section_load_section_again() {
        when(questionnaireStateManager.goToPreviousSection()).thenReturn(true);

        presenter.onBackButtonClicked();

        verify(userInterface, times(1)).bindQuestions(anyListOf(Question.class));
        verify(userInterface, times(1)).sectionProgress(anyInt(), anyInt());
    }

    @Test
    public void activity_back_navigates_on_first_section_back() {
        when(questionnaireStateManager.goToPreviousSection()).thenReturn(false);

        presenter.onBackButtonClicked();

        verify(userInterface, times(1)).navigateToLandingScreen();
    }

    @Test
    public void question_answered_recalculate_views() {
        when(questionnaireStateManager.answer(any(Question.class))).thenReturn(new ValidationResult(false));
        presenter.setQuestionAnswered(null);

        verify(userInterface, times(1)).rebindQuestions(anyListOf(Question.class));
    }

    @Test
    public void question_answered_recalculate_can_proceed() {
        // assume all answered correctly
        when(questionnaireStateManager.answer(any(Question.class))).thenReturn(new ValidationResult(true));

        // unanswered questions remain
        when(questionnaireStateManager.getUnansweredQuestionsInCurrentSection()).thenReturn(TestQuestionFactory.basicList(0));
        presenter.setQuestionAnswered(null);
        verify(userInterface, times(1)).canProceedToNextSection(false);

        // all questions answered
        when(questionnaireStateManager.getUnansweredQuestionsInCurrentSection()).thenReturn(Collections.<Question>emptyList());
        presenter.setQuestionAnswered(null);
        verify(userInterface, times(1)).canProceedToNextSection(true);
    }
}
