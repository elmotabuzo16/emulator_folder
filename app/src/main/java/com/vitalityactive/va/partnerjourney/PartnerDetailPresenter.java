package com.vitalityactive.va.partnerjourney;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.partnerjourney.repository.PartnerRepository;
import com.vitalityactive.va.partnerjourney.service.PartnerDetailRequestEvent;
import com.vitalityactive.va.partnerjourney.service.PartnerServiceClient;

public class PartnerDetailPresenter implements Presenter<PartnerDetailPresenter.UserInterface> {
    private final PartnerServiceClient partnerServiceClient;
    private final EventDispatcher eventDispatcher;
    private final PartnerRepository repository;
    private UserInterface userInterface;
    private long partnerId;
    private EventListener<PartnerDetailRequestEvent> listener = new EventListener<PartnerDetailRequestEvent>() {
        @Override
        public void onEvent(PartnerDetailRequestEvent event) {
            switch (event.result) {
                case SUCCESSFUL:
                    String partnerName = repository.getName(partnerId);
                    String html = event.details.getMainContent();
                    String url = event.details.getUrl();
                    userInterface.setupDetails(partnerName, html, url);
                    break;
                case CONNECTION_ERROR:
                    userInterface.showConnectionError();
                    break;
                case GENERIC_ERROR:
                    userInterface.showGenericError();
                    break;
            }
        }
    };

    public PartnerDetailPresenter(PartnerServiceClient partnerServiceClient, EventDispatcher eventDispatcher, PartnerRepository repository) {
        this.partnerServiceClient = partnerServiceClient;
        this.eventDispatcher = eventDispatcher;
        this.repository = repository;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        if (!isNewNavigation) {
            return;
        }
        loadPartnerDetails();
    }

    @Override
    public void onUserInterfaceAppeared() {
        eventDispatcher.addEventListener(PartnerDetailRequestEvent.class, listener);
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        eventDispatcher.removeEventListener(PartnerDetailRequestEvent.class, listener);
    }

    @Override
    public void onUserInterfaceDestroyed() {
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void setPartner(long partnerId) {
        this.partnerId = partnerId;
        String partnerName = repository.getName(partnerId);

        userInterface.setupActionBar(partnerName);
    }

    void loadPartnerDetails() {
        userInterface.showLoadingIndicator();
        partnerServiceClient.fetchWellnessPartnerDetails(partnerId);
    }

    public void onGetStartedTapped() {
        String url = repository.getDetails(partnerId).getUrl();
        userInterface.loadPartnerDetailsBrowser(url);
    }

    public interface UserInterface {
        void setupDetails(String partnerName, String partnerDetails, String url);

        void showGenericError();

        void setupActionBar(String partnerName);

        void hideLoadingIndicator();

        void showLoadingIndicator();

        void showConnectionError();

        void loadPartnerDetailsBrowser(String url);
    }
}
