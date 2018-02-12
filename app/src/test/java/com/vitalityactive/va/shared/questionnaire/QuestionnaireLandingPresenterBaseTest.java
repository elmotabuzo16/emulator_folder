package com.vitalityactive.va.shared.questionnaire;

import com.vitalityactive.va.EventDispatcherOnInvoke;
import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestFailedEvent;
import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireDTO;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireSetInformation;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetRequestSuccess;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetServiceClient;
import com.vitalityactive.va.vhr.content.VHRContent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class QuestionnaireLandingPresenterBaseTest {
    @Mock
    private VHRContent content;
    @Mock
    private QuestionnaireLandingUserInterface mockUserInterface;
    @Mock
    private QuestionnaireSetServiceClient mockServiceClient;
    @Mock
    private QuestionnaireSetRepository questionnaireSetRepository;

    private QuestionnaireLandingPresenterBase presenter;
    private EventDispatcher eventDispatcher = new EventDispatcher();
    private SameThreadScheduler scheduler = new SameThreadScheduler();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(content.getOnboardingTitle()).thenReturn("TITLE");
    }

    @Test
    public void load_title_from_content() {
        setUpPresenter();
        presenter.onUserInterfaceCreated(true);

        // todo: why is this returning with an extra space
        verify(mockUserInterface).setActionBarTitleAndDisplayHomeAsUp(matches("^Title.*"));
    }

    @Test
    public void loading_indicator_is_shown_when_questionnaires_are_fetched() throws Exception {
        setUpPresenter();

        presenter.fetchQuestionnaireSet();

        verify(mockUserInterface).showLoadingIndicator();
    }

    @Test
    public void fetch_questionnaires_when_interface_appeared() {
        setUpPresenter();
        presenter.onUserInterfaceAppeared();

        verify(mockServiceClient).fetchQuestionnaireSets(anyLong(), eq(questionnaireSetRepository));
    }

    @Test
    public void updates_user_interface_with_result() throws InterruptedException {
        setUpPresenter();
        EventDispatcherOnInvoke
                .fire(eventDispatcher, new QuestionnaireSetRequestSuccess())
                .when(mockServiceClient).fetchQuestionnaireSets(anyLong(), eq(questionnaireSetRepository));

        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        assertTrue(scheduler.invoked);
        verify(mockUserInterface).showQuestionnaireSetAndQuestionnaires(any(QuestionnaireSetInformation.class), anyListOf(QuestionnaireDTO.class));
        verify(mockUserInterface).hideLoadingIndicator();
    }

    @Test
    public void updates_user_interface_with_result_from_repository() throws InterruptedException {
        setUpPresenter();
        EventDispatcherOnInvoke
                .fire(eventDispatcher, new QuestionnaireSetRequestSuccess())
                .when(mockServiceClient).fetchQuestionnaireSets(anyLong(), eq(questionnaireSetRepository));

        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        verify(questionnaireSetRepository).getQuestionnairesSetTopLevelData();
        verify(questionnaireSetRepository).getQuestionnairesTopLevelData();
    }

    @Test
    public void show_error_messages_on_user_interface() throws InterruptedException {
        setUpPresenter();
        EventDispatcherOnInvoke
                .fire(eventDispatcher, new RequestFailedEvent(RequestFailedEvent.Type.CONNECTION_ERROR))
                .when(mockServiceClient).fetchQuestionnaireSets(anyLong(), eq(questionnaireSetRepository));

        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        assertTrue(scheduler.invoked);
        verify(mockUserInterface).showConnectionError();
        verify(mockUserInterface).hideLoadingIndicator();
    }

    private void setUpPresenter() {
        presenter = new QuestionnaireLandingPresenterBase(content,
                scheduler,
                eventDispatcher, mockServiceClient, questionnaireSetRepository)
        {
            @Override
            protected int getQuestionnaireSetTypeKey() {
                return 0;
            }
        };

        presenter.setUserInterface(mockUserInterface);
    }
}
