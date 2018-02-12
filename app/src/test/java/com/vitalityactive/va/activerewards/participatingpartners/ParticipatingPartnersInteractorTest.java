package com.vitalityactive.va.activerewards.participatingpartners;

import com.vitalityactive.va.activerewards.rewards.service.RewardsServiceClient;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.partnerjourney.PartnerType;
import com.vitalityactive.va.partnerjourney.repository.PartnerRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;


public class ParticipatingPartnersInteractorTest {

    @Mock
    private RewardsServiceClient serviceClient;
    @Mock
    private PartnerRepository partnerRepository;
    @Mock
    private EventDispatcher eventDispatcher;
    @Captor
    private ArgumentCaptor<ParticipatingPartnersInteractor.PartnerListResponseParser> responseParserCaptor;

    private ParticipatingPartnersInteractor interactor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        interactor = new ParticipatingPartnersInteractor(serviceClient, partnerRepository, eventDispatcher);
    }

    @Test
    public void should_fetch_participating_partners() throws Exception {
        interactor.fetchRewardPartners();
        verify(serviceClient).fetchActiveRewardsPartners(responseParserCaptor.capture());
        assertNotNull(responseParserCaptor.getValue());
    }

    @Test
    public void should_return_persisted_participating_partners() throws Exception {
        interactor.getActiveRewardsPartners();
        verify(partnerRepository).getPartnerItems(PartnerType.REWARDS.productFeatureCategoryTypeKey);
    }

}