package com.vitalityactive.va.vitalitystatus.detail;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.TitleSubtitleAndIcon;
import com.vitalityactive.va.vitalitystatus.VitalityStatusDTO;
import com.vitalityactive.va.vitalitystatus.repository.VitalityStatusRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class VitalityStatusLevelDetailPresenterImpl implements VitalityStatusLevelDetailPresenter {
    private UserInterface userInterface;
    private VitalityStatusRepository repository;

    public VitalityStatusLevelDetailPresenterImpl(VitalityStatusRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        List<TitleSubtitleAndIcon> rewardsDetails = new ArrayList<>();

        rewardsDetails.add(new TitleSubtitleAndIcon("15% discount", "Shop online", R.drawable.amazon_reward_sml));
        rewardsDetails.add(new TitleSubtitleAndIcon("15% discount", "Spend R300", R.drawable.starbucks_reward_sml));
        rewardsDetails.add(new TitleSubtitleAndIcon("15% discount", "Buy any drink", R.drawable.amazon_reward_sml));

        VitalityStatusDTO vitalityStatus = repository.getVitalityStatus();
        userInterface.displayRewardsDetail(vitalityStatus, rewardsDetails);
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
