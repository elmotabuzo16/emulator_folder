package com.vitalityactive.va.activerewards.dto;

public class CardMarginSettings {
    public boolean topMargin = true;
    public boolean bottomMargin = false;

    public void showBottomMarginOnly() {
        topMargin = false;
        bottomMargin = true;
    }
}
