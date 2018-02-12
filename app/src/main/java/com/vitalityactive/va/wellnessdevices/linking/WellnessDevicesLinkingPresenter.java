package com.vitalityactive.va.wellnessdevices.linking;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.wellnessdevices.dto.AssetsDto;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;
import com.vitalityactive.va.wellnessdevices.dto.PotentialPointsDto;

public interface WellnessDevicesLinkingPresenter extends Presenter<WellnessDevicesLinkingPresenter.UserInterface> {
    void setPartner(PartnerDto partner);
    PartnerDto getPartner();
    AssetsDto getAssets();

    boolean shouldShowWDPrivacyPolicy();

    void linkDevice();
    void delinkDevice();

    void sendUpdateTokenRequest();

    boolean manufacturerContainsTypeKey(int typeKey);
    PotentialPointsDto getPotentialPoints(int typeKey);

    void setLinked(boolean isLinked);
    boolean isLinked();

    interface UserInterface {
        void updateUi();
        void showLoadingIndicator();
        void showProgressWithText(@WellnessDevicesLinkingInteractor.RequestType String requestType);
        void hideLoadingIndicator();
        void showConnectionErrorMessage(@WellnessDevicesLinkingInteractor.RequestType String requestType);
        void showGenericErrorMessage(@WellnessDevicesLinkingInteractor.RequestType String requestType);
        void showLinkSuccessfullMessage();
        void showDelinkSuccessfullMessage();
        void redirectToPartnerWebSite(String redirectUrl, @WellnessDevicesLinkingInteractor.RequestType String requestType);
        void showDelinkConfirmationDialog();
    }
}