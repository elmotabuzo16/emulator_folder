package com.vitalityactive.va.questionnaire;

import android.support.annotation.NonNull;

import com.vitalityactive.va.UnitsOfMeasure;

public interface QuestionnaireUnitOfMeasureContent {
    @NonNull
    String getUnitOfMeasureDisplayName(UnitsOfMeasure unitOfMeasure);

    @NonNull
    String getUnitOfMeasureSymbol(UnitsOfMeasure unitOfMeasure);
}
