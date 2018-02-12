package com.vitalityactive.va.activerewards.rewards.persistence;

import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.activerewards.rewards.dto.RewardSelectionDTO;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.activerewards.rewards.dto.UnclaimedRewardDTO;
import com.vitalityactive.va.activerewards.rewards.service.PartnerEmailResponse;
import com.vitalityactive.va.activerewards.rewards.service.SingleAwardedRewardServiceResponse;
import com.vitalityactive.va.activerewards.rewards.service.RewardsServiceResponse;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RepositoryTests
public class RewardsRepositoryTest extends RepositoryTestBase {

    private static final long VOUCHER_REWARD_UNIQUE_ID = 300402;
    private static final long WHEEL_SPIN_REWARD_UNIQUE_ID = 300406;

    private RewardsServiceResponse rewardsServiceResponse;
    private SingleAwardedRewardServiceResponse voucherServiceResponse;

    private RewardsRepository repository;

    @Before
    public void setUp() throws IOException {
        super.setUp();
        repository = new RewardsRepository(dataStore);
    }

    @Test
    public void should_persist_and_retrieve_partner_registered_email() throws Exception {
        PartnerEmailResponse response = getResponse(PartnerEmailResponse.class, "ar/get_party_by_id_starbucks_email_response.json");

        assertTrue(repository.persistPartnerRegisteredEmail(response));

        assertThat(repository.getPartnerRegisteredEmail(),
                is(response.response.party.generalPreferences.get(0).value));
    }

    @Test
    public void should_return_unclaimed_rewards_count() throws Exception {
        setupRewardsServiceResponse();
        assertThat(repository.getUnclaimedRewardsCount(), is(1L));
    }

    @Test
    public void should_return_available_unclaimed_rewards() throws Exception {
        setupRewardsServiceResponse();
        List<UnclaimedRewardDTO> unclaimedRewards = repository.getAvailableUnclaimedRewards();
        assertThat(unclaimedRewards.size(), is(1));
    }

    @Test
    public void should_return_available_reward_vouchers() throws Exception {
        setupRewardVoucherServiceResponse("ar/get_awarded_reward_by_id_allocated_response.json");
        List<RewardVoucherDTO> rewardVouchers = repository.getAvailableRewardVouchers();
        assertThat(rewardVouchers.size(), is(1));
    }

    @Test
    public void should_return_unclaimed_reward() throws Exception {
        setupRewardsServiceResponse();
        UnclaimedRewardDTO unclaimedReward = repository.getUnclaimedReward(WHEEL_SPIN_REWARD_UNIQUE_ID);
        assertThat(unclaimedReward.name, is("WheelSpin"));
    }

    @Test
    public void should_return_reward_voucher() throws Exception {
        setupRewardVoucherServiceResponse("ar/get_awarded_reward_by_id_allocated_response.json");
        RewardVoucherDTO rewardVoucher = repository.getRewardVoucher(VOUCHER_REWARD_UNIQUE_ID);
        assertNotNull(rewardVoucher.name, is("Cineworld or Vue"));
    }

    @Test
    public void should_remove_wheel_spin() throws Exception {
        setupRewardsServiceResponse();
        repository.removeWheelSpin(WHEEL_SPIN_REWARD_UNIQUE_ID);
        assertNull(repository.getUnclaimedReward(WHEEL_SPIN_REWARD_UNIQUE_ID));
    }

    @Test
    public void should_remove_voucher_with_selections() throws Exception {
        setupRewardVoucherServiceResponse("ar/get_awarded_reward_by_id_available_to_redeem_response.json");
        repository.removeVoucherWithSelections(VOUCHER_REWARD_UNIQUE_ID);
        List<RewardSelectionDTO> selections = repository.getSelectionsForRewardWithUniqueId(VOUCHER_REWARD_UNIQUE_ID);
        assertThat(selections.size(), is(0));
    }

    @Test
    public void should_return_selections_for_reward_with_unique_id() throws Exception {
        setupRewardVoucherServiceResponse("ar/get_awarded_reward_by_id_available_to_redeem_response.json");
        List<RewardSelectionDTO> selections = repository.getSelectionsForRewardWithUniqueId(VOUCHER_REWARD_UNIQUE_ID);
        assertThat(selections.size(), is(2));
    }

    private void setupRewardVoucherServiceResponse(String responseFilePath) throws IOException {
        voucherServiceResponse = getResponse(SingleAwardedRewardServiceResponse.class, responseFilePath);
        assertTrue(repository.persistRewardVoucher(voucherServiceResponse.awardedReward));
    }

    private void setupRewardsServiceResponse() throws IOException {
        rewardsServiceResponse = getResponse(RewardsServiceResponse.class, "ar/get_awarded_reward_by_party_id_response.json");
        assertTrue(repository.persistCurrentRewards(rewardsServiceResponse));
    }

}