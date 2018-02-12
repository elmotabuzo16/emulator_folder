package com.vitalityactive.va.vitalitystatus;

import com.vitalityactive.va.activerewards.dto.TitleSubtitleAndIcon;
import com.vitalityactive.va.vitalitystatus.repository.VitalityStatusRepository;

import java.util.List;

public class VitalityStatusLevelIncreasedPresenterImpl implements VitalityStatusLevelIncreasedPresenter {
    private UserInterface userInterface;
    private VitalityStatusRepository statusRepository;

    public VitalityStatusLevelIncreasedPresenterImpl(VitalityStatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        statusRepository.removeInstruction();

        List<TitleSubtitleAndIcon> rewardsDetails = statusRepository.getStatusRewardsDetails();

        if (statusRepository.isPreviousStatusReached()) {
            userInterface.displayStatusInformation(rewardsDetails, statusRepository.getVitalityStatus());
        } else {
            userInterface.displayStatusIncreased(rewardsDetails, statusRepository.getVitalityStatus());
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
}
