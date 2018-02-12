package com.vitalityactive.va.vitalitystatus.repository;

import com.vitalityactive.va.activerewards.dto.TitleSubtitleAndIcon;
import com.vitalityactive.va.dto.UserInstructionDTO;
import com.vitalityactive.va.home.service.HomeScreenCardStatusResponse;
import com.vitalityactive.va.vitalitystatus.VitalityStatusDTO;
import com.vitalityactive.va.vitalitystatus.levels.LevelStatusDTO;

import java.util.List;

public interface VitalityStatusRepository {
    void removeInstruction();
    boolean persistVitalityStatusResponse(HomeScreenCardStatusResponse response);
    boolean isPreviousStatusReached();
    boolean hasInstruction();
    boolean shouldShowMyRewards();
    LevelStatusDTO getStatusLevel(int latestStatusLevelKey);
    VitalityStatusDTO getVitalityStatus();
    UserInstructionDTO getStatusIncreasedInstruction();
    List<TitleSubtitleAndIcon> getStatusRewardsDetails();
}
