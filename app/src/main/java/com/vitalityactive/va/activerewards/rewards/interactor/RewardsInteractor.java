package com.vitalityactive.va.activerewards.rewards.interactor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.activerewards.rewards.dto.RewardSelectionDTO;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.activerewards.rewards.dto.UnclaimedRewardDTO;
import com.vitalityactive.va.activerewards.rewards.events.CurrentRewardsRequestCompletedEvent;
import com.vitalityactive.va.activerewards.rewards.events.PartnerRegisteredEmailFetchedEvent;
import com.vitalityactive.va.activerewards.rewards.events.PartnerRegisteredEmailUpdatedEvent;
import com.vitalityactive.va.activerewards.rewards.events.RewardVoucherSelectedEvent;
import com.vitalityactive.va.activerewards.rewards.events.WheelSpinRequestCompletedEvent;
import com.vitalityactive.va.activerewards.rewards.persistence.RewardsRepository;
import com.vitalityactive.va.activerewards.rewards.service.MarkNoRewardWheelSpinUsedEvent;
import com.vitalityactive.va.activerewards.rewards.service.PartnerEmailResponse;
import com.vitalityactive.va.activerewards.rewards.service.RewardsServiceClient;
import com.vitalityactive.va.activerewards.rewards.service.RewardsServiceResponse;
import com.vitalityactive.va.activerewards.rewards.service.SelectVoucherServiceResponse;
import com.vitalityactive.va.activerewards.rewards.service.SingleAwardedRewardServiceResponse;
import com.vitalityactive.va.dependencyinjection.ar.ActiveRewardsScope;
import com.vitalityactive.va.dto.PartnerItemDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.partnerjourney.PartnerType;
import com.vitalityactive.va.partnerjourney.models.PartnerGroup;
import com.vitalityactive.va.partnerjourney.models.PartnerItem;
import com.vitalityactive.va.partnerjourney.repository.PartnerRepository;
import com.vitalityactive.va.partnerjourney.service.PartnerListRequestEvent;
import com.vitalityactive.va.partnerjourney.service.models.PartnerListResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

@ActiveRewardsScope
public class RewardsInteractor {
    private static final String TAG = RewardsInteractor.class.getSimpleName();
    private static final int CURRENT_REWARDS_HTTP_BAD_REQUEST = 400;
    private static final int UKE_REWARD_NOT_FOUND = 30214;

    private RewardsServiceClient serviceClient;
    private RewardsRepository rewardsRepository;
    private PartnerRepository partnerRepository;
    private EventDispatcher eventDispatcher;
    private InsurerConfigurationRepository insurerConfigurationRepository;
    private HashMap<Long, Long> selectVoucherState = new HashMap<>();

    @Inject
    RewardsInteractor(RewardsServiceClient serviceClient,
                      RewardsRepository rewardsRepository,
                      PartnerRepository partnerRepository,
                      EventDispatcher eventDispatcher,
                      InsurerConfigurationRepository insurerConfigurationRepository) {
        this.serviceClient = serviceClient;
        this.rewardsRepository = rewardsRepository;
        this.partnerRepository = partnerRepository;
        this.eventDispatcher = eventDispatcher;
        this.insurerConfigurationRepository = insurerConfigurationRepository;
    }

    public void fetchPartnerRegisteredEmail() {
        serviceClient.fetchPartnerRegisteredEmail(new PartnerEmailResponseParser());
    }

    public void updatePartnerRegisteredEmail(String partnerRegisteredEmail) {
        serviceClient.updatePartnerRegisteredEmail(partnerRegisteredEmail, new UpdatePartnerRegisteredEmailResponseParser());
    }

    public void selectVoucher(final long wheelSpinUniqueId) {
        if (selectVoucherState.containsKey(wheelSpinUniqueId)) {
            fetchRewardVoucher(selectVoucherState.get(wheelSpinUniqueId));
        } else {
            UnclaimedRewardDTO wheelSpin = getWheelSpin(wheelSpinUniqueId);
            if (wheelSpin == null) {
                return;
            }
            selectVoucher(wheelSpin.uniqueId, wheelSpin.outcomeRewardValueLinkId, true);
        }
    }

    public void selectVoucher(final long rewardUniqueId, final long rewardValueLinkId) {
        if (selectVoucherState.containsKey(rewardUniqueId)) {
            fetchRewardVoucher(selectVoucherState.get(rewardUniqueId));
        } else {
            selectVoucher(rewardUniqueId, rewardValueLinkId, false);
        }
    }

    private void selectVoucher(final long rewardUniqueId, final long rewardValueLinkId, final boolean isWheelSpin) {
        serviceClient.selectVoucher(rewardUniqueId, rewardValueLinkId, new SelectVoucherServiceResponseParser(rewardUniqueId, isWheelSpin));
    }

