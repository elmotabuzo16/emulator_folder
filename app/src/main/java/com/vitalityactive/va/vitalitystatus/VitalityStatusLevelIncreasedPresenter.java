package com.vitalityactive.va.vitalitystatus;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.activerewards.dto.TitleSubtitleAndIcon;
import com.vitalityactive.va.vitalitystatus.levels.LevelStatusDTO;

import java.util.List;

public interface VitalityStatusLevelIncreasedPresenter extends Presenter<VitalityStatusLevelIncreasedPresenter.UserInterface> {
    interface UserInterface {
        void displayStatusIncreased(List<TitleSubtitleAndIcon> rewardsDetails, VitalityStatusDTO vitalityStatusDTO);
        void displayStatusInformation(List<TitleSubtitleAndIcon> rewardsDetails, VitalityStatusDTO vitalityStatus);
    }
}
