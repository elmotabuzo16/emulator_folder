package com.vitalityactive.va.activerewards.participatingpartners;

import android.support.annotation.NonNull;

import com.vitalityactive.va.activerewards.rewards.service.RewardsServiceClient;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@ActiveRewardsScope
public class ParticipatingPartnersInteractor {

    private RewardsServiceClient serviceClient;
    private PartnerRepository partnerRepository;
    private EventDispatcher eventDispatcher;

    @Inject
    ParticipatingPartnersInteractor(RewardsServiceClient serviceClient,
                      PartnerRepository partnerRepository,
                      EventDispatcher eventDispatcher) {
        this.serviceClient = serviceClient;
        this.partnerRepository = partnerRepository;
        this.eventDispatcher = eventDispatcher;
    }

    public void fetchRewardPartners() {
        serviceClient.fetchActiveRewardsPartners(new PartnerListResponseParser());
    }

    public List<PartnerItemDTO> getActiveRewardsPartners() {
        return partnerRepository.getPartnerItems(PartnerType.REWARDS.productFeatureCategoryTypeKey);
    }

    class PartnerListResponseParser implements WebServiceResponseParser<PartnerListResponse> {
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

}
