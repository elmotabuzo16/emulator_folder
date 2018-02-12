package com.vitalityactive.va.home;

import com.vitalityactive.va.dto.HomeCardDTO;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HomeCardDtoTests {
    @Test
    public void can_get_status() {
        HomeCardDTO card = new HomeCardDTO();

        card.status = HomeCardType.StatusType.DONE;
        assertTrue(card.isDone());

        card.status = HomeCardType.StatusType.NOT_STARTED;
        assertTrue(card.hasNotStarted());

        card.status = HomeCardType.StatusType.IN_PROGRESS;
        assertTrue(card.isInProgress());

    }
}
