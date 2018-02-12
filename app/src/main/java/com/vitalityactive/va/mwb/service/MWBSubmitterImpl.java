package com.vitalityactive.va.mwb.service;

import com.vitalityactive.va.constants.QuestionnaireSet;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.myhealth.dto.VitalityAgeRepository;
import com.vitalityactive.va.myhealth.entity.VitalityAge;
import com.vitalityactive.va.questionnaire.QuestionnaireStateManager;
import com.vitalityactive.va.shared.questionnaire.service.BaseQuestionnaireSubmitter;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmissionServiceClient;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmitResponse;

/**
 * Created by christian.j.p.capin on 1/30/2018.
 */

public class MWBSubmitterImpl extends BaseQuestionnaireSubmitter {
    private final VitalityAgeRepository vitalityAgeRepository;

    public MWBSubmitterImpl(QuestionnaireStateManager questionnaireStateManager,
                            QuestionnaireSubmissionServiceClient questionnaireSubmissionServiceClient,
                            EventDispatcher eventDispatcher, VitalityAgeRepository vitalityAgeRepository) {
        super(questionnaireStateManager, eventDispatcher, questionnaireSubmissionServiceClient);
        this.vitalityAgeRepository = vitalityAgeRepository;
    }

    @Override
    protected int getQuestionnaireSetType() {
        return QuestionnaireSet._MWB;
    }

    @Override
    public void parseResponse(QuestionnaireSubmitResponse questionnaireSubmitResponse) {
        super.parseResponse(questionnaireSubmitResponse);
        parseVitalityAgeFromResponse(questionnaireSubmitResponse);
    }

    private void parseVitalityAgeFromResponse(QuestionnaireSubmitResponse questionnaireSubmitResponse) {
        if (questionnaireSubmitResponse != null) {
            VitalityAge vitalityAge = getVitalityAgeAttribute(questionnaireSubmitResponse);
            if (vitalityAge != null) {
                vitalityAgeRepository.saveVitalityAgeValue(vitalityAge);
            }
        }
    }

    public VitalityAge getVitalityAgeAttribute(QuestionnaireSubmitResponse questionnaireSubmitResponse) {
        try {
            if (questionnaireSubmitResponse != null && questionnaireSubmitResponse.healthAttributeMetadatas != null && !questionnaireSubmitResponse.healthAttributeMetadatas.isEmpty()) {
                QuestionnaireSubmitResponse.HealthAttributeMetadata healthAttribute = questionnaireSubmitResponse.healthAttributeMetadatas.get(0);
                if (healthAttribute.healthAttributeFeedbacks != null && !healthAttribute.healthAttributeFeedbacks.isEmpty()) {
                    return new VitalityAge.Builder()
                            .age(healthAttribute.value)
                            .effectiveType(healthAttribute.healthAttributeFeedbacks.get(0).feedbackTypeKey)
                            .feedbackTitle(healthAttribute.healthAttributeFeedbacks.get(0).feedbackTypeName)
                            .feedbackContent(healthAttribute.healthAttributeFeedbacks.get(0).feedbackTypeName)
                            .build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
