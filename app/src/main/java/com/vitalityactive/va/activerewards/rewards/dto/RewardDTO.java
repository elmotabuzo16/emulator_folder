package com.vitalityactive.va.activerewards.rewards.dto;

import com.vitalityactive.va.utilities.date.LocalDate;

public class RewardDTO {
    public final LocalDate expiryDate;
    public final long uniqueId;
    public final int rewardId;
    public final String name;

    public RewardDTO(String expiryDate, long uniqueId, int rewardId, String name) {
        this.expiryDate = new LocalDate(expiryDate);
        this.uniqueId = uniqueId;
        this.rewardId = rewardId;
        this.name = name;
    }
}
