package com.vitalityactive.va.vhr.landing;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.constants.QuestionnaireSet;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.shared.questionnaire.QuestionnaireLandingPresenterBase;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetServiceClient;
import com.vitalityactive.va.vhr.content.VHRContent;

public class VHRLandingPresenterImpl extends QuestionnaireLandingPresenterBase {


    public VHRLandingPresenterImpl(VHRContent content,
                                   QuestionnaireSetServiceClient questionnaireSetServiceClient,
                                   EventDispatcher eventDispatcher,
                                   Scheduler scheduler,
                                   QuestionnaireSetRepository questionnaireSetRepository) {
        super(content, scheduler, eventDispatcher, questionnaireSetServiceClient, questionnaireSetRepository);
    }

    @Override
    protected int getQuestionnaireSetTypeKey() {
        return QuestionnaireSet._VHR;
    }

}
