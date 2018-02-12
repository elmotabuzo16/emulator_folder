package com.vitalityactive.va.vitalitystatus.repository;

import android.support.annotation.NonNull;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.TitleSubtitleAndIcon;
import com.vitalityactive.va.constants.StatusType;
import com.vitalityactive.va.constants.UserInstructions;
import com.vitalityactive.va.dto.UserInstructionDTO;
import com.vitalityactive.va.home.service.HomeScreenCardStatusResponse;
import com.vitalityactive.va.networking.parsing.Persister;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.UserInstruction;
import com.vitalityactive.va.vitalitystatus.VitalityStatus;
import com.vitalityactive.va.vitalitystatus.VitalityStatusDTO;
import com.vitalityactive.va.vitalitystatus.levels.LevelStatus;
import com.vitalityactive.va.vitalitystatus.levels.LevelStatusDTO;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmQuery;

public class VitalityStatusRepositoryImpl implements VitalityStatusRepository {
    private final DataStore dataStore;
    private final Persister persister;

    public VitalityStatusRepositoryImpl(DataStore dataStore) {
        this.dataStore = dataStore;
        persister = new Persister(dataStore);
    }

    @Override
    public boolean persistVitalityStatusResponse(HomeScreenCardStatusResponse response) {
        dataStore.removeAll(VitalityStatus.class);
        dataStore.removeAll(LevelStatus.class);

        return response.vitalityStatus != null && persister.addModel(new VitalityStatus(response));
    }

    @Override
    public boolean isPreviousStatusReached() {
        return false;
    }

    @Override
    public VitalityStatusDTO getVitalityStatus() {
        return dataStore.getFirstModelInstance(VitalityStatus.class, new DataStore.ModelMapper<VitalityStatus, VitalityStatusDTO>() {
            @Override
            public VitalityStatusDTO mapModel(VitalityStatus model) {
                return new VitalityStatusDTO(model,
                        getStatusLevel(model.getCurrentStatusKey()),
                        getStatusLevel(model.getNextStatusKey()),
                        getStatusLevel(model.getCarryOverStatusKey()),
                        getAvailableLevels(),
                        model.getPointsStatusKey());
            }
        });
    }

    @Override
    public List<TitleSubtitleAndIcon> getStatusRewardsDetails() {
        List<TitleSubtitleAndIcon> rewardsDetails = new ArrayList<>();

        rewardsDetails.add(new TitleSubtitleAndIcon("15% discount", "Shop online", "garmin.png"));
        rewardsDetails.add(new TitleSubtitleAndIcon("15% discount", "Spend R300", "garmin.png"));
        rewardsDetails.add(new TitleSubtitleAndIcon("15% discount", "Buy any drink", ""));

        return rewardsDetails;
    }

    @Override
    public LevelStatusDTO getStatusLevel(final int statusKey) {
        return dataStore.getModelInstance(LevelStatus.class, new DataStore.SingleModelQueryExecutor<LevelStatus, RealmQuery<LevelStatus>>() {
            @Override
            public LevelStatus executeQueries(RealmQuery<LevelStatus> initialQuery) {
                return initialQuery.equalTo("statusKey", statusKey).findFirst();
            }
        }, getLevelStatusDTOModelMapper());
    }

    @NonNull
    private DataStore.ModelMapper<LevelStatus, LevelStatusDTO> getLevelStatusDTOModelMapper() {
        return new DataStore.ModelMapper<LevelStatus, LevelStatusDTO>() {
            @Override
            public LevelStatusDTO mapModel(LevelStatus model) {
                return model == null ? new LevelStatusDTO() : new LevelStatusDTO(model.getName(),
                        model.getKey(),
                        model.getPointsNeeded(),
                        model.getPointsThreshold(),
                        getSmallStatusIconResourceId(model.getKey()),
                        getLargeStatusIconResourceId(model.getKey()),
                        model.getSortOrder());
            }
        };
    }

    private List<LevelStatusDTO> getAvailableLevels() {
        return dataStore.getModels(LevelStatus.class, new DataStore.QueryExecutor<LevelStatus, RealmQuery<LevelStatus>>() {
            @Override
            public List<LevelStatus> executeQueries(RealmQuery<LevelStatus> initialQuery) {
                return initialQuery.findAllSorted("sortOrder");
            }
        }, new DataStore.ModelListMapper<LevelStatus, LevelStatusDTO>() {
            @Override
            public List<LevelStatusDTO> mapModels(List<LevelStatus> models) {
                List<LevelStatusDTO> statuses = new ArrayList<>();

                for (LevelStatus level : models) {
                    statuses.add(new LevelStatusDTO(level.getName(),
                            level.getKey(),
                            level.getPointsNeeded(),
                            level.getPointsThreshold(),
                            getSmallStatusIconResourceId(level.getKey()),
                            getLargeStatusIconResourceId(level.getKey()),
                            level.getSortOrder()));
                }

                return statuses;
            }
        });
    }

    @Override
    public UserInstructionDTO getStatusIncreasedInstruction() {
        return dataStore.getModelInstance(UserInstruction.class,
                new UserInstructionDTO.Mapper(),
                "type",
                UserInstructions.Types.STATUS_INCREASED);
    }

    @Override
    public boolean hasInstruction() {
        return getStatusIncreasedInstruction() != null;
    }

    @Override
    public void removeInstruction() {
        dataStore.remove(UserInstruction.class,
                "type",
                UserInstructions.Types.STATUS_INCREASED);
    }

    @Override
    public boolean shouldShowMyRewards() {
        return false;
    }

    private int getLargeStatusIconResourceId(int key) {
        switch (key) {
            case StatusType._BLUE:
                return R.drawable.status_badge_blue_large;
            case StatusType._BRONZE:
                return R.drawable.status_badge_bronze_large;
            case StatusType._SILVER:
                return R.drawable.status_badge_silver_large;
            case StatusType._GOLD:
                return R.drawable.status_badge_gold_large;
            case StatusType._PLATINUM:
                return R.drawable.status_badge_platinum_large;
            default:
                return 0;
        }
    }

    private int getSmallStatusIconResourceId(int key) {
        switch (key) {
            case StatusType._BLUE:
                return R.drawable.status_badge_blue_small;
            case StatusType._BRONZE:
                return R.drawable.status_badge_bronze_small;
            case StatusType._SILVER:
                return R.drawable.status_badge_silver_small;
            case StatusType._GOLD:
                return R.drawable.status_badge_gold_small;
            case StatusType._PLATINUM:
                return R.drawable.status_badge_platinum_small;
            default:
                return 0;
        }
    }
}