package com.vitalityactive.va.activerewards;

public enum ActivationErrorType
{
    NONE(0),
    CONNECTION(1),
    GENERIC(2);

    private final int value;

    ActivationErrorType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
