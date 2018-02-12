package com.vitalityactive.va.networking.model;

import com.google.gson.annotations.SerializedName;
import com.vitalityactive.va.constants.EventAssociationType;
import com.vitalityactive.va.constants.EventMetadataType;
import com.vitalityactive.va.constants.EventSource;
import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.utilities.date.Date;
import com.vitalityactive.va.vhc.captureresults.models.CapturedField;

import java.util.List;

public class ProcessEventsServiceRequest {
    @SerializedName("processEventsRequest")
    public ProcessEventsRequest processEventsRequest;

    public ProcessEventsServiceRequest(List<CapturedField> capturedFields, String date, long partyId, List<ProofItemDTO> proofItems, TimeUtilities timeUtilities) {
        processEventsRequest = new ProcessEventsRequest(capturedFields, date, partyId, proofItems, timeUtilities);
    }

    public ProcessEventsServiceRequest(long[] eventTypeKeys, String date, long partyId) {
        processEventsRequest = new ProcessEventsRequest(eventTypeKeys, date, partyId);
    }

    public static class ProcessEventsRequest {
        @SerializedName("eventIn")
        public Event[] events;

        public ProcessEventsRequest(List<CapturedField> capturedFields, String capturedOnDate, long partyId, List<ProofItemDTO> proofItems, TimeUtilities timeUtilities) {
            Event event = new Event(capturedOnDate, partyId, EventType._DOCUMENTUPLOADSVHC, capturedOnDate);
            event.associatedEvents = new AssociatedEvent[capturedFields.size()];
            int i = 0;
            for (CapturedField capturedField : capturedFields) {
                String occurredOnDate = timeUtilities.getStringOfISODateWithZoneId(new Date(capturedField.getDateTested()).getValue());
                AssociatedEvent associatedEvent = new AssociatedEvent(occurredOnDate, partyId, Long.valueOf(capturedField.key), capturedOnDate, new AssociatedEventMetadata(capturedField));
                event.associatedEvents[i++] = associatedEvent;
            }
            event.eventMetadata = new EventMetadata[proofItems.size()];
            i = 0;
            for (ProofItemDTO proofItem : proofItems) {
                event.eventMetadata[i++] = new EventMetadata(proofItem.getReferenceId());
            }
            events = new Event[] {event};
        }

        public ProcessEventsRequest(long[] eventTypeKeys, String date, long partyId) {
            events = new Event[eventTypeKeys.length];
            int i = 0;
            for (long eventTypeKey : eventTypeKeys) {
                events[i++] = new Event(date, partyId, eventTypeKey, date);
            }
        }
    }

    public static class Event extends BaseEvent {
        @SerializedName("associatedEvent")
        public AssociatedEvent[] associatedEvents;
        @SerializedName("eventMetaData")
        public EventMetadata[] eventMetadata;

        public Event(String occurredOnDate, long partyId, long typeKey, String capturedOnDate) {
            super(occurredOnDate, partyId, typeKey, capturedOnDate);
        }
    }

    public static class AssociatedEvent extends BaseEvent {
        @SerializedName("eventAssociationTypeKey")
        public long eventAssociationTypeKey;
        @SerializedName("associatedEventMetadatas")
        public AssociatedEventMetadata[] associatedEventMetadata;

        public AssociatedEvent(String occurredOnDate, long partyId, long typeKey, String capturedOnDate, AssociatedEventMetadata associatedEventMetadata) {
            super(occurredOnDate, partyId, typeKey, capturedOnDate);
            eventAssociationTypeKey = EventAssociationType._DOCUMENTARYPROOF;
            this.associatedEventMetadata = new AssociatedEventMetadata[] {associatedEventMetadata};
        }
    }

    private static class BaseEvent {
        @SerializedName("eventOccuredOn")
        public String eventOccurredOn;
        @SerializedName("typeKey")
        public Long typeKey;
        @SerializedName("eventSourceTypeKey")
        public Integer eventSourceTypeKey;
        @SerializedName("eventCapturedOn")
        public String eventCapturedOn;
        @SerializedName("applicableTo")
        public long applicableTo;
        @SerializedName("reportedBy")
        public long reportedBy;

        public BaseEvent(String occurredOnDate, long partyId, long typeKey, String capturedOnDate) {
            eventOccurredOn = occurredOnDate;
            eventCapturedOn = capturedOnDate;
            applicableTo = partyId;
            reportedBy = partyId;
            eventSourceTypeKey = EventSource._MOBILEAPP;
            this.typeKey = typeKey;
        }
    }

    public static class AssociatedEventMetadata {
        @SerializedName("typeKey")
        public long typeKey;
        @SerializedName("unitOfMeasure")
        public String unitOfMeasure;
        @SerializedName("value")
        public String value;

        public AssociatedEventMetadata(CapturedField capturedField) {
            typeKey = EventMetadataType._VALUE;
            String typeKey = capturedField.getSelectedUnitOfMeasure().getTypeKey();
            unitOfMeasure = typeKey.equals("-1") ? null : typeKey;
            value = capturedField.getValueForSubmission();
        }

    }

    public static class EventMetadata {
        @SerializedName("typeKey")
        public long typeKey;
        @SerializedName("value")
        public String value;

        public EventMetadata(String value) {
            typeKey = EventMetadataType._DOCUMENTREFERENCE;
            this.value = value;
        }
    }
}
