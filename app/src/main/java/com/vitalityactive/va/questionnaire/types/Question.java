package com.vitalityactive.va.questionnaire.types;

import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.questionnaire.VisibilityDependsOnQuestionAnswer;
import com.vitalityactive.va.questionnaire.dependencies.DependencyRuleSet;

import java.util.List;
import java.util.Objects;

public abstract class Question implements VisibilityDependsOnQuestionAnswer {
    private final String title;
    private final String detail;
    private final String footer;
    public final long sectionTypeKey;
    private long id;
    private float sortOrder;
    private final int maxLength;
    private boolean canBeAnswered;
    private DependencyRuleSet dependencyRules;
    private long questionTypeKey;
    private boolean prePopulatedAnswer;
    private long prePopulatedDate;
    private int prePopulatedSource;
    private boolean isChildQuestion = false;
    private List<Integer> associatedChildTypeKeys;

    public Question(long id, long questionTypeKey, long sectionTypeKey, String title, String detail, String footer, float sortOrder) {
        this(id, questionTypeKey, sectionTypeKey, title, detail, footer, sortOrder, 0);
    }

    public Question(long id, long questionTypeKey, long sectionTypeKey, String title, String detail, String footer, float sortOrder, int maxLength) {
        this.id = id;
        this.questionTypeKey = questionTypeKey;
        this.sectionTypeKey = sectionTypeKey;
        this.title = title;
        this.detail = detail;
        this.footer = footer;
        this.sortOrder = sortOrder;
        this.maxLength = maxLength;
    }

    public long getIdentifier() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return detail;
    }

    public String getFooter() {
        return footer;
    }

    public abstract QuestionType getQuestionType();

    @Override
    public boolean getCanBeAnswered() {
        return canBeAnswered;
    }

    @Override
    public void setCanBeAnswered(boolean canBeAnswered) {
        this.canBeAnswered = canBeAnswered;
    }

    public float getSortOrder() {
        return sortOrder;
    }

    public abstract Answer getAnswer();

    public abstract boolean isAnswered();

    @Override
    public DependencyRuleSet getDependencyRules() {
        return dependencyRules;
    }

    Question setDependencyRules(DependencyRuleSet dependencyRules) {
        this.dependencyRules = dependencyRules;
        return this;
    }

    abstract void loadAnswer(Answer answer);

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Question && id == (((Question) obj).id);
    }

    public long getQuestionTypeKey() {
        return questionTypeKey;
    }

    public boolean shouldCreateDefaultAnswer() {
        return false;
    }

    public void hasAnAnswerSavedButNoValues() {
    }

    public int getMaxLength() {
        return maxLength;
    }

    public boolean isPrePopulatedAnswer() {
        return prePopulatedAnswer;
    }

    void setPrePopulatedAnswer(long prePopulatedDate) {
        this.prePopulatedDate = prePopulatedDate;
        prePopulatedAnswer = true;
    }

    public long getPrePopulatedDate() {
        return prePopulatedDate;
    }

    public int getPrePopulatedSource() {
        return prePopulatedSource;
    }

    public void setPrePopulatedSource(int prePopulatedSource) {
        this.prePopulatedSource = prePopulatedSource;
    }

    public List<Integer> getAssociatedChildTypeKeys() {
        return associatedChildTypeKeys;
    }

    public void setAssociatedChildTypeKeys(List<Integer> associatedChildTypeKeys) {
        this.associatedChildTypeKeys = associatedChildTypeKeys;
    }

    public boolean isChildQuestion() {
        return isChildQuestion;
    }

    public void setChildQuestion(boolean childQuestion) {
        isChildQuestion = childQuestion;
    }
}
