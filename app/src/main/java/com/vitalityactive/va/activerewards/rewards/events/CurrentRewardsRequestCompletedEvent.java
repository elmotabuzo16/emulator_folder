package com.vitalityactive.va.activerewards.rewards.events;

public class CurrentRewardsRequestCompletedEvent {
    public static final CurrentRewardsRequestCompletedEvent SUCCESSFUL = new CurrentRewardsRequestCompletedEvent();
    public static final CurrentRewardsRequestCompletedEvent REWARD_NOT_FOUND = new CurrentRewardsRequestCompletedEvent();
    public static final CurrentRewardsRequestCompletedEvent GENERIC_ERROR = new CurrentRewardsRequestCompletedEvent();
    public static final CurrentRewardsRequestCompletedEvent CONNECTION_ERROR = new CurrentRewardsRequestCompletedEvent();

    private CurrentRewardsRequestCompletedEvent() {
    }
}
