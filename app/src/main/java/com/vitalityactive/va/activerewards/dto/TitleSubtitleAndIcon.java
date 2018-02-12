package com.vitalityactive.va.activerewards.dto;

public class TitleSubtitleAndIcon extends TitleAndSubtitle {
    private int iconResourceId;
    private String iconFileName = "";

    public TitleSubtitleAndIcon(String title, String subtitle, int iconResourceId) {
        super(title, subtitle);
        this.iconResourceId = iconResourceId;
    }

    public TitleSubtitleAndIcon(String title, String subtitle, String iconFileName) {
        super(title, subtitle);
        this.iconFileName = iconFileName;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public String getIconFileName() {
        return iconFileName;
    }
}
