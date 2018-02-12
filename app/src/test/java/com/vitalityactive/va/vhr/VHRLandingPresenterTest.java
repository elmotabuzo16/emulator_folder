package com.vitalityactive.va.vhr;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.constants.QuestionnaireSet;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.home.vhr.VHRCardRepository;
import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.shared.questionnaire.QuestionnaireLandingUserInterface;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetServiceClient;
import com.vitalityactive.va.vhr.content.VHRContent;
import com.vitalityactive.va.vhr.landing.VHRLandingPresenterImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class VHRLandingPresenterTest {
    @Mock
    private QuestionnaireLandingUserInterface mockUserInterface;
    @Mock
    private VHRContent content;
    @Mock
    private VHRCardRepository vhrCardRepository;
    @Mock
    private QuestionnaireSetServiceClient questionnaireSetServiceClient;
    @Mock
    private QuestionnaireSetRepository questionnaireSetRepository;
    @Mock
    private EventDispatcher eventDispatcher;
    @Mock
    private Scheduler scheduler;

    private VHRLandingPresenterImpl presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void fetches_vhr_questionnaires() throws Exception {
        setUpPresenter();

        presenter.fetchQuestionnaireSet();

        long vhr = QuestionnaireSet._VHR;       // fetchQuestionnaireSets expects longs
        verify(questionnaireSetServiceClient).fetchQuestionnaireSets(eq(vhr), any(QuestionnaireSetRepository.class));
    }

    private void setUpPresenter() {
        presenter = new VHRLandingPresenterImpl(content,
                questionnaireSetServiceClient,
                eventDispatcher,
                scheduler,
                questionnaireSetRepository);

        presenter.setUserInterface(mockUserInterface);
    }
}