    public void fetchRewardVoucher(long rewardVoucherUniqueId) {
        serviceClient.fetchSingleAwardedReward(rewardVoucherUniqueId, new RewardVoucherServiceResponseParser(rewardVoucherUniqueId));
    }

    private void resetSelectVoucherState(long exchangedRewardId) {
        for (long wheelSpinUniqueId : selectVoucherState.keySet()) {
            if (selectVoucherState.get(wheelSpinUniqueId) == exchangedRewardId) {
                selectVoucherState.remove(wheelSpinUniqueId);
                break;
            }
        }
    }

    public boolean shouldShowRewardPartnerDataSharingConsent(long rewardKey) {
        return insurerConfigurationRepository.shouldShowRewardPartnerDataSharingConsent(rewardKey);
    }

    @Nullable
    public UnclaimedRewardDTO getWheelSpin(long uniqueId) {
        return rewardsRepository.getUnclaimedReward(uniqueId);
    }

    public boolean isFetchingRewardSelections(long rewardVoucherUniqueId) {
        return serviceClient.isFetchingRewardVoucher(rewardVoucherUniqueId);
    }

    @NonNull
    public List<RewardSelectionDTO> getSelectionsForCinemaRewardWithId(long uniqueId) {
        return rewardsRepository.getSelectionsForRewardWithUniqueId(uniqueId);
    }

    @NonNull
    public List<RewardSelectionDTO> getSelectionsForWheelSpinWithId(long uniqueId) {
        return rewardsRepository.getSelectionsForRewardWithUniqueId(uniqueId);
    }

    public RewardVoucherDTO getRewardVoucherById(long voucherId) {
        return rewardsRepository.getRewardVoucher(voucherId);
    }

    public long getUnclaimedRewardsCount() {
        return rewardsRepository.getUnclaimedRewardsCount();
    }

    public void fetchCurrentRewards() {
        if (serviceClient.isCurrentRewardsRequestInProgress()) {
            return;
        }
        serviceClient.fetchCurrentRewards(new RewardsServiceResponseParser());
    }

    public void fetchRewardPartners() {
        serviceClient.fetchActiveRewardsPartners(new PartnerListResponseParser());
    }

    @NonNull
    public List<UnclaimedRewardDTO> getAvailableUnclaimedRewards() {
        return rewardsRepository.getAvailableUnclaimedRewards();
    }

    @NonNull
    public List<RewardVoucherDTO> getAvailableRewardVouchers() {
        return rewardsRepository.getAvailableRewardVouchers();
    }

    @Nullable
    public List<PartnerItemDTO> getActiveRewardsPartners() {
        return partnerRepository.getPartnerItems(PartnerType.REWARDS.productFeatureCategoryTypeKey);
    }

    @Nullable
    public String getPartnerRegisteredEmail() {
        return rewardsRepository.getPartnerRegisteredEmail();
    }

    public boolean isFetchingPartnerRegisteredEmail() {
        return serviceClient.isPartnerRegisteredEmailRequestInProgress();
    }

    public boolean shouldShowDataSharingConsentForWheelSpinOutcome(long rewardUniqueId) {
        UnclaimedRewardDTO wheelSpin = getWheelSpin(rewardUniqueId);
        return wheelSpin != null && shouldShowRewardPartnerDataSharingConsent(wheelSpin.outcomeRewardKey);
    }

    public void markNoRewardWheelSpinUsed(final long wheelSpinUniqueId) {
        serviceClient.markNoRewardWheelSpinUsed(wheelSpinUniqueId, new WebServiceResponseParser<String>() {
            @Override
            public void parseResponse(String response) {
                rewardsRepository.removeWheelSpin(wheelSpinUniqueId);
                eventDispatcher.dispatchEvent(MarkNoRewardWheelSpinUsedEvent.SUCCESSFUL);
            }

            @Override
            public void parseErrorResponse(String errorBody, int code) {
                eventDispatcher.dispatchEvent(MarkNoRewardWheelSpinUsedEvent.GENERIC_ERROR);
            }

            @Override
            public void handleGenericError(Exception exception) {
                eventDispatcher.dispatchEvent(MarkNoRewardWheelSpinUsedEvent.GENERIC_ERROR);
            }

            @Override
            public void handleConnectionError() {
                eventDispatcher.dispatchEvent(MarkNoRewardWheelSpinUsedEvent.CONNECTION_ERROR);
            }
        });
    }

