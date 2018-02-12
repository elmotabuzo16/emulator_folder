package com.vitalityactive.va.shared.questionnaire.service;//
//  QuestionnaireSetResponse.java
//  VitalityActive
//
//  Created by Swagger Codegen on 05/16/2017.
//  Copyright © 2017 Glucode. All rights reserved.

//  Don't modify this file

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.annotations.Required;

public final class QuestionnaireSetResponse {
    @SerializedName("questionnaire")
    public List<Questionnaire> questionnaire ;
    @SerializedName("questionnaireSetCompleted")
    @Required
    public Boolean questionnaireSetCompleted; // Indicator if the set of questions have been completed;
    @SerializedName("questionnaireSetTypeCode")
    public String questionnaireSetTypeCode; // The short name describing the associated "type feedbackKey" reference. It's purpose is to describe the unique feedbackKey to facilitate easy reference. The short name is used for easy context recognition for debugging but could also be used as a reference able item in itself;
    @SerializedName("questionnaireSetTypeKey")
    @Required
    public Integer questionnaireSetTypeKey; // A unique global feedbackKey that identifies the reference type that describes a specific context.  In this case it describes a specific type questionnaire category The feedbackKey is used to facilitate the correct implementation of descriptions across multiple language implementations  <b><i>Example</i></b> VHR - Vitality Health Review VNA - Vitality Nutrition Assessment;
    @SerializedName("questionnaireSetTypeName")
    public String questionnaireSetTypeName; // The extended name describing the associated "type feedbackKey" reference. It's purpose is to describe the "type feedbackKey" in detail to facilitate better understanding;
    @SerializedName("setText")
    public String setText ;
    @SerializedName("setTextDescription")
    public String setTextDescription ;
    @SerializedName("setTextNote")
    public String setTextNote ;
    @SerializedName("totalEarnedPoints")
    @Required
    public Integer totalEarnedPoints; // Total points earned ;
    @SerializedName("totalPotentialPoints")
    @Required
    public Integer totalPotentialPoints; // Total potential points;
    @SerializedName("totalQuestionnaireCompleted")
    @Required
    public Integer totalQuestionnaireCompleted; // Total amount of questionnaires completed ;
    @SerializedName("totalQuestionnaires")
    @Required
    public Integer totalQuestionnaires; // Total amount of questionnaires;

    public final class Questionnaire {
        @SerializedName("completedOn")
        public String completedOn; // Date the questionnaire was completed ;
        @SerializedName("completionFlag")
        @Required
        public Boolean completionFlag; // Flag indicating of the Questionnaire has been completed ;
        @SerializedName("questionnaireSections")
        public List<QuestionnaireSections> questionnaireSections; // This is the section/s that a questionnaire may be broken down into. Some questionnaires may not have sections.;
        @SerializedName("sortOrderIndex")
        @Required
        public Integer sortOrderIndex; // An index indicating the order in which the questionnaire should be displayed;
        @SerializedName("text")
        public String text ;
        @SerializedName("textDescription")
        public String textDescription ;
        @SerializedName("textNote")
        public String textNote ;
        @SerializedName("typeCode")
        @Required
        public String typeCode; // The short name describing the associated "type feedbackKey" reference. It's purpose is to describe the unique feedbackKey to facilitate easy reference. The short name is used for easy context recognition for debugging but could also be used as a reference able item in itself;
        @SerializedName("typeKey")
        @Required
        public Integer typeKey; // A unique global feedbackKey that identifies the reference type that describes a specific context.  In this case it describes a specific questionnaire type  The feedbackKey is used to facilitate the correct implementation of descriptions across multiple language implementations  <b><i>Example</i></b> Well being Questionnaire social Well being Questionnaire;
        @SerializedName("typeName")
        public String typeName; // The extended name describing the associated "type feedbackKey" reference. It's purpose is to describe the "type feedbackKey" in detail to facilitate better understanding;
    }

