package com.vitalityactive.va.userpreferences.learnmore.presenter;


public class ShareVitalityLearnMoreSuccessEvent {

    protected static String templateHTML;

    public ShareVitalityLearnMoreSuccessEvent(String templateHTML) {
        this.templateHTML = templateHTML;
    }

    public String getTemplateHTML() {
        return templateHTML;
    }
}
