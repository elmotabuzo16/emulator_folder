package com.vitalityactive.va.wellnessdevices.linking.privacypolicy;

import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;

public interface PrivacyPolicyPresenter extends TermsAndConditionsPresenter<PrivacyPolicyPresenter.UserInterface> {
    void setPartner(PartnerDto partner);
    void linkDevice();

    interface UserInterface extends TermsAndConditionsPresenter.UserInterface {
        void redirectToPartnerWebSite(String redirectUrl);
        void navigateToPreviousScreenWithSuccessStatus();
        void showGenericLinkErrorMessage();
        void showConnectionLinkErrorMessage();
    }
}
