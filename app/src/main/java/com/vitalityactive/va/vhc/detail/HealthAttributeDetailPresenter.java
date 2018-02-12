package com.vitalityactive.va.vhc.detail;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.vhc.dto.HealthAttributeDTO;

import java.util.Date;
import java.util.List;

public interface HealthAttributeDetailPresenter
        extends Presenter<HealthAttributeDetailPresenter.UserInterface> {

    void setHealthAttributeGroupFeatureType(int healthAttributeGroupFeatureType);

    interface UserInterface {
        void showLoadingIndicator();

        void hideLoadingIndicator();

        void setActionBarTitle(String groupDescription);

        void setUpRecyclerView(List<HealthAttributeDTO> healthAttributeGroup, Date membershipPeriodStart, Date membershipPeriodEnd, String groupDescription);
    }
}