    public final class QuestionnaireSections {
        @SerializedName("isVisible")
        @Required
        public  Boolean isVisible; // Value assigned in order to indicate that the section is to be made visible for the specific questionnaire for a particular market;
        @SerializedName("questions")
        public  List<Questions> questions ;
        @SerializedName("sortOrderIndex")
        @Required
        public  Integer sortOrderIndex; // An index indicating the order in which the questionnaire section should be displayed;
        @SerializedName("text")
        public  String text ;
        @SerializedName("textDescription")
        public  String textDescription ;
        @SerializedName("textNote")
        public  String textNote ;
        @SerializedName("typeCode")
        public  String typeCode; // The short name describing the associated "type feedbackKey" reference. It's purpose is to describe the unique feedbackKey to facilitate easy reference. The short name is used for easy context recognition for debugging but could also be used as a reference able item in itself;
        @SerializedName("typeKey")
        @Required
        public  Integer typeKey ; // A unique global feedbackKey that identifies the reference type that describes a specific context.  In this case it describes a specific questionnaire section The feedbackKey is used to facilitate the correct implementation of descriptions across multiple language implementations;
        @SerializedName("typeName")
        public  String typeName; // The extended name describing the associated "type feedbackKey" reference. It's purpose is to describe the "type feedbackKey" in detail to facilitate better understanding;
        @SerializedName("visibilityTagName")
        @Required
        public  String visibilityTagName; // A value which describes what criteria needs to be complied with in order for the questionnaire to become active;
    }

    public final class Questions {
        @SerializedName("format")
        public  String format; // The expected format of the input value;
        @SerializedName("length")
        public  Integer length; // The length of the expected input value;
        @SerializedName("populationValues")
        public  List<PopulationValue> populationValues ;
        @SerializedName("questionAssociations")
        public  List<QuestionAssociations> questionAssociations ;
        @SerializedName("questionDecorator")
        public  QuestionDecorator questionDecorator ;
        @SerializedName("questionTypeCode")
        public  String questionTypeCode; // The short name describing the associated "type feedbackKey" reference. It's purpose is to describe the unique feedbackKey to facilitate easy reference. The short name is used for easy context recognition for debugging but could also be used as a reference able item in itself;
        @SerializedName("questionTypeKey")
        @Required
        public  Long questionTypeKey; // A unique global feedbackKey that identifies the reference type that describes a specific context.  The feedbackKey is used to facilitate the correct implementation of descriptions across multiple language implementations;
        @SerializedName("questionTypeName")
        public  String questionTypeName; // The extended name describing the associated "type feedbackKey" reference. It's purpose is to describe the "type feedbackKey" in detail to facilitate better understanding;
        @SerializedName("sortOrderIndex")
        @Required
        public  Integer sortOrderIndex; // An index indicating the order in which the questionnaire should be displayed;
        @SerializedName("text")
        public  String text ;
        @SerializedName("textDescription")
        public  String textDescription ;
        @SerializedName("textNote")
        public  String textNote ;
        @SerializedName("typeCode")
        public  String typeCode; // The short name describing the associated "type feedbackKey" reference. It's purpose is to describe the unique feedbackKey to facilitate easy reference. The short name is used for easy context recognition for debugging but could also be used as a reference able item in itself;
        @SerializedName("typeKey")
        @Required
        public  Integer typeKey; // A unique global feedbackKey that identifies the reference type that describes a specific context. In this case it describes a specific question  The feedbackKey is used to facilitate the correct implementation of descriptions across multiple language implementations;
        @SerializedName("typeName")
        public  String typeName; // The extended name describing the associated "type feedbackKey" reference. It's purpose is to describe the "type feedbackKey" in detail to facilitate better understanding;
        @SerializedName("unitOfMeasures")
        public  List<UnitOfMeasure> unitOfMeasures ;
        @SerializedName("validValues")
        public  List<ValidValues> validValues ;
        @SerializedName("visibilityTagName")
        public  String visibilityTagName; // A value which describes what criteria needs to be complied with in order to unlock a question or questionnaire section;
    }

