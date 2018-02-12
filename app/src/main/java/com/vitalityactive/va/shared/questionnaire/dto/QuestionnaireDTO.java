package com.vitalityactive.va.shared.questionnaire.dto;

import com.vitalityactive.va.activerewards.dto.TitleAndSubtitle;

public class QuestionnaireDTO extends TitleAndSubtitle {
    private final Boolean completionFlag;
    private final boolean inProgress;
    private final long typeKey;
    public String earnedPointsDescription;

    public QuestionnaireDTO(String title,
                            String description,
                            Boolean completionFlag,
                            boolean inProgress,
                            long typeKey) {
        super(title, description);

        this.completionFlag = completionFlag;
        this.inProgress = inProgress;
        this.typeKey = typeKey;
    }



    public boolean isCompleted() {
        return completionFlag;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public long getTypeKey() {
        return typeKey;
    }
}
