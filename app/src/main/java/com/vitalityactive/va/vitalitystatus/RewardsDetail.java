package com.vitalityactive.va.vitalitystatus;

public class RewardsDetail {
    private int iconResourceId;
    private String partnerName;
    private String discount;
    private String description;

    public RewardsDetail(String partnerName, String discount, String description, int iconResourceId) {
        this.partnerName = partnerName;
        this.discount = discount;
        this.description = description;
        this.iconResourceId = iconResourceId;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public String getDiscount() {
        return discount;
    }

    public String getDescription() {
        return description;
    }
}
