package com.vitalityactive.va.shared.questionnaire;

import android.util.Log;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestFailedEvent;
import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.shared.content.OnboardingContent;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireDTO;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireSetInformation;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetRequestSuccess;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetServiceClient;
import com.vitalityactive.va.utilities.TextUtilities;

import java.util.List;

public abstract class QuestionnaireLandingPresenterBase implements QuestionnaireLandingPresenter {
    protected final OnboardingContent content;
    protected final Scheduler scheduler;
    protected final EventDispatcher eventDispatcher;
    protected final QuestionnaireSetServiceClient questionnaireSetServiceClient;
    protected final QuestionnaireSetRepository questionnaireSetRepository;
    protected QuestionnaireLandingUserInterface userInterface;
    protected EventListener<RequestFailedEvent> requestFailedEventEventListener;
    protected EventListener<QuestionnaireSetRequestSuccess> requestSuccessEventListener;

    public QuestionnaireLandingPresenterBase(OnboardingContent content, Scheduler scheduler, EventDispatcher eventDispatcher, QuestionnaireSetServiceClient questionnaireSetServiceClient, QuestionnaireSetRepository questionnaireSetRepository) {
        this.scheduler = scheduler;
        this.content = content;
        this.eventDispatcher = eventDispatcher;
        this.questionnaireSetServiceClient = questionnaireSetServiceClient;
        this.questionnaireSetRepository = questionnaireSetRepository;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        createEventListeners();

        userInterface.setActionBarTitleAndDisplayHomeAsUp(TextUtilities.toCamelCase(content.getOnboardingTitle()));
    }

    private void createEventListeners() {
        requestFailedEventEventListener = new EventListener<RequestFailedEvent>() {
            @Override
            public void onEvent(final RequestFailedEvent event) {
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        handleErrorResponse(event);
                    }
                });
            }
        };

        requestSuccessEventListener = new EventListener<QuestionnaireSetRequestSuccess>() {
            @Override
            public void onEvent(QuestionnaireSetRequestSuccess event) {
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        handleSuccessResponse();
                    }
                });
            }
        };
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(RequestFailedEvent.class, requestFailedEventEventListener);
        eventDispatcher.addEventListener(QuestionnaireSetRequestSuccess.class, requestSuccessEventListener);
    }

    private void removeEventListeners() {
        eventDispatcher.removeEventListener(RequestFailedEvent.class, requestFailedEventEventListener);
        eventDispatcher.removeEventListener(QuestionnaireSetRequestSuccess.class, requestSuccessEventListener);
    }

    protected void handleSuccessResponse() {
        questionnaireSetRepository.prepare();
        QuestionnaireSetInformation questionnaireSet = questionnaireSetRepository.getQuestionnairesSetTopLevelData();
        List<QuestionnaireDTO> questionnaires = questionnaireSetRepository.getQuestionnairesTopLevelData();

        userInterface.showQuestionnaireSetAndQuestionnaires(questionnaireSet, questionnaires);

        userInterface.hideLoadingIndicator();
    }

    protected void handleErrorResponse(RequestFailedEvent event) {
        questionnaireSetRepository.prepare();
        if (event.getType() == RequestFailedEvent.Type.CONNECTION_ERROR) {
            userInterface.showConnectionError();
        } else {
            userInterface.showGenericError();
        }

        userInterface.hideLoadingIndicator();
    }

    @Override
    public void fetchQuestionnaireSet() {
        userInterface.showLoadingIndicator();
        Log.e("cjc","fetchQuestionnaireSet");
        Log.e("cjc","fetchQuestionnaireSet"+ questionnaireSetRepository.getQuestionnaireTitle(getQuestionnaireSetTypeKey()));
        questionnaireSetServiceClient.fetchQuestionnaireSets(getQuestionnaireSetTypeKey(), questionnaireSetRepository);
    }

    @Override
    public void onUserInterfaceAppeared() {
        addEventListeners();

        fetchQuestionnaireSet();
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        removeEventListeners();
    }

    @Override
    public void onUserInterfaceDestroyed() {
    }



    @Override
    public void setUserInterface(QuestionnaireLandingUserInterface userInterface) {
        this.userInterface = userInterface;
    }

    protected abstract int getQuestionnaireSetTypeKey();
}
