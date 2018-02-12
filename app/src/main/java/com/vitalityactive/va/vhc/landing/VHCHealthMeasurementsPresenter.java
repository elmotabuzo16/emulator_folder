package com.vitalityactive.va.vhc.landing;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.vhc.dto.HealthAttributeGroupDTO;

import java.util.List;

public interface VHCHealthMeasurementsPresenter
        extends Presenter<VHCHealthMeasurementsPresenter.UserInterface> {
    void loadAttributes();

    void fetchAttributesFromService();

    boolean loadHealthAttributesFromRepository();

    boolean shouldShowVitalityAgeVHRPrompt();

    void setVHCHasCompletedBefore();

    void onCompletedHealthAttributeGroupTapped(HealthAttributeGroupDTO item);

    void setReturnfromFormSubmission(boolean isCompletingFormSubmittion);

    interface UserInterface {
        void showLoadingIndicator();

        void hideLoadingIndicator();

        void onHealthAttributesReady(List<HealthAttributeGroupDTO> incomplete, List<HealthAttributeGroupDTO> captured);

        void showHealthAttributeGroupDetails(int featureType);

        void showGenericError();

        void showConnectionError();

        void setUpPullToRefresh();

        void navigateToVitalityAge();
    }
}
