package com.vitalityactive.va.snv.dto;

import com.vitalityactive.va.persistence.models.screeningsandvaccinations.EventType;
import com.vitalityactive.va.persistence.models.screeningsandvaccinations.GetPotentialPointsAndEventsCompletedPointsFeedback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerry.e.lawagan on 11/27/2017.
 */

public class GetPotentialPointsAndEventsCompletedPointsDto {
    private List<EventTypeDto> eventTypes;
    private List<WarningDto> warnings;

    public GetPotentialPointsAndEventsCompletedPointsDto(GetPotentialPointsAndEventsCompletedPointsFeedback model) {
        eventTypes = new ArrayList<EventTypeDto>();

        for (EventType e: model.getEventTypes()) {
            eventTypes.add(new EventTypeDto(e));
        }
    }

    public List<EventTypeDto> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<EventTypeDto> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public List<WarningDto> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<WarningDto> warnings) {
        this.warnings = warnings;
    }
}
