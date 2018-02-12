package com.vitalityactive.va.shared.questionnaire.repository.model;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetResponse;

import io.realm.RealmObject;

public class QuestionDecoratorModel extends RealmObject implements Model{
    public long channelTypeKey; // Description of the channel A unique global feedbackKey that identifies the reference type that describes a specific context.  In this case it describes a specific channel type requesting a questionnaire  The feedbackKey is used to facilitate the correct implementation of descriptions across multiple language implementations  <b><i>Example</i></b> Mobile Web Output Management Component;
    public long typeKey; // A unique global feedbackKey that identifies the reference type that describes a specific context.  In this case it describes the type of decorator that should be applied for the question The feedbackKey is used to facilitate the correct implementation of descriptions across multiple language implementations;
    public String channelTypeCode; // The short name describing the associated "type feedbackKey" reference. It's purpose is to describe the unique feedbackKey to facilitate easy reference. The short name is used for easy context recognition for debugging but could also be used as a reference able item in itself ;
    public String typeName; // The extended name describing the associated "type feedbackKey" reference. It's purpose is to describe the "type feedbackKey" in detail to facilitate better understanding;
    public String channelTypeName; // The extended name describing the associated "type feedbackKey" reference. It's purpose is to describe the "type feedbackKey" in detail to facilitate better understanding;
    public String typeCode;

    public QuestionDecoratorModel() {}

    public static QuestionDecoratorModel create(QuestionnaireSetResponse.QuestionDecorator responseQuestionDecorator) {
        if (responseQuestionDecorator.channelTypeKey == null
                || responseQuestionDecorator.typeKey == null) {
            return null;
        }

        QuestionDecoratorModel questionDecoratorModel = new QuestionDecoratorModel();

        questionDecoratorModel.channelTypeKey = responseQuestionDecorator.channelTypeKey;
        questionDecoratorModel.channelTypeCode = responseQuestionDecorator.channelTypeCode;
        questionDecoratorModel.typeKey = responseQuestionDecorator.typeKey;
        questionDecoratorModel.channelTypeCode = responseQuestionDecorator.channelTypeCode;
        questionDecoratorModel.typeName = responseQuestionDecorator.typeName;
        questionDecoratorModel.channelTypeName = responseQuestionDecorator.channelTypeName;
        questionDecoratorModel.typeCode = responseQuestionDecorator.typeCode;

        return questionDecoratorModel;
    }
}
