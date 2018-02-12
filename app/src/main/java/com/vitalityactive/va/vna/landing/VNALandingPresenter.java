package com.vitalityactive.va.vna.landing;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.constants.QuestionnaireSet;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.shared.questionnaire.QuestionnaireLandingPresenterBase;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetServiceClient;
import com.vitalityactive.va.vna.content.VNAContent;

public class VNALandingPresenter extends QuestionnaireLandingPresenterBase {
    public VNALandingPresenter(VNAContent content, QuestionnaireSetServiceClient serviceClient,
                               EventDispatcher eventDispatcher, MainThreadScheduler scheduler,
                               QuestionnaireSetRepository questionnaireSetRepository)
    {
        super(content, scheduler, eventDispatcher, serviceClient, questionnaireSetRepository);
    }

    @Override
    protected int getQuestionnaireSetTypeKey() {
        return QuestionnaireSet._VNA;
    }
}
