package com.vitalityactive.va.shared.questionnaire.service;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.vitalityactive.va.UnitsOfMeasure;
import com.vitalityactive.va.constants.AssessmentAnswerStatus;
import com.vitalityactive.va.constants.AssessmentStatus;
import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.questionnaire.types.LabelWithAssociationsQuestionDto;
import com.vitalityactive.va.questionnaire.types.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class QuestionnaireSubmitRequestBody {
    @SerializedName("assessment")
    public Assessment[] assessments;

    public QuestionnaireSubmitRequestBody(long questionnaireTypeKey, @NonNull List<Question> questions) {

        AssessmentAnswer[] assessmentAnswers = new AssessmentAnswer[questions.size() - countParentQuestions(questions)];
        @SuppressLint("UseSparseArrays")
        Map<Integer, Answer> childTypeKeyToAnswerValue = new HashMap<>();
        int index = 0;

        for (int i = 0; i < questions.size(); ++i) {
            Question question = questions.get(i);

            if (question.getAssociatedChildTypeKeys() != null && question.getAssociatedChildTypeKeys().size() > 0) {
                for (int typeKey : question.getAssociatedChildTypeKeys()) {
                    childTypeKeyToAnswerValue.put(typeKey, ((LabelWithAssociationsQuestionDto) question).getAnswerByTypeKey(typeKey));
                }
                continue;
            }

            if (question.isChildQuestion()) {
                assessmentAnswers[index++] = new AssessmentAnswer(question.getQuestionTypeKey(), question.getIdentifier(), childTypeKeyToAnswerValue.get((int) question.getIdentifier()));
            } else {
                assessmentAnswers[index++] = new AssessmentAnswer(question.getQuestionTypeKey(), question.getIdentifier(), question.getAnswer());
            }
        }
        assessments = new Assessment[]{new Assessment(questionnaireTypeKey, assessmentAnswers)};
    }


    private int countParentQuestions(List<Question> questions) {
        int counter = 0;
        for (Question question : questions) {
            if (isParentQuestion(question)) {
                counter++;
            }
        }
        return counter;
    }

    private boolean isParentQuestion(Question question) {
        return question.getAssociatedChildTypeKeys() != null && question.getAssociatedChildTypeKeys().size() > 0;
    }

    public static class Assessment {
        @SerializedName("typeKey")
        public long typeKey;
        @SerializedName("statusTypeKey")
        public long statusTypeKey = AssessmentStatus._NEW;

        @SerializedName("assessmentAnswer")
        public AssessmentAnswer[] assessmentAnswers;

        public Assessment(long questionnaireTypeKey, AssessmentAnswer[] assessmentAnswers) {
            typeKey = questionnaireTypeKey;
            this.assessmentAnswers = assessmentAnswers;
        }
    }

    public static class AssessmentAnswer {
        @SerializedName("answerStatusTypeKey")
        public long answerStatusTypeKey = AssessmentAnswerStatus._ANSWERED;
        @SerializedName("questionTypeKey")
        public long questionIdentifier;
        @SerializedName("selectedValues")
        public SelectedValue[] selectedValues;

        public AssessmentAnswer(long valueType, long questionIdentifier, Answer answer) {
            this.questionIdentifier = questionIdentifier;

            List<String> values = answer.getValues();

            selectedValues = new SelectedValue[values.size()];
            for (int i = 0; i < values.size(); i++) {
                selectedValues[i] = (new SelectedValue(valueType, values.get(i), answer.getUnitOfMeasureKey()));
            }
        }

        public class SelectedValue {
            @SerializedName("valueType")
            public long valueType;
            @SerializedName("value")
            public String value;
            @SerializedName("unitOfMeasure")
            public String unitOfMeasure;

            public SelectedValue(long valueType, String value, String unitOfMeasure) {
                this.valueType = valueType;
                this.value = prepareFtInorStLbResponses(value, unitOfMeasure);
                this.unitOfMeasure = unitOfMeasure;
            }

            private String prepareFtInorStLbResponses(String initialValue, String unitOfMeasure) {

                if (TextUtils.isEmpty(unitOfMeasure)) return initialValue;

                //TODO: Adjust by using stringBuilder instead

                if (unitOfMeasure.equals(UnitsOfMeasure.FOOTINCH.getTypeKey())) {
                    Log.d("Unit Value", "FT In Ultimate value " + initialValue);
                    String[] slpitValue = initialValue.split(Pattern.quote("."));

                    if (slpitValue.length == 0) {
                        slpitValue = new String[2];
                        slpitValue[0] = "0";
                        slpitValue[1] = "0";
                    }

                    if (slpitValue.length == 1) {
                        String initValue = slpitValue[0];
                        slpitValue = new String[2];
                        slpitValue[0] = initValue;
                        slpitValue[1] = "0";
                    }

                    return slpitValue[0].trim() + " F " + slpitValue[1].trim() + ".0 INCH";
                }

                if (unitOfMeasure.equals(UnitsOfMeasure.STONEPOUND.getTypeKey())) {
                    Log.d("Unit Value", "ST LB Ultimate value " + initialValue);
                    String[] slpitValue = initialValue.split(Pattern.quote("."));

                    if (slpitValue.length == 0) {
                        slpitValue = new String[2];
                        slpitValue[0] = "0";
                        slpitValue[1] = "0";
                    }

                    if (slpitValue.length == 1) {
                        String initValue = slpitValue[0];
                        slpitValue = new String[2];
                        slpitValue[0] = initValue;
                        slpitValue[1] = "0";
                    }

                    return slpitValue[0].trim() + " ST " + slpitValue[1].trim() + ".0 LB";
                }

                return initialValue;
            }
        }
    }


}
