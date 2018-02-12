package com.vitalityactive.va.snv.partners.presenter;

import android.util.Log;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.snv.partners.dto.ProductFeatureGroupDto;
import com.vitalityactive.va.snv.partners.interactor.SnvParticipatingPartnersInteractor;
import com.vitalityactive.va.snv.partners.service.GetPartnersByCategoryResponse;
import com.vitalityactive.va.snv.shared.SnvConstants;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.utilities.CMSImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerry.e.lawagan on 12/8/2017.
 */

public class SnvParticipatingPartnersPresenterImpl<UserInterface extends SnvParticipatingPartnersPresenter.UserInterface> extends BasePresenter<UserInterface> implements SnvParticipatingPartnersPresenter<UserInterface> {
    private final CMSImageLoader cmsImageLoader;
    private EventDispatcher eventDispatcher;
    private SnvParticipatingPartnersInteractor interactor;
    private List<ProductFeatureGroupDto> productFeatureGroupDtos;

    private final EventListener<GetPartnersByCategorySuccessEvent> successtEventListener = new EventListener<GetPartnersByCategorySuccessEvent>() {
        @Override
        public void onEvent(GetPartnersByCategorySuccessEvent getPartnersByCategorySuccessEvent) {
            onGetPartnersByCategorySuccessEvent(getPartnersByCategorySuccessEvent);
        }
    };

    private final EventListener<GetPartnersByCategoryFailedEvent> failedEventListener = new EventListener<GetPartnersByCategoryFailedEvent>() {
        @Override
        public void onEvent(GetPartnersByCategoryFailedEvent getPartnersByCategoryFailedEvent) {
            userInterface.hideLoadingIndicator();
            onGetPartnersByCategoryFailedEvent(getPartnersByCategoryFailedEvent);
        }
    };

    private void onGetPartnersByCategorySuccessEvent(GetPartnersByCategorySuccessEvent getPartnersByCategorySuccessEvent) {
        productFeatureGroupDtos = new ArrayList<ProductFeatureGroupDto>();
        ProductFeatureGroupDto productFeatureGroupDto;
        for (GetPartnersByCategoryResponse.ProductFeatureGroup productFeatureGroup: getPartnersByCategorySuccessEvent.getResponse().getProductFeatureGroups()) {
            productFeatureGroupDto = new ProductFeatureGroupDto(productFeatureGroup);
            productFeatureGroupDtos.add(productFeatureGroupDto);
        }

        userInterface.updateListItems(productFeatureGroupDtos);
        userInterface.hideLoadingIndicator();
    }

    private final EventListener<AlertDialogFragment.DismissedEvent> alertDismissed = new EventListener<AlertDialogFragment.DismissedEvent>() {
        @Override
        public void onEvent(AlertDialogFragment.DismissedEvent event) {
            if ((event.getType().equals(SnvConstants.SNV_CONNECTION_ERROR_ALERT) || event.getType().equals(SnvConstants.SNV_GENERIC_ERROR_ALERT))
                    && event.getClickedButtonType() == AlertDialogFragment.DismissedEvent.ClickedButtonType.Positive) {
                fetchData();
            } else {
                userInterface.hideLoadingIndicator();
            }
        }
    };

    private void onGetPartnersByCategoryFailedEvent(GetPartnersByCategoryFailedEvent getPartnersByCategoryFailedEvent) {
        if (getPartnersByCategoryFailedEvent.getResult().equals(SnvConstants.SnvApiResult.CONNECTION_ERROR)) {
            userInterface.showConnectionContentRequestErrorMessage();
        } else {
            userInterface.showGenericContentRequestErrorMessage();
        }

    }

    public SnvParticipatingPartnersPresenterImpl(EventDispatcher eventDispatcher, SnvParticipatingPartnersInteractor interactor, CMSImageLoader cmsImageLoader) {
        this.eventDispatcher = eventDispatcher;
        this.interactor = interactor;
        this.cmsImageLoader = cmsImageLoader;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        if (isNewNavigation) {
            removeEventListeners();
            addEventListeners();
            fetchData();
        }
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);
        removeEventListeners();
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();
        removeEventListeners();
        addEventListeners();
    }

    private void fetchData() {
        interactor.fetchData();
        //userInterface.showLoadingIndicator();
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(GetPartnersByCategorySuccessEvent.class, successtEventListener);
        eventDispatcher.addEventListener(GetPartnersByCategoryFailedEvent.class, failedEventListener);
        eventDispatcher.addEventListener(AlertDialogFragment.DismissedEvent.class, alertDismissed);
    }

    private void removeEventListeners() {
        eventDispatcher.removeEventListener(GetPartnersByCategorySuccessEvent.class, successtEventListener);
        eventDispatcher.removeEventListener(GetPartnersByCategoryFailedEvent.class, failedEventListener);
        eventDispatcher.removeEventListener(AlertDialogFragment.DismissedEvent.class, alertDismissed);
    }

    @Override
    public CMSImageLoader getCmsImageLoader() {
        return cmsImageLoader;
    }
}
