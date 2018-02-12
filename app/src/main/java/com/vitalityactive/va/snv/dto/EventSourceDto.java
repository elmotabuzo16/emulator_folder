package com.vitalityactive.va.snv.dto;

/**
 * Created by kerry.e.lawagan on 11/27/2017.
 */

public class EventSourceDto {
    private String eventSourceCode;
    private int eventSourcekey;
    private String eventSourceName;
    private String note;

    public String getEventSourceCode() {
        return eventSourceCode;
    }

    public void setEventSourceCode(String eventSourceCode) {
        this.eventSourceCode = eventSourceCode;
    }

    public int getEventSourcekey() {
        return eventSourcekey;
    }

    public void setEventSourcekey(int eventSourcekey) {
        this.eventSourcekey = eventSourcekey;
    }

    public String getEventSourceName() {
        return eventSourceName;
    }

    public void setEventSourceName(String eventSourceName) {
        this.eventSourceName = eventSourceName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
