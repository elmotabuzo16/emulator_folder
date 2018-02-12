package com.vitalityactive.va.vitalitystatus.detail;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.activerewards.dto.TitleSubtitleAndIcon;
import com.vitalityactive.va.vitalitystatus.VitalityStatusDTO;

import java.util.List;

public interface VitalityStatusLevelDetailPresenter extends Presenter<VitalityStatusLevelDetailPresenter.UserInterface> {
    interface UserInterface {
        void displayRewardsDetail(VitalityStatusDTO headerText, List<TitleSubtitleAndIcon> rewardsDetails);
    }
}
