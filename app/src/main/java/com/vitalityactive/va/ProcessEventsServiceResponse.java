package com.vitalityactive.va;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProcessEventsServiceResponse {
    @SerializedName("processEventsResponse")
    public ProcessEventsResponse processEventsResponse;

    public class EventExternalReference {

        @SerializedName("typeCode")
        public String typeCode;
        @SerializedName("typeKey")
        public Integer typeKey;
        @SerializedName("typeName")
        public String typeName;
        @SerializedName("value")
        public String value;

        public EventExternalReference(String typeCode, Integer typeKey, String typeName, String value) {
            this.typeCode = typeCode;
            this.typeKey = typeKey;
            this.typeName = typeName;
            this.value = value;
        }
    }

    public class Id {

        @SerializedName("eventExternalReference")
        public List<EventExternalReference> eventExternalReference = null;
        @SerializedName("eventId")
        public Integer eventId;

        public Id(List<EventExternalReference> eventExternalReference, Integer eventId) {
            this.eventExternalReference = eventExternalReference;
            this.eventId = eventId;
        }
    }

    public class ProcessEventsResponse {

        @SerializedName("ids")
        @Expose
        public List<Id> ids = null;

        public ProcessEventsResponse(List<Id> ids) {
            this.ids = ids;
        }
    }
}
