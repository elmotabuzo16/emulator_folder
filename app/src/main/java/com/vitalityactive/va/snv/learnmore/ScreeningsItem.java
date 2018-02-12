package com.vitalityactive.va.snv.learnmore;

/**
 * Created by stephen.rey.w.avila on 12/4/2017.
 */

public class ScreeningsItem {
    private String description;
    private int points;

    public ScreeningsItem(String description, int points) {
        this.description = description;
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
