package com.vitalityactive.va.snv.history.views.model;

import com.vitalityactive.va.snv.dto.HistoryDetailDto;

import java.util.List;

/**
 * Created by dharel.h.rosell on 12/6/2017.
 */

public class SNVMainAdapterModel {

    private List<HistoryDetailDto> eventList;
    private String eventLabel;

    public SNVMainAdapterModel(String eventLabel, List<HistoryDetailDto> eventList) {
        this.eventLabel = eventLabel;
        this.eventList = eventList;
    }

    public List<HistoryDetailDto> getEventList() {
        return eventList;
    }

    public String getEventLabel() {
        return eventLabel;
    }
}
