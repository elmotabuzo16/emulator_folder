package com.vitalityactive.va.activerewards.rewards.dto;

import com.vitalityactive.va.activerewards.rewards.persistence.RewardVoucher;
import com.vitalityactive.va.utilities.date.LocalDate;

import java.util.List;

public class RewardVoucherDTO extends RewardDTO {

    public final String rewardValue;
    public final String rewardType;
    public final boolean showCodePending;
    public final List<String> voucherNumbers;
    public final boolean issueFailed;
    public boolean availableToRedeem;
    public final LocalDate effectiveFromDate;

    public RewardVoucherDTO(RewardVoucher rewardVoucher) {
        super(rewardVoucher.getExpiryDate(), rewardVoucher.getId(), rewardVoucher.getRewardId(), rewardVoucher.getName());
        rewardValue = rewardVoucher.getRewardValue();
        rewardType = rewardVoucher.getRewardType();
        showCodePending = rewardVoucher.getShowCodePending();
        voucherNumbers = rewardVoucher.getVoucherNumbers();
        issueFailed = rewardVoucher.isIssueFailed();
        availableToRedeem = rewardVoucher.isAvailableToRedeem();
        effectiveFromDate = new LocalDate(rewardVoucher.getEffectiveFromDate());
    }
}
