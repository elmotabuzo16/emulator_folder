package com.vitalityactive.va.vhc.captureresults;

public class ValueLimit {
    private final Float minValue;
    private final Float maxValue;
    private String rangeDetails;

    public ValueLimit(Float minValue, Float maxValue, String rangeDetails) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.rangeDetails = rangeDetails;
    }

    public Float getMaxValue() {
        return maxValue;
    }

    public Float getMinValue() {
        return minValue;
    }

    public String getRangeDetails() {
        return rangeDetails;
    }
}