    public void fetchWheelSpinWithId(long rewardUniqueId) {
        serviceClient.fetchSingleAwardedReward(rewardUniqueId, new WebServiceResponseParser<SingleAwardedRewardServiceResponse>() {
            @Override
            public void parseResponse(SingleAwardedRewardServiceResponse response) {
                if (rewardsRepository.persistUnclaimedReward(response)) {
                    eventDispatcher.dispatchEvent(WheelSpinRequestCompletedEvent.SUCCESSFUL);
                } else {
                    eventDispatcher.dispatchEvent(WheelSpinRequestCompletedEvent.GENERIC_ERROR);
                }
            }

            @Override
            public void parseErrorResponse(String errorBody, int code) {
                eventDispatcher.dispatchEvent(WheelSpinRequestCompletedEvent.GENERIC_ERROR);
            }

            @Override
            public void handleGenericError(Exception exception) {
                eventDispatcher.dispatchEvent(WheelSpinRequestCompletedEvent.GENERIC_ERROR);
            }

            @Override
            public void handleConnectionError() {
                eventDispatcher.dispatchEvent(WheelSpinRequestCompletedEvent.CONNECTION_ERROR);
            }
        });
    }

    public RewardSelectionDTO getRewardSelection(long rewardUniqueId, int rewardId) {
        return rewardsRepository.getRewardSelection(rewardUniqueId, rewardId);
    }

    class PartnerEmailResponseParser implements WebServiceResponseParser<PartnerEmailResponse> {

        @Override
        public void parseResponse(PartnerEmailResponse response) {
            rewardsRepository.persistPartnerRegisteredEmail(response);
            eventDispatcher.dispatchEvent(PartnerRegisteredEmailFetchedEvent.COMPLETED);
        }

        @Override
        public void parseErrorResponse(String errorBody, int code) {
            eventDispatcher.dispatchEvent(PartnerRegisteredEmailFetchedEvent.COMPLETED);
        }

        @Override
        public void handleGenericError(Exception exception) {
            eventDispatcher.dispatchEvent(PartnerRegisteredEmailFetchedEvent.COMPLETED);
        }

        @Override
        public void handleConnectionError() {
            eventDispatcher.dispatchEvent(PartnerRegisteredEmailFetchedEvent.COMPLETED);
        }
    }

    private class SelectVoucherServiceResponseParser implements WebServiceResponseParser<SelectVoucherServiceResponse> {

        private final long rewardUniqueId;
        private final boolean isWheelSpin;

        SelectVoucherServiceResponseParser(long rewardUniqueId, boolean isWheelSpin) {
            this.rewardUniqueId = rewardUniqueId;
            this.isWheelSpin = isWheelSpin;
        }

        @Override
        public void parseResponse(SelectVoucherServiceResponse response) {
            if (isWheelSpin) {
                rewardsRepository.removeWheelSpin(rewardUniqueId);
            } else {
                rewardsRepository.removeVoucherWithSelections(rewardUniqueId);
            }
            if (response.response == null || response.response.exchangedRewardId == null) {
                eventDispatcher.dispatchEvent(RewardVoucherSelectedEvent.GENERIC_ERROR);
            } else {
                selectVoucherState.put(rewardUniqueId, response.response.exchangedRewardId);
                fetchRewardVoucher(response.response.exchangedRewardId);
            }
        }

        @Override
        public void parseErrorResponse(String errorBody, int code) {
            eventDispatcher.dispatchEvent(RewardVoucherSelectedEvent.GENERIC_ERROR);
        }

        @Override
        public void handleGenericError(Exception exception) {
            eventDispatcher.dispatchEvent(RewardVoucherSelectedEvent.GENERIC_ERROR);
        }

        @Override
        public void handleConnectionError() {
            eventDispatcher.dispatchEvent(RewardVoucherSelectedEvent.CONNECTION_ERROR);
        }
    }

    class UpdatePartnerRegisteredEmailResponseParser implements WebServiceResponseParser<String> {

        @Override
        public void parseResponse(String response) {
            eventDispatcher.dispatchEvent(PartnerRegisteredEmailUpdatedEvent.SUCCESSFUL);
        }

        @Override
        public void parseErrorResponse(String errorBody, int code) {
            eventDispatcher.dispatchEvent(PartnerRegisteredEmailUpdatedEvent.GENERIC_ERROR);
        }

        @Override
        public void handleGenericError(Exception exception) {
            eventDispatcher.dispatchEvent(PartnerRegisteredEmailUpdatedEvent.GENERIC_ERROR);
        }

        @Override
        public void handleConnectionError() {
            eventDispatcher.dispatchEvent(PartnerRegisteredEmailUpdatedEvent.CONNECTION_ERROR);
        }
    }

    class RewardsServiceResponseParser implements WebServiceResponseParser<RewardsServiceResponse> {

