package com.vitalityactive.va.eventsfeed.data.dto;

public class EventsFeedCategoryDTO {
    private final int typeKey;
    private final String title;

    public EventsFeedCategoryDTO(int typeKey, String title) {
        this.typeKey = typeKey;
        this.title = title;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAll() {
        return typeKey == 999;
    }

    public boolean isOther() {
        return typeKey == -1;
    }
}
