package com.vitalityactive.va.activerewards.rewards.events;

public class RewardVoucherSelectedEvent {
    public static final RewardVoucherSelectedEvent GENERIC_ERROR = new RewardVoucherSelectedEvent();
    public static final RewardVoucherSelectedEvent CONNECTION_ERROR = new RewardVoucherSelectedEvent();
    public static final RewardVoucherSelectedEvent NONE = new RewardVoucherSelectedEvent();
    private final long rewardUniqueId;

    public RewardVoucherSelectedEvent(long rewardUniqueId) {
        this.rewardUniqueId = rewardUniqueId;
    }

    private RewardVoucherSelectedEvent() {
        this(0);
    }

    public long getRewardUniqueId() {
        return rewardUniqueId;
    }
}
