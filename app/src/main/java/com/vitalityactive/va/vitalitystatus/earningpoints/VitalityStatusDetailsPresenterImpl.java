package com.vitalityactive.va.vitalitystatus.earningpoints;

import com.vitalityactive.va.activerewards.dto.TitleAndSubtitle;
import com.vitalityactive.va.vitalitystatus.ProductPointsContent;
import com.vitalityactive.va.vitalitystatus.repository.ProductPointsRepository;

import java.util.List;

public class VitalityStatusDetailsPresenterImpl implements VitalityStatusDetailsPresenter {
    private UserInterface userInterface;
    private int key;
    private ProductPointsRepository productPointsRepository;
    private ProductPointsContent content;
    private boolean isSubFeature = false;

    public VitalityStatusDetailsPresenterImpl(ProductPointsRepository productPointsRepository, ProductPointsContent productPointsContent) {
        this.productPointsRepository = productPointsRepository;
        this.content = productPointsContent;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        List<PointsInformationDTO> featureList;

        if (isSubFeature) {
            featureList = productPointsRepository.getSubFeatureList(key);
            userInterface.setActionBarTitle(productPointsRepository.getSubFeatureName(key));
        } else {
            featureList = productPointsRepository.getFeatureList(key);
            userInterface.setActionBarTitle(productPointsRepository.getFeatureName(key));
        }

        userInterface.displayPointsInformation(featureList, new TitleAndSubtitle("", ""));
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
    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public void setIsSubfeature(boolean isSubFeature) {
        this.isSubFeature = isSubFeature;
    }
}
