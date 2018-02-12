package com.vitalityactive.va.activerewards;

public interface ActiveRewardsActivator {
    void activate();

    boolean isActivateRequestInProgress();

    class ActivationSucceededEvent {

    }

    class ActivationFailedEvent {
        ActivationErrorType activationErrorType;

        public ActivationFailedEvent() {
            this(ActivationErrorType.GENERIC);
        }

        public ActivationFailedEvent(ActivationErrorType activationErrorType) {
            this.activationErrorType = activationErrorType;
        }

        public ActivationErrorType getActivationErrorType() {
            return activationErrorType;
        }
    }
}
