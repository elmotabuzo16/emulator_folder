package com.vitalityactive.va.vitalitystatus.landing;

import com.vitalityactive.va.activerewards.dto.TitleSubtitleAndIcon;

public class StatusItem extends TitleSubtitleAndIcon {
    private final int progress;

    public StatusItem(String title, String subtitle, int iconResourceId, int progress) {
        super(title, subtitle, iconResourceId);
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }
}
