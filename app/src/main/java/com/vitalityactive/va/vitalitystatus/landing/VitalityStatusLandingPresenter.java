package com.vitalityactive.va.vitalitystatus.landing;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.home.events.GetProductFeaturePointsResponseEvent;
import com.vitalityactive.va.vitalitystatus.VitalityStatusDTO;
import com.vitalityactive.va.vitalitystatus.earningpoints.PointsInformationDTO;

import java.util.List;

public interface VitalityStatusLandingPresenter extends Presenter<VitalityStatusLandingPresenter.UserInterface> {

    void retryLoading();
    boolean requestInProgress();
    boolean hasFeatureCategories();

    interface UserInterface {
        void displayContent(VitalityStatusDTO currentLevel, boolean shouldShowMyRewards);
        void displayProductFeaturePoints(List<PointsInformationDTO> pointsInformationDTOs);
        void showError(GetProductFeaturePointsResponseEvent event);
    }
}
