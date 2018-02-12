package com.vitalityactive.va.mwb.landing;

/**
 * Created by christian.j.p.capin on 1/30/2018.
 */

public interface MWBVitalityAgePresenter {
    void setReturnfromFormSubmission(boolean isReturnfromFormSubmission);

    boolean shouldShowVitalityAge();

    void setHasShownVitalityAge();

    void setUserInterface(UserInterface userInterface);

    void fetchVitalityAge();

    void unRegisterVitalityAgeRequest();

    boolean isReturnfromFormSubmission();

    interface UserInterface {
        void onVitalityAgeResponse(boolean equals);
    }
}
