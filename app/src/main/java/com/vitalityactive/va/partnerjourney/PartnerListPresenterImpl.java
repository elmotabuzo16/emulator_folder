package com.vitalityactive.va.partnerjourney;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.partnerjourney.models.PartnerGroup;
import com.vitalityactive.va.partnerjourney.models.PartnerItem;
import com.vitalityactive.va.partnerjourney.service.PartnerListRequestEvent;
import com.vitalityactive.va.partnerjourney.service.PartnerServiceClient;
import com.vitalityactive.va.utilities.CMSImageLoader;

import java.util.List;

public class PartnerListPresenterImpl implements Presenter<PartnerListPresenterImpl.UserInterface> {
    private final PartnerServiceClient partnerServiceClient;
    private final CMSImageLoader cmsImageLoader;
    private final EventDispatcher eventDispatcher;
    private UserInterface userInterface;
    private EventListener<PartnerListRequestEvent> listEventListener = new EventListener<PartnerListRequestEvent>() {
        @Override
        public void onEvent(PartnerListRequestEvent event) {
            switch (event.result) {
                case SUCCESSFUL:
                    handleSuccessResponse(event.list);
                    break;
                case CONNECTION_ERROR:
                    handleConnectionError();
                    break;
                case GENERIC_ERROR:
                    handleGenericError();
                    break;
            }
        }
    };
    private PartnerType partnerType;

    public PartnerListPresenterImpl(PartnerServiceClient partnerServiceClient, CMSImageLoader cmsImageLoader, EventDispatcher eventDispatcher) {
        this.partnerServiceClient = partnerServiceClient;
        this.cmsImageLoader = cmsImageLoader;
        this.eventDispatcher = eventDispatcher;
    }

    public void setPartnerType(PartnerType partnerType) {
        this.partnerType = partnerType;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        fetchPartnerItems();
    }

    private void handleConnectionError() {
        userInterface.showConnectionError();
        userInterface.hideLoadingIndicator();
    }

    private void handleGenericError() {
        userInterface.showGenericError();
        userInterface.hideLoadingIndicator();
    }

    private void handleSuccessResponse(List<PartnerGroup> healthServiceItems) {
        userInterface.displayServiceItems(healthServiceItems);
        userInterface.hideLoadingIndicator();
    }

    @Override
    public void onUserInterfaceAppeared() {
        eventDispatcher.addEventListener(PartnerListRequestEvent.class, listEventListener);
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        eventDispatcher.removeEventListener(PartnerListRequestEvent.class, listEventListener);
    }

    @Override
    public void onUserInterfaceDestroyed() {
        this.userInterface = null;
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void onHealthServiceItemClicked(final PartnerItem item) {
        userInterface.navigateToPartnerDetails(item);
    }

    public void fetchPartnerItems() {
        userInterface.showLoadingIndicator();
        partnerServiceClient.fetchWellnessPartners(partnerType);
    }

    public PartnerType getPartnerType() {
        return partnerType;
    }

    public CMSImageLoader getCMSImageLoader() {
        return cmsImageLoader;
    }

    public interface UserInterface {
        void displayServiceItems(List<PartnerGroup> healthServiceItems);

        void navigateToPartnerDetails(PartnerItem item);

        void hideLoadingIndicator();

        void showConnectionError();

        void showGenericError();

        void showLoadingIndicator();
    }
}
