package com.vitalityactive.va.vitalitystatus.earningpoints;

import com.vitalityactive.va.vitalitystatus.repository.ProductPointsRepository;
import com.vitalityactive.va.wellnessdevices.WdEventType;

import java.util.List;

public class StatusPointsPresenterImpl implements StatusPointsPresenter {
    private UserInterface userInterface;
    private ProductPointsRepository pointsRepository;
    private int productFeatureKey;

    public StatusPointsPresenterImpl(ProductPointsRepository pointsRepository) {
        this.pointsRepository = pointsRepository;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        List<PointsInformationDTO> subFeatureList = pointsRepository.getSubFeatureList(productFeatureKey);

        if (subFeatureList == null) {
            userInterface.displayPointsContent(pointsRepository.getSubFeaturePointsEntries(productFeatureKey));
        } else {
            userInterface.displayPointsContent(pointsRepository.getFeaturePointsEntries(productFeatureKey));
        }

    }

    @Override
    public void onUserInterfaceAppeared() {

    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {

    }

    @Override
    public void onUserInterfaceDestroyed() {

    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void setProductFeatureKey(int productFeatureKey) {
        this.productFeatureKey = productFeatureKey;
    }

    @Override
    public WdEventType getWdEventType() {
        List<StatusPointsItem> featurePointsEntries = pointsRepository.getFeaturePointsEntries(productFeatureKey);
        if (featurePointsEntries.isEmpty()) {
            featurePointsEntries = pointsRepository.getSubFeaturePointsEntries(productFeatureKey);
        }
        if (featurePointsEntries.isEmpty()) {
            return WdEventType.HEART_RATE;
        }
        return WdEventType.getEventTypeByKey(featurePointsEntries.get(0).getPointsEntryType());
    }
}