        @Override
        public void parseResponse(RewardsServiceResponse response) {
            if (rewardsRepository.persistCurrentRewards(response)) {
                eventDispatcher.dispatchEvent(CurrentRewardsRequestCompletedEvent.SUCCESSFUL);
            } else {
                eventDispatcher.dispatchEvent(CurrentRewardsRequestCompletedEvent.GENERIC_ERROR);
            }
        }

        @Override
        public void parseErrorResponse(String errorBody, int responseCode) {
            int errorCode = 0;
            try {
                errorCode = new JSONObject(errorBody)
                        .getJSONArray("errors")
                        .getJSONObject(0)
                        .optInt("code", 0);
            } catch (JSONException e) {
                Log.e(TAG, "error parsing error", e);
            }

            if (responseCode == CURRENT_REWARDS_HTTP_BAD_REQUEST && rewardNotFoundErrorCode(errorCode)) {
                eventDispatcher.dispatchEvent(CurrentRewardsRequestCompletedEvent.REWARD_NOT_FOUND);
            } else {
                eventDispatcher.dispatchEvent(CurrentRewardsRequestCompletedEvent.GENERIC_ERROR);
            }
        }

        @Override
        public void handleGenericError(Exception exception) {
            eventDispatcher.dispatchEvent(CurrentRewardsRequestCompletedEvent.GENERIC_ERROR);
        }

        @Override
        public void handleConnectionError() {
            eventDispatcher.dispatchEvent(CurrentRewardsRequestCompletedEvent.CONNECTION_ERROR);
        }
    }

    private class PartnerListResponseParser implements WebServiceResponseParser<PartnerListResponse> {
        @Override
        public void parseResponse(PartnerListResponse response) {
            final List<PartnerGroup> groups = mapModel(response);
            if (partnerRepository.persist(groups)) {
                eventDispatcher.dispatchEvent(new PartnerListRequestEvent(groups));
            } else {
                eventDispatcher.dispatchEvent(new PartnerListRequestEvent(RequestResult.GENERIC_ERROR));
            }
        }

        @NonNull
        private List<PartnerGroup> mapModel(PartnerListResponse body) {
            final List<PartnerGroup> groups = new ArrayList<>();

            for (PartnerListResponse.ProductFeatureGroups group: body.productFeatureGroups) {
                PartnerGroup partnerGroup = new PartnerGroup(PartnerType.REWARDS.productFeatureCategoryTypeKey, group.name);
                for (PartnerListResponse.PartnerResponse response : group.productFeatures) {
                    PartnerItem item = new PartnerItem(PartnerType.REWARDS.productFeatureCategoryTypeKey, response.typeKey,
                            response.name, response.description, response.longDescription, response.logoFileName);
                    partnerGroup.items.add(item);
                }
                groups.add(partnerGroup);
            }

            return groups;
        }

        @Override
        public void parseErrorResponse(String errorBody, int code) {
            eventDispatcher.dispatchEvent(new PartnerListRequestEvent(RequestResult.GENERIC_ERROR));
        }

        @Override
        public void handleGenericError(Exception exception) {
            eventDispatcher.dispatchEvent(new PartnerListRequestEvent(RequestResult.GENERIC_ERROR));
        }

        @Override
        public void handleConnectionError() {
            eventDispatcher.dispatchEvent(new PartnerListRequestEvent(RequestResult.CONNECTION_ERROR));
        }
    }

    private boolean rewardNotFoundErrorCode(int errorCode) {
        return errorCode == UKE_REWARD_NOT_FOUND;
    }

    class RewardVoucherServiceResponseParser implements WebServiceResponseParser<SingleAwardedRewardServiceResponse> {
        private final long exchangedRewardId;

        RewardVoucherServiceResponseParser(long exchangedRewardId) {
            this.exchangedRewardId = exchangedRewardId;
        }

        @Override
        public void parseResponse(SingleAwardedRewardServiceResponse response) {
            if (response.awardedReward != null) {
                rewardsRepository.persistRewardVoucher(response.awardedReward);
            }
            resetSelectVoucherState(exchangedRewardId);
            eventDispatcher.dispatchEvent(new RewardVoucherSelectedEvent(exchangedRewardId));
        }

        @Override
        public void parseErrorResponse(String errorBody, int code) {
            eventDispatcher.dispatchEvent(RewardVoucherSelectedEvent.GENERIC_ERROR);
        }

        @Override
        public void handleGenericError(Exception exception) {
            eventDispatcher.dispatchEvent(RewardVoucherSelectedEvent.GENERIC_ERROR);
        }

        @Override
        public void handleConnectionError() {
            eventDispatcher.dispatchEvent(RewardVoucherSelectedEvent.CONNECTION_ERROR);
        }
    }
}
