package com.vitalityactive.va.vhr;

import com.vitalityactive.va.EventDispatcherOnInvoke;
import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmitter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsAgreeRequestCompletedEvent;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;

public class VHRPrivacyPolicyPresenterImplTests {
    @Mock
    private TermsAndConditionsInteractor interactor;
    @Mock
    private TermsAndConditionsConsenter consenter;
    @Mock
    private QuestionnaireSubmitter questionnaireSubmitter;
    @Mock
    private VHRPrivacyPolicyUserInterface userInterface;

    private SameThreadScheduler scheduler = new SameThreadScheduler();
    private EventDispatcher eventDispatcher = new EventDispatcher();
    private VHRPrivacyPolicyPresenterImpl presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new VHRPrivacyPolicyPresenterImpl(scheduler, interactor, consenter, eventDispatcher, questionnaireSubmitter);
        presenter.setUserInterface(userInterface);
        presenter.addEventListeners();
    }

    @After
    public void tearDown() {
        presenter.removeEventListeners();
    }

    @Test
    public void when_terms_accepted_create_event() {
        presenter.onUserAgreesToTermsAndConditions();

        verify(consenter).agreeToTermsAndConditions();
    }

    @Test
    public void when_terms_accepted_vhr_submitted() {
        presenter.onUserInterfaceCreated(true);
        setupSuccessfulConsentService();
        presenter.onUserAgreesToTermsAndConditions();

        verify(questionnaireSubmitter).submit(anyLong());
    }

    private void setupSuccessfulConsentService() {
        EventDispatcherOnInvoke
                .fire(eventDispatcher, new TermsAndConditionsAgreeRequestCompletedEvent(RequestResult.SUCCESSFUL))
                .when(consenter).agreeToTermsAndConditions();
    }
}
