package com.vitalityactive.va.eventsfeed;

import com.vitalityactive.va.eventsfeed.data.dto.EventDTO;
import com.vitalityactive.va.utilities.date.Date;

import java.util.List;

public class EventsFeedDay {
    private List<EventDTO> events;
    private Date date;

    public EventsFeedDay(Date date, List<EventDTO> events) {
        this.date = date;
        this.events = events;
    }

    public List<EventDTO> getPointsEntries() {
        return events;
    }

    public Date getDate() {
        return date;
    }
}
