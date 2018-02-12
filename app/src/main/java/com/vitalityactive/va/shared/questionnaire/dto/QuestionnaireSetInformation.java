package com.vitalityactive.va.shared.questionnaire.dto;

public class QuestionnaireSetInformation {
    private int totalQuestionnaireCompleted;
    private final Boolean questionnaireSetCompleted;
    private int totalQuestionnaires;
    private int totalPotentialPoints;
    private final String footer;
    private final String description;
    private final String text;
    private final Integer totalEarnedPoints;

    public QuestionnaireSetInformation(Integer totalQuestionnairesCompleted,
                                       Boolean setIsCompleted,
                                       Integer totalPotentialPoints,
                                       Integer totalQuestionnaires,
                                       String footer,
                                       String description,
                                       String text,
                                       Integer totalEarnedPoints) {
        this.totalQuestionnaireCompleted = totalQuestionnairesCompleted;
        this.questionnaireSetCompleted = setIsCompleted;
        this.totalQuestionnaires = totalQuestionnaires;
        this.totalPotentialPoints = totalPotentialPoints;
        this.footer = footer;
        this.description = description;
        this.text = text;
        this.totalEarnedPoints = totalEarnedPoints;
    }

    public int totalCompleted() {
        return totalQuestionnaireCompleted;
    }

    public boolean isCompleted() {
        return questionnaireSetCompleted;
    }

    public int getTotalQuestionnaires() {
        return totalQuestionnaires;
    }

    public int getTotalPotentialPoints() {
        return totalPotentialPoints;
    }

    public String getFooter() {
        return footer;
    }

    public String getDescription() {
        return description;
    }

    public String getText() {
        return text;
    }

    public Integer getTotalEarnedPoints() {
        return totalEarnedPoints;
    }
}
