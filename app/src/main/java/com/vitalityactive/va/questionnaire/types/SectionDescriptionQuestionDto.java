package com.vitalityactive.va.questionnaire.types;

public class SectionDescriptionQuestionDto extends LabelQuestionDto {
    SectionDescriptionQuestionDto(long id,
                                  long questionTypeKey,
                                  long sectionTypeKey,
                                  String title,
                                  String detail,
                                  String footer,
                                  float sortOrder) {
        super(id, questionTypeKey, sectionTypeKey, title, detail, footer, sortOrder);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.SECTION_DESCRIPTION;
    }
}
