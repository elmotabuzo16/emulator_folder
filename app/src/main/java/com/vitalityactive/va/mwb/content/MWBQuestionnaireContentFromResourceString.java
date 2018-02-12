package com.vitalityactive.va.mwb.content;

import android.content.Context;

import com.vitalityactive.va.shared.UnitOfMeasureStringLoader;

/**
 * Created by christian.j.p.capin on 1/30/2018.
 */

public class MWBQuestionnaireContentFromResourceString extends UnitOfMeasureStringLoader implements MWBQuestionnaireContent {
    public MWBQuestionnaireContentFromResourceString(Context activeApplication) {
        super(activeApplication);
    }
}
