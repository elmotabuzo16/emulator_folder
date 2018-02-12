package com.referencedatacodegen;

import com.referencedatacodegen.csv.CSVCodeGen;
import com.referencedatacodegen.json.JSONCodeGen;

import java.io.IOException;

public class ReferenceDataCodeGen {
    public static void main(String[] args) throws IOException {
        generateTypesFromCSV();
        generateTypesFromJSON();
    }

    private static void generateTypesFromCSV() throws IOException {
        generateTypeFromCSVStartLineTWO("CardType.csv", "CardType", CSVCodeGen.FieldNameIndex.TWO);
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("CardStatusType.csv", "CardStatusType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("EventType.csv", "EventType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("CardMetadataType.csv", "CardMetadataType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("EventMetaDataType.csv", "EventMetadataType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("EventAssociationType.csv", "EventAssociationType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("EventSource.csv", "EventSource");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("QuestionnaireChannel.csv", "QuestionnaireChannel");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("QuestionnaireSet.csv", "QuestionnaireSet");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("Questionnaire.csv", "Questionnaire");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("QuestionType.csv", "QuestionType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("QuestionDecorator.csv", "QuestionDecorator");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("QuestionnaireFeedbackType.csv", "QuestionnaireFeedbackType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("QuestionValidValueType.csv", "QuestionValidValueType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("PointsEntryCategory.csv", "PointsEntryCategory");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("LinkType.csv", "ProductFeatureLinkType");
        generateTypeFromCSVStartLineONE("Product Feature.csv", "ProductFeature", CSVCodeGen.FieldNameIndex.ONE);
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("ProductFeatureType.csv", "ProductFeatureType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("AssessmentAnswerStatus.csv", "AssessmentAnswerStatus");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("AssessmentStatus.csv", "AssessmentStatus");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("QuestionnaireSections.csv", "QuestionnaireSections");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("PointsEntryType.csv", "PointsEntryType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("PartyAttributeFeedback.csv", "HealthAttributeFeedback");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("Goal.csv", "Goal");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("Reason.csv", "Reason");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("PartyAttributeType.csv", "PartyAttributeType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("StatusType.csv", "StatusType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("GoalTrackerStatus.csv", "GoalTrackerStatus");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("ProductFeatureCategoryType.csv", "ProductFeatureCategoryType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("ProductFeatureCategory.csv", "ProductFeatureCategory");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("PartyEmailRoleType.csv", "PartyEmailRoleType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("CardItemStatusType.csv", "CardItemStatusType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("CardItemType.csv", "CardItemType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("AwardedRewardStatus.csv", "AwardedRewardStatus");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("AwardedRewardReferenceType.csv", "AwardedRewardReferenceType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("RewardValueType.csv", "RewardValueType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("PreferenceType.csv", "PreferenceType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("RewardSelectionType.csv", "RewardSelectionType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("PartyReferenceType.csv", "PartyReferenceType");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("ProcessInstructionType.csv", "ProcessInstructionType");
        generateTypeFromCSVStartLineONE("Rewards in Test.csv", "RewardId", CSVCodeGen.FieldNameIndex.FIVE);
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("EventCategory.csv", "EventCategory");
        generateTypeFromCSVStartLineTWOFieldNameIndexONE("CardItemMetadataType.csv", "CardItemMetadataType");
    }

    private static void generateTypeFromCSVStartLineONE(String csvFileName, String javaFileName, CSVCodeGen.FieldNameIndex fieldNameIndex) throws IOException {
        generateTypeFromCSV(fieldNameIndex, csvFileName, javaFileName, CSVCodeGen.StartLine.ONE);
    }

    private static void generateTypeFromCSVStartLineTWO(String csvFileName, String javaFileName, CSVCodeGen.FieldNameIndex fieldNameIndex) throws IOException {
        generateTypeFromCSV(fieldNameIndex, csvFileName, javaFileName, CSVCodeGen.StartLine.TWO);
    }

    private static void generateTypeFromCSVStartLineTWOFieldNameIndexONE(String csvFileName, String javaFileName) throws IOException {
        generateTypeFromCSVStartLineTWO(csvFileName, javaFileName, CSVCodeGen.FieldNameIndex.ONE);
    }

    private static void generateTypeFromCSV(CSVCodeGen.FieldNameIndex fieldNameIndex, String csvFileName, String javaFileName, CSVCodeGen.StartLine startLine) throws IOException {
        CSVCodeGen.generateType(csvFileName, javaFileName, startLine, fieldNameIndex, CSVCodeGen.FieldValueIndex.ZERO);
    }

    private static void generateTypesFromJSON() throws IOException {
        JSONCodeGen.generateUnitOfMeasure();
    }

}
