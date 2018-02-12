package com.vitalityactive.va.wellnessdevices.landing;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.service.EventType;

import java.util.List;

public interface WellnessDevicesLandingPresenter extends Presenter<WellnessDevicesLandingPresenter.UserInterface> {
//    boolean hasCachedData();
    void updateDeviceList(boolean showFullScreenIndicator);

    void loadDeviceDetails(int typeKey);
    EventType getPotentialPoints();
    List<EventType> getChildEventTypes();
    boolean manufacturerContainsTypeKey(int typeKey);
    EventType getEventType(int typeKey);

    void fetchDeviceActivityMapping();

    interface UserInterface {
        void updateLinkedDevices(List<PartnerDto> available, List<PartnerDto> linked);
        void showLoadingIndicator();
        void hideLoadingIndicator();
        void showConnectionErrorMessage(@WellnessDevicesLandingInteractor.RequestType String requestType);
        void showGenericErrorMessage(@WellnessDevicesLandingInteractor.RequestType String requestType);
        void handleSuccessfullPointsResponse();
    }
}
