package com.vitalityactive.va.home.interactor;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.home.cards.HomeScreenCardSection;
import com.vitalityactive.va.home.events.GetCardCollectionResponseEvent;
import com.vitalityactive.va.home.events.GetEventStatusByPartyIdResponseEvent;
import com.vitalityactive.va.vitalitystatus.VitalityStatusDTO;

import java.util.List;

public interface HomePresenter extends Presenter<HomePresenter.Ui> {
    void retryLoadingHomeScreenCardSections();
    void startCheckingVhrStatus();

    interface Ui {
        void onHomeScreenCardSectionsReady(List<HomeScreenCardSection> sections);
        void onHomeScreenCardSectionsFailed(GetCardCollectionResponseEvent event);
        void showLoadingIndicator();
        void hideLoadingIndicator();
        void navigateToVhrLandingScreen();
        void navigateToActiveRewardsLanding();
        void showVhrStatusValidationFailedAlert(GetEventStatusByPartyIdResponseEvent event);
        void setStatusInfo(VitalityStatusDTO vitalityStatus);
        void handleStatusLevelIncreased();
    }
}