    public final class QuestionDecorator {
        @SerializedName("channelTypeCode")
        public  String channelTypeCode; // The short name describing the associated "type feedbackKey" reference. It's purpose is to describe the unique feedbackKey to facilitate easy reference. The short name is used for easy context recognition for debugging but could also be used as a reference able item in itself ;
        @SerializedName("channelTypeKey")
        @Required
        public  Integer channelTypeKey; // Description of the channel A unique global feedbackKey that identifies the reference type that describes a specific context.  In this case it describes a specific channel type requesting a questionnaire  The feedbackKey is used to facilitate the correct implementation of descriptions across multiple language implementations  <b><i>Example</i></b> Mobile Web Output Management Component;
        @SerializedName("channelTypeName")
        public  String channelTypeName; // The extended name describing the associated "type feedbackKey" reference. It's purpose is to describe the "type feedbackKey" in detail to facilitate better understanding;
        @SerializedName("typeCode")
        public  String typeCode; // The short name describing the associated "type feedbackKey" reference. It's purpose is to describe the unique feedbackKey to facilitate easy reference. The short name is used for easy context recognition for debugging but could also be used as a reference able item in itself;
        @SerializedName("typeKey")
        @Required
        public  Integer typeKey; // A unique global feedbackKey that identifies the reference type that describes a specific context.  In this case it describes the type of decorator that should be applied for the question The feedbackKey is used to facilitate the correct implementation of descriptions across multiple language implementations;
        @SerializedName("typeName")
        public  String typeName; // The extended name describing the associated "type feedbackKey" reference. It's purpose is to describe the "type feedbackKey" in detail to facilitate better understanding;
        @SerializedName("unitOfMeasures")
        public  List<UnitOfMeasure> unitOfMeasures ;
    }

    public final class PopulationValue {
        @SerializedName("fromValue")
        public String fromValue; // The from value for an answer that is associated with a range;
        @SerializedName("toValue")
        public String toValue; // The to value for an answer that is associated with a range;
        @SerializedName("unitOfMeasure")
        public String unitOfMeasure; // Unit of Measure associated with value returned;
        @SerializedName("valueType")
        public String valueType; // The type of the value returned <b><i>Example</i></b> Date String Long Double;
        @SerializedName("value")
        public String value; // The value for an answer that is associated with a single number or character set;
        @SerializedName("eventCode")
        public  String eventCode;
        @SerializedName("eventDate")
        public  String eventDate;
        @SerializedName("eventKey")
        public int eventKey;
        @SerializedName("eventName")
        public  String eventName;
    }

    public final class UnitOfMeasure {
        @SerializedName("value")
        @Required
        public  String value; // Unit of Measure associated with valid value or with the question itself;
    }

    public final class ValidValues {
        @SerializedName("description")
        public  String description ;
        @SerializedName("name")
        @Required
        public  String name; // The name of valid value;
        @SerializedName("note")
        public  String note ;
        @SerializedName("unitOfMeasure")
        public  UnitOfMeasure unitOfMeasure ;
        @SerializedName("validValueTypes")
        public  List<ValidValueType> validValueTypes ;
        @SerializedName("value")
        @Required
        public  String value; // the valid value;
    }

    public final class ValidValueType {
        @SerializedName("typeCode")
        public  String typeCode; // The short name related to the "type feedbackKey" referenced. It describes the unique feedbackKey for easy reference purposes. The short name is used for easy context recognition. assists in debugging but could also be used as a reference when applicable;
        @SerializedName("typeKey")
        @Required
        public  Integer typeKey; // A unique global feedbackKey that identifies the correct type reference for a specific environment. Used to facilitate the correct implementation of multilanguage. It is in essence the numerical specification of a Type attribute for a specific functional reference;
        @SerializedName("typeName")
        public  String typeName; // The extended name related to the "type Code". The name is used to describe the feedbackKey's function in more detail;
    }

    public final class QuestionAssociations {
        @SerializedName("childQuestionKey")
        @Required
        public Integer childQuestionKey;
        @SerializedName("sortIndex")
        @Required
        public Integer sortIndex;
    }
}
