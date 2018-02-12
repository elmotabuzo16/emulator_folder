package com.vitalityactive.va.activerewards.rewards.history;

class RewardsHistoryRequestCompletedEvent {
    public static final RewardsHistoryRequestCompletedEvent SUCCESSFUL = new RewardsHistoryRequestCompletedEvent();
    public static final RewardsHistoryRequestCompletedEvent GENERIC_ERROR = new RewardsHistoryRequestCompletedEvent();
    public static final RewardsHistoryRequestCompletedEvent CONNECTION_ERROR = new RewardsHistoryRequestCompletedEvent();
}
