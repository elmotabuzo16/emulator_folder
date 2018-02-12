package com.vitalityactive.va.home;

import com.vitalityactive.va.dto.RewardHomeCardDTO;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RewardHomeCardDtoTests {
    @Test
    public void can_get_status() {
        RewardHomeCardDTO card = new RewardHomeCardDTO();

        card.status = HomeCardType.StatusType.PENDING;
        assertTrue(card.isPending());

        card.status = HomeCardType.StatusType.AVAILABLE_TO_REDEEM;
        assertTrue(card.isAvailableToRedeem());

        card.status = HomeCardType.StatusType.AVAILABLE;
        assertTrue(card.isAvailable());
    }
}
