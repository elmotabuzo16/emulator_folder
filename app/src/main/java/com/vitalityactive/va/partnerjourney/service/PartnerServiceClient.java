package com.vitalityactive.va.partnerjourney.service;

import android.support.annotation.NonNull;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.dependencyinjection.partnerjourney.PartnerJourneyScope;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.partnerjourney.PartnerType;
import com.vitalityactive.va.partnerjourney.models.PartnerGroup;
import com.vitalityactive.va.partnerjourney.models.PartnerItem;
import com.vitalityactive.va.partnerjourney.models.PartnerItemDetails;
import com.vitalityactive.va.partnerjourney.repository.PartnerRepository;
import com.vitalityactive.va.partnerjourney.service.models.PartnerDetailResponse;
import com.vitalityactive.va.partnerjourney.service.models.PartnerListResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

@PartnerJourneyScope
public class PartnerServiceClient {
    private final AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;
    private final PartnerRetrievalService service;
    private final WebServiceClient webServiceClient;
    private final PartnerRepository repository;
    private final EventDispatcher eventDispatcher;
    private final MainThreadScheduler scheduler;
    private PartyInformationRepository partyInformationRepository;

    @Inject
    public PartnerServiceClient(PartnerRetrievalService service, WebServiceClient webServiceClient,
                                PartnerRepository repository,
                                AccessTokenAuthorizationProvider accessTokenAuthorizationProvider,
                                EventDispatcher eventDispatcher, MainThreadScheduler scheduler, PartyInformationRepository partyInformationRepository) {
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
        this.service = service;
        this.webServiceClient = webServiceClient;
        this.repository = repository;
        this.eventDispatcher = eventDispatcher;
        this.scheduler = scheduler;
        this.partyInformationRepository = partyInformationRepository;
    }

    public void fetchWellnessPartners(final PartnerType partnerType) {
        String token = accessTokenAuthorizationProvider.getAuthorization();
        Call<PartnerListResponse> call = service.getPartners(token, partyInformationRepository.getTenantId(), partyInformationRepository.getPartyId(), buildPartnerListRequest(partnerType));

        webServiceClient.executeAsynchronousRequest(call, new BasicWebServiceResponseParser<PartnerListResponse, List<PartnerGroup>, PartnerListRequestEvent>() {
            @Override
            public void parseResponse(PartnerListResponse body) {
                final ArrayList<PartnerGroup> groups = buildHealthServiceGroups(body);
                if (!repository.persist(groups)) {
                    failedToPersist();
                    return;
                }

                respond(groups);
            }

            @NonNull
            private ArrayList<PartnerGroup> buildHealthServiceGroups(PartnerListResponse body) {
                final ArrayList<PartnerGroup> groups = new ArrayList<>();

                for (PartnerListResponse.ProductFeatureGroups group: body.productFeatureGroups) {
                    if (!isGroupIncludedForThisPartnerType(group.name, partnerType)) {
                        continue;
                    }

                    PartnerGroup partnerGroup = new PartnerGroup(partnerType.productFeatureCategoryTypeKey, shouldGroupsBeNamed(partnerType) ? group.name : "");
                    for (PartnerListResponse.PartnerResponse response : group.productFeatures) {
                        PartnerItem item = new PartnerItem(partnerType.productFeatureCategoryTypeKey, response.typeKey,
                                response.name, response.description, response.longDescription, response.logoFileName);
                        partnerGroup.items.add(item);
                    }
                    groups.add(partnerGroup);
                }

                return groups;
            }

            @Override
            PartnerListRequestEvent buildSuccessEvent(List<PartnerGroup> result) {
                return new PartnerListRequestEvent(result);
            }

            @Override
            PartnerListRequestEvent buildFailureEvent(RequestResult result) {
                return new PartnerListRequestEvent(result);
            }
        });
    }

    private boolean shouldGroupsBeNamed(PartnerType partnerType) {
        return partnerType != PartnerType.NON_SMOKERS;
    }

    private boolean isGroupIncludedForThisPartnerType(String groupName, PartnerType partnerType) {
        //noinspection SimplifiableIfStatement
        if (partnerType == PartnerType.NON_SMOKERS) {
            return groupName.equals("Stop Smoking");
        }
        return true;
    }

    @NonNull
    private PartnerListRequest buildPartnerListRequest(PartnerType partnerType) {
        PartnerListRequest request = new PartnerListRequest();
        request.productFeatureCategoryTypeKey = partnerType.productFeatureCategoryTypeKey;
        return request;
    }

    public void fetchWellnessPartnerDetails(final long partnerId) {
        String token = accessTokenAuthorizationProvider.getAuthorization();
        Call<PartnerDetailResponse> call = service.getPartnerDetails(token, partyInformationRepository.getTenantId(), partyInformationRepository.getPartyId(), partyInformationRepository.getVitalityMembershipId(), new PartnerDetailRequest(partnerId));

        webServiceClient.executeAsynchronousRequest(call, new BasicWebServiceResponseParser<PartnerDetailResponse, PartnerItemDetails, PartnerDetailRequestEvent>() {
            @Override
            public void parseResponse(PartnerDetailResponse body) {
                PartnerItemDetails mapped = mapModel(body);
                if (repository.persist(mapped)) {
                    respond(mapped);
                } else {
                    failedToPersist();
                }
            }

            private PartnerItemDetails mapModel(PartnerDetailResponse response) {
                return new PartnerItemDetails(partnerId,
                        response.productFeatureEligibilityContent.mainContent,
                        response.productFeatureEligibilityContent.url);
            }

            @Override
            PartnerDetailRequestEvent buildSuccessEvent(PartnerItemDetails result) {
                return new PartnerDetailRequestEvent(result);
            }

            @Override
            PartnerDetailRequestEvent buildFailureEvent(RequestResult result) {
                return new PartnerDetailRequestEvent(result);
            }
        });
    }

    abstract class BasicWebServiceResponseParser<TNetworkModel, dto, TEvent> implements WebServiceResponseParser<TNetworkModel> {
        void respond(final dto result) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    eventDispatcher.dispatchEvent(buildSuccessEvent(result));
                }
            });
        }

        void failedToPersist() {
            handleGenericError(new RuntimeException("Failed to persist in realm"));
        }

        @Override
        public void parseErrorResponse(final String errorBody, final int code) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    eventDispatcher.dispatchEvent(buildFailureEvent(RequestResult.GENERIC_ERROR));
                }
            });
        }

        @Override
        public void handleGenericError(final Exception exception) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    eventDispatcher.dispatchEvent(buildFailureEvent(RequestResult.GENERIC_ERROR));
                }
            });
        }

        @Override
        public void handleConnectionError() {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    eventDispatcher.dispatchEvent(buildFailureEvent(RequestResult.CONNECTION_ERROR));
                }
            });
        }

        abstract TEvent buildSuccessEvent(dto result);

        abstract TEvent buildFailureEvent(RequestResult result);
    }
}
