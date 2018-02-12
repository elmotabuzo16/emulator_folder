package com.vitalityactive.va.shared.questionnaire.service;

import com.google.gson.annotations.SerializedName;
import com.vitalityactive.va.constants.QuestionnaireChannel;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
class QuestionnaireSetRequestBody {
    @SerializedName("questionnaires")
    private Questionnaire[] questionnaires;
    @SerializedName("questionnaireChannelTypeKey")
    private long questionnaireChannelTypeKey;
    @SerializedName("prePopulation")
    private boolean prePopulation = true;
    @SerializedName("questionnaireSetTypeKey")
    private Long questionnaireSetTypeKey;

    public QuestionnaireSetRequestBody(long questionnaireSetTypeKey, Integer[] questionnaireTypeKeys) {
        if (questionnaireTypeKeys != null) {
            questionnaires = new Questionnaire[questionnaireTypeKeys.length];

            for (int i = 0; i < questionnaireTypeKeys.length; i++)
                questionnaires[i] = new Questionnaire(questionnaireTypeKeys[i]);
        }

        this.questionnaireSetTypeKey = questionnaireSetTypeKey;
        questionnaireChannelTypeKey = QuestionnaireChannel._MOBILE;
    }

    private class Questionnaire {
        @SerializedName("typeKey")
        Integer typeKey;

        public Questionnaire(Integer typeKey) {
            this.typeKey = typeKey;
        }
    }
}
