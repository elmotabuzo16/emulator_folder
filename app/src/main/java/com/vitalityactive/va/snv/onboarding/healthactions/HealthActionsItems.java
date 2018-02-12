package com.vitalityactive.va.snv.onboarding.healthactions;

/**
 * Created by kerry.e.lawagan on 11/27/2017.
 */

public class HealthActionsItems {
    private String description;
    private int points;

    public HealthActionsItems(String description, int points) {
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
