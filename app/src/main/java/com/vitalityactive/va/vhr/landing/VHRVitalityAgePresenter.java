package com.vitalityactive.va.vhr.landing;


public interface VHRVitalityAgePresenter {

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
