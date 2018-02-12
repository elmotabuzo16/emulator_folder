package com.vitalityactive.va.vitalitystatus.landing;

import com.vitalityactive.va.vitalitystatus.VitalityStatusDTO;
import com.vitalityactive.va.vitalitystatus.earningpoints.PointsInformationDTO;

import java.util.List;

public interface StatusLandingInteractor {
    void fetchProductFeaturePoints();
    VitalityStatusDTO loadVitalityStatus();
    boolean shouldShowMyRewards();
    boolean hasFeaturePointsData();
    List<PointsInformationDTO> loadPointsCategories();
    boolean requestInProgress();
}
