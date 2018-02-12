package com.vitalityactive.va.activerewards.rewards.persistence;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vitalityactive.va.activerewards.rewards.dto.RewardSelectionDTO;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.activerewards.rewards.dto.UnclaimedRewardDTO;
import com.vitalityactive.va.activerewards.rewards.service.PartnerEmailResponse;
import com.vitalityactive.va.activerewards.rewards.service.RewardsServiceResponse;
import com.vitalityactive.va.activerewards.rewards.service.SingleAwardedRewardServiceResponse;
import com.vitalityactive.va.constants.AwardedRewardStatus;
import com.vitalityactive.va.constants.PreferenceType;
import com.vitalityactive.va.dependencyinjection.ar.ActiveRewardsScope;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.date.LocalDate;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmQuery;

@ActiveRewardsScope
public class RewardsRepository {
    private static final int REWARD_KEY_WHEEL_SPIN = 1;
    private static final int REWARD_KEY_CHOOSE_REWARD = 7;
    private DataStore dataStore;

    @Inject
    RewardsRepository(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    static boolean isRewardSelectionTypeInvalid(@NonNull RewardsServiceResponse.AwardedReward awardedReward) {
        return awardedReward.rewardSelectionType == null ||
                awardedReward.rewardSelectionType.key == null ||
                awardedReward.rewardSelectionType.rewardSelections == null ||
                awardedReward.rewardSelectionType.rewardSelections.isEmpty();
    }

    public boolean persistCurrentRewards(RewardsServiceResponse response) {
        dataStore.removeAll(UnclaimedReward.class);
        dataStore.removeAll(RewardSelection.class);
        dataStore.removeAll(RewardVoucher.class);

        if (response.awardedRewards == null || response.awardedRewards.isEmpty()) {
            return true;
        }

        List<UnclaimedReward> unclaimedRewards = new ArrayList<>();
        List<RewardVoucher> vouchers = new ArrayList<>();

        for (RewardsServiceResponse.AwardedReward awardedReward : response.awardedRewards) {
            if (isAwardedRewardInvalid(awardedReward)) {
                return false;
            }
            int awardedRewardStatus = getAwardedRewardStatus(awardedReward);
            if (isWheelSpinOrChooseReward(awardedReward, awardedRewardStatus)) {
                UnclaimedReward unclaimedReward = UnclaimedReward.create(awardedReward, awardedRewardStatus);
                if (unclaimedReward == null) {
                    return false;
                }
                unclaimedRewards.add(unclaimedReward);
            } else if (isRewardVoucher(awardedReward, awardedRewardStatus)) {
                RewardVoucher rewardVoucher = RewardVoucher.create(awardedReward, awardedRewardStatus);
                if (rewardVoucher == null) {
                    return false;
                }
                vouchers.add(rewardVoucher);
            }
        }

        dataStore.addOrUpdate(unclaimedRewards);
        dataStore.addOrUpdate(vouchers);

        return true;
    }

    private boolean isWheelSpinOrChooseReward(RewardsServiceResponse.AwardedReward awardedReward, int awardedRewardStatus) {
        return (awardedReward.reward.key == REWARD_KEY_WHEEL_SPIN || awardedReward.reward.key == REWARD_KEY_CHOOSE_REWARD) &&
                (awardedRewardStatus == AwardedRewardStatus._ACKNOWLEDGED || awardedRewardStatus == AwardedRewardStatus._ALLOCATED);
    }

    private boolean isRewardVoucher(RewardsServiceResponse.AwardedReward awardedReward, int awardedRewardStatus) {
        return (awardedReward.reward.key != REWARD_KEY_WHEEL_SPIN && awardedReward.reward.key != REWARD_KEY_CHOOSE_REWARD) &&
                (awardedRewardStatus == AwardedRewardStatus._ISSUED ||
                        awardedRewardStatus == AwardedRewardStatus._ALLOCATED ||
                        awardedRewardStatus == AwardedRewardStatus._AVAILABLETOREDEEM ||
                        awardedRewardStatus == AwardedRewardStatus._PARTNERREGISTRATION ||
                        awardedRewardStatus == AwardedRewardStatus._ISSUEFAILED);
    }

    private boolean isAwardedRewardInvalid(RewardsServiceResponse.AwardedReward awardedReward) {
        return awardedReward == null ||
                awardedReward.agreementPartyId == null ||
                awardedReward.awardedOn == null ||
                awardedReward.effectiveFrom == null ||
                awardedReward.effectiveTo == null ||
                awardedReward.id == null ||
                awardedReward.partyId == null ||
                awardedReward.rewardProviderPartyId == null ||
                isRewardInvalid(awardedReward.reward);
    }

    static boolean isRewardInvalid(@Nullable RewardsServiceResponse.Reward reward) {
        return reward == null ||
                reward.categoryKey == null ||
                reward.id == null ||
                reward.key == null ||
                reward.name == null ||
                reward.optionTypeKey == null ||
                reward.optionTypeName == null ||
                reward.providedByPartyId == null ||
                reward.typeCategoryKey == null ||
                reward.typeKey == null;
    }

    private int getAwardedRewardStatus(@NonNull RewardsServiceResponse.AwardedReward awardedReward) {
        int currentStatus = -1;
        if (awardedReward.awardedRewardStatuses != null) {
            LocalDate latestDate = new LocalDate("0000-01-01");
            for (RewardsServiceResponse.Status status : awardedReward.awardedRewardStatuses) {
                if (status.effectiveOn == null ||
                        status.key == null ||
                        status.partyId == null) {
                    continue;
                }
                LocalDate effectiveOn = new LocalDate(status.effectiveOn);
                if (effectiveOn.compareTo(latestDate) > 0) {
                    latestDate = effectiveOn;
                    currentStatus = status.key;
                }
            }
        }
        return currentStatus;
    }

    public long getUnclaimedRewardsCount() {
        return dataStore.getNumberOfModels(UnclaimedReward.class);
    }

    public boolean persistRewardsHistory(RewardsServiceResponse response) {
        // TODO: persist into HistoricalReward
        return false;
    }

    public List<UnclaimedRewardDTO> getAvailableUnclaimedRewards() {
        return dataStore.getModels(UnclaimedReward.class, new DataStore.ModelListMapper<UnclaimedReward, UnclaimedRewardDTO>() {
            @Override
            public List<UnclaimedRewardDTO> mapModels(List<UnclaimedReward> models) {
                ArrayList<UnclaimedRewardDTO> unclaimedRewardDTOs = new ArrayList<>();
                for (UnclaimedReward unclaimedReward : models) {
                    unclaimedRewardDTOs.add(new UnclaimedRewardDTO(unclaimedReward));
                }
                return unclaimedRewardDTOs;
            }
        });
    }

    public List<RewardVoucherDTO> getAvailableRewardVouchers() {
        return dataStore.getModels(RewardVoucher.class, new DataStore.QueryExecutor<RewardVoucher, RealmQuery<RewardVoucher>>() {
            @Override
            public List<RewardVoucher> executeQueries(RealmQuery<RewardVoucher> initialQuery) {
                return initialQuery.equalTo("issueFailed", false).findAll();
            }
        }, new DataStore.ModelListMapper<RewardVoucher, RewardVoucherDTO>() {
            @Override
            public List<RewardVoucherDTO> mapModels(List<RewardVoucher> models) {
                ArrayList<RewardVoucherDTO> rewardVoucherDTOs = new ArrayList<>();
                for (RewardVoucher rewardVoucher : models) {
                    rewardVoucherDTOs.add(new RewardVoucherDTO(rewardVoucher));
                }
                return rewardVoucherDTOs;
            }
        });
    }

    public UnclaimedRewardDTO getUnclaimedReward(long rewardUniqueId) {
        return dataStore.getModelInstance(UnclaimedReward.class, new DataStore.ModelMapper<UnclaimedReward, UnclaimedRewardDTO>() {
            @Override
            public UnclaimedRewardDTO mapModel(UnclaimedReward model) {
                return model == null ? null : new UnclaimedRewardDTO(model);
            }
        }, "id", rewardUniqueId);
    }

    public RewardVoucherDTO getRewardVoucher(long rewardUniqueId) {
        return dataStore.getModelInstance(RewardVoucher.class, new DataStore.ModelMapper<RewardVoucher, RewardVoucherDTO>() {
            @Override
            public RewardVoucherDTO mapModel(RewardVoucher model) {
                return model == null ? null : new RewardVoucherDTO(model);
            }
        }, "id", rewardUniqueId);
    }

    public boolean persistPartnerRegisteredEmail(PartnerEmailResponse response) {
        if (response.response == null || response.response.party == null || response.response.party.generalPreferences == null) {
            return false;
        }
        dataStore.removeAll(PartnerEmail.class);
        for (PartnerEmailResponse.GeneralPreference generalPreference : response.response.party.generalPreferences) {
            if (generalPreference.typeKey == PreferenceType._STARBUCKSEMAIL && generalPreference.value != null) {
                dataStore.add(new PartnerEmail(generalPreference.value));
                return true;
            }
        }
        return false;
    }

    @Nullable
    public String getPartnerRegisteredEmail() {
        return dataStore.getFirstModelInstance(PartnerEmail.class, new DataStore.ModelMapper<PartnerEmail, String>() {
            @Override
            public String mapModel(PartnerEmail model) {
                return model == null ? null : model.getValue();
            }
        });
    }

    public boolean persistRewardVoucher(RewardsServiceResponse.AwardedReward awardedReward) {
        if (isAwardedRewardInvalid(awardedReward)) {
            return false;
        }
        int awardedRewardStatus = getAwardedRewardStatus(awardedReward);

        if (isRewardVoucher(awardedReward, awardedRewardStatus)) {
            removeSelections(awardedReward.id);
            RewardVoucher rewardVoucher = RewardVoucher.create(awardedReward, awardedRewardStatus);
            if (rewardVoucher != null) {
                dataStore.addOrUpdate(rewardVoucher);
                return true;
            }
        }
        return false;
    }

    public boolean persistUnclaimedReward(SingleAwardedRewardServiceResponse response) {
        RewardsServiceResponse.AwardedReward awardedReward = response.awardedReward;
        if (isAwardedRewardInvalid(awardedReward)) {
            return false;
        }
        int awardedRewardStatus = getAwardedRewardStatus(awardedReward);

        if (isWheelSpinOrChooseReward(awardedReward, awardedRewardStatus)) {
            UnclaimedReward unclaimedReward = UnclaimedReward.create(awardedReward, awardedRewardStatus);
            if (unclaimedReward != null) {
                dataStore.addOrUpdate(unclaimedReward);
                return true;
            }
        }
        return false;
    }

    public void removeWheelSpin(long wheelSpinUniqueId) {
        dataStore.remove(UnclaimedReward.class, "id", wheelSpinUniqueId);
        removeSelections(wheelSpinUniqueId);
    }

    private void removeSelections(long rewardUniqueId) {
        dataStore.removeAll(RewardSelection.class, "rewardUniqueId", rewardUniqueId);
    }

    public void removeVoucherWithSelections(long voucherWithSelectionsUniqueId) {
        dataStore.remove(RewardVoucher.class, "id", voucherWithSelectionsUniqueId);
        removeSelections(voucherWithSelectionsUniqueId);
    }

    public List<RewardSelectionDTO> getSelectionsForRewardWithUniqueId(final long uniqueId) {
        return dataStore.getModels(RewardSelection.class, new DataStore.QueryExecutor<RewardSelection, RealmQuery<RewardSelection>>() {
            @Override
            public List<RewardSelection> executeQueries(RealmQuery<RewardSelection> initialQuery) {
                return initialQuery.equalTo("rewardUniqueId", uniqueId).findAllSorted("sortOrder");
            }
        }, new DataStore.ModelListMapper<RewardSelection, RewardSelectionDTO>() {
            @Override
            public List<RewardSelectionDTO> mapModels(List<RewardSelection> models) {
                List<RewardSelectionDTO> rewardSelections = new ArrayList<>();
                for (RewardSelection rewardSelection : models) {
                    rewardSelections.add(new RewardSelectionDTO(rewardSelection));
                }
                return rewardSelections;
            }
        });
    }

    @NonNull
    public static String getRewardDescription(String rewardValue, String rewardType) {
        if (TextUtilities.isNullOrWhitespace(rewardValue)) {
            return rewardType;
        }
        return rewardValue + " " + rewardType;
    }

    public RewardSelectionDTO getRewardSelection(long rewardUniqueId, int rewardId) {
        return dataStore.getModelInstance(RewardSelection.class, new DataStore.ModelMapper<RewardSelection, RewardSelectionDTO>() {
            @Override
            public RewardSelectionDTO mapModel(RewardSelection model) {
                return model == null ? null : new RewardSelectionDTO(model);
            }
        }, "rewardUniqueId", rewardUniqueId, "rewardId", rewardId);
    }
}
