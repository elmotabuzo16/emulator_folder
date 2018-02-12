package com.vitalityactive.va.mwb.landing;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.constants.QuestionnaireSet;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.mwb.content.MWBContent;
import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.shared.questionnaire.QuestionnaireLandingPresenterBase;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetServiceClient;
import com.vitalityactive.va.vhr.content.VHRContent;

/**
 * Created by christian.j.p.capin on 1/30/2018.
 */

public class MWBLandingPresenterImpl extends QuestionnaireLandingPresenterBase {


    public MWBLandingPresenterImpl(MWBContent content,
                                   QuestionnaireSetServiceClient questionnaireSetServiceClient,
                                   EventDispatcher eventDispatcher,
                                   Scheduler scheduler,
                                   QuestionnaireSetRepository questionnaireSetRepository) {
        super(content, scheduler, eventDispatcher, questionnaireSetServiceClient, questionnaireSetRepository);
    }

    @Override
    protected int getQuestionnaireSetTypeKey() {
        return QuestionnaireSet._MWB;
    }

}