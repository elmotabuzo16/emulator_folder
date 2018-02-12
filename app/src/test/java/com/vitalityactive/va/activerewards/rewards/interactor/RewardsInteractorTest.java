package com.vitalityactive.va.activerewards.rewards.interactor;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.activerewards.rewards.persistence.RewardsRepository;
import com.vitalityactive.va.activerewards.rewards.service.RewardsServiceClient;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.partnerjourney.repository.PartnerRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

public class RewardsInteractorTest {

    @Mock
    private RewardsServiceClient serviceClient;
    @Mock
    private RewardsRepository rewardsRepository;
    @Mock
    private PartnerRepository partnerRepository;
    @Mock
    private EventDispatcher eventDispatcher;
    @Mock
    private InsurerConfigurationRepository insurerConfigRepo;

    private RewardsInteractor interactor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        interactor = new RewardsInteractor(serviceClient, rewardsRepository, partnerRepository, eventDispatcher, insurerConfigRepo);
    }

    @Test
    public void should_fetch_partner_registered_email() throws Exception {
        interactor.fetchPartnerRegisteredEmail();

        ArgumentCaptor<RewardsInteractor.PartnerEmailResponseParser> responseParserCaptor =
                ArgumentCaptor.forClass(RewardsInteractor.PartnerEmailResponseParser.class);
        verify(serviceClient).fetchPartnerRegisteredEmail(responseParserCaptor.capture());
        assertNotNull(responseParserCaptor.getValue());
    }

    @Test
    public void should_update_partner_registered_email() throws Exception {
        String email = "test@mail.com";
        interactor.updatePartnerRegisteredEmail(email);

        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<RewardsInteractor.UpdatePartnerRegisteredEmailResponseParser> responseParserCaptor =
                ArgumentCaptor.forClass(RewardsInteractor.UpdatePartnerRegisteredEmailResponseParser.class);
        verify(serviceClient).updatePartnerRegisteredEmail(stringCaptor.capture(), responseParserCaptor.capture());
        assertEquals(email, stringCaptor.getValue());
        assertNotNull(responseParserCaptor.getValue());
    }

    @Test
    public void should_fetch_reward_voucher() throws Exception {
        long uniqueId = 1234;
        interactor.fetchRewardVoucher(uniqueId);

        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<RewardsInteractor.RewardVoucherServiceResponseParser> responseParserCaptor =
                ArgumentCaptor.forClass(RewardsInteractor.RewardVoucherServiceResponseParser.class);
        verify(serviceClient).fetchSingleAwardedReward(longCaptor.capture(), responseParserCaptor.capture());
        assertThat(longCaptor.getValue(), is(uniqueId));
        assertNotNull(responseParserCaptor.getValue());
    }

    @Test
    public void shouldShowRewardPartnerDataSharingConsent() throws Exception {
        long rewardKey = 1;
        interactor.shouldShowRewardPartnerDataSharingConsent(rewardKey);
        verify(insurerConfigRepo).shouldShowRewardPartnerDataSharingConsent(rewardKey);
    }

    @Test
    public void should_return_wheel_spin_reward() throws Exception {
        long uniqueId = 1234;
        interactor.getWheelSpin(uniqueId);
        verify(rewardsRepository).getUnclaimedReward(uniqueId);
    }

    @Test
    public void should_report_when_fetching_reward_selections() throws Exception {
        interactor.isFetchingRewardSelections(0);
        verify(serviceClient).isFetchingRewardVoucher(0);
    }

    @Test
    public void should_return_selections_cinema_reward_with_given_id() throws Exception {
        long uniqueId = 1234;
        interactor.getSelectionsForCinemaRewardWithId(uniqueId);
        verify(rewardsRepository).getSelectionsForRewardWithUniqueId(uniqueId);
    }

    @Test
    public void should_return_reward_voucher_by_given_id() throws Exception {
        long voucherId = 1234;
        interactor.getRewardVoucherById(voucherId);
        verify(rewardsRepository).getRewardVoucher(voucherId);
    }

    @Test
    public void should_return_selections_for_wheel_spin_with_given_id() throws Exception {
        long uniqueId = 1234;
        interactor.getSelectionsForWheelSpinWithId(uniqueId);
        verify(rewardsRepository).getSelectionsForRewardWithUniqueId(uniqueId);
    }

    @Test
    public void should_return_unclaimed_rewards_count() throws Exception {
        interactor.getUnclaimedRewardsCount();
        verify(rewardsRepository).getUnclaimedRewardsCount();
    }

    @Test
    public void should_fetch_current_rewards() throws Exception {
        interactor.fetchCurrentRewards();

        ArgumentCaptor<RewardsInteractor.RewardsServiceResponseParser> responseParserCaptor =
                ArgumentCaptor.forClass(RewardsInteractor.RewardsServiceResponseParser.class);
        verify(serviceClient).fetchCurrentRewards(responseParserCaptor.capture());
        assertNotNull(responseParserCaptor.getValue());
    }

    @Test
    public void should_return_available_unclaimed_rewards() throws Exception {
        interactor.getAvailableUnclaimedRewards();
        verify(rewardsRepository).getAvailableUnclaimedRewards();
    }

    @Test
    public void should_return_available_reward_vouchers() throws Exception {
        interactor.getAvailableRewardVouchers();
        verify(rewardsRepository).getAvailableRewardVouchers();
    }

    @Test
    public void should_return_partner_registered_email() throws Exception {
        interactor.getPartnerRegisteredEmail();
        verify(rewardsRepository).getPartnerRegisteredEmail();
    }

    @Test
    public void isFetchingPartnerRegisteredEmail() throws Exception {
        interactor.isFetchingPartnerRegisteredEmail();
        verify(serviceClient).isPartnerRegisteredEmailRequestInProgress();
    }

}