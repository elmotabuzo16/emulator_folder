package com.vitalityactive.va.activerewards;

import io.realm.RealmObject;

public class ActiveRewardsActivationState extends RealmObject
{
    private boolean activated;
    private int activationError;

    public boolean isActivated()
    {
        return activated;
    }

    public void setActivated(boolean activated)
    {
        this.activated = activated;
    }

    public int getActivationError()
    {
        return activationError;
    }

    public void setActivationError(int activationError)
    {
        this.activationError = activationError;
    }

    public ActivationErrorType getActivationErrorType()
    {
        if (activationError == ActivationErrorType.NONE.getValue()) {
            return ActivationErrorType.NONE;
        } else if (activationError == ActivationErrorType.CONNECTION.getValue()) {
            return ActivationErrorType.CONNECTION;
        } else {
            return ActivationErrorType.GENERIC;
        }
    }
}
