package com.vitalityactive.va.networking.model;

import com.google.gson.annotations.SerializedName;
import com.vitalityactive.va.constants.EventAssociationType;
import com.vitalityactive.va.constants.EventMetadataType;
import com.vitalityactive.va.constants.EventSource;
import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.snv.dto.ConfirmAndSubmitItemDTO;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.utilities.date.Date;

import java.util.List;

public class ProcessEventsServiceRequestSNV {
    @SerializedName("processEventsRequest")
    public ProcessEventsRequest processEventsRequest;

    public ProcessEventsServiceRequestSNV(List<ConfirmAndSubmitItemDTO> screeningItems, List<ConfirmAndSubmitItemDTO> vaccinationItems, String date, long partyId, List<ProofItemDTO> proofItems, TimeUtilities timeUtilities) {
        processEventsRequest = new ProcessEventsRequest(screeningItems, vaccinationItems, date, partyId, proofItems, timeUtilities);
    }

    public static class ProcessEventsRequest {
        @SerializedName("eventIn")
        public Event[] events;

        public ProcessEventsRequest(List<ConfirmAndSubmitItemDTO> screeningItems, List<ConfirmAndSubmitItemDTO> vaccinationItems, String capturedOnDate, long partyId, List<ProofItemDTO> proofItems, TimeUtilities timeUtilities) {
            Event event = new Event(capturedOnDate, partyId, EventType._DOCUMENTUPLOADSSV, capturedOnDate);
            event.associatedEvents = new AssociatedEvent[screeningItems.size() + vaccinationItems.size()];
            int i = 0;

            // Add Screenings
            for (ConfirmAndSubmitItemDTO screeningItem : screeningItems) {
                String occurredOnDate = timeUtilities.getStringOfISODateWithZoneId(new Date(screeningItem.getDateTestedInLong()).getValue());
                AssociatedEvent associatedEvent = new AssociatedEvent(occurredOnDate, partyId, screeningItem.getTypeKey(), capturedOnDate);
                event.associatedEvents[i++] = associatedEvent;
            }

            // Add Vaccinations
            for (ConfirmAndSubmitItemDTO vaccinationItem : vaccinationItems) {
                String occurredOnDate = timeUtilities.getStringOfISODateWithZoneId(new Date(vaccinationItem.getDateTestedInLong()).getValue());
                AssociatedEvent associatedEvent = new AssociatedEvent(occurredOnDate, partyId, vaccinationItem.getTypeKey(), capturedOnDate);
                event.associatedEvents[i++] = associatedEvent;
            }

            event.eventMetadata = new EventMetadata[proofItems.size()];
            i = 0;
            for (ProofItemDTO proofItem : proofItems) {
                event.eventMetadata[i++] = new EventMetadata(proofItem.getReferenceId());
            }
            events = new Event[] {event};
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

        public AssociatedEvent(String occurredOnDate, long partyId, long typeKey, String capturedOnDate) {
            super(occurredOnDate, partyId, typeKey, capturedOnDate);
            eventAssociationTypeKey = EventAssociationType._DOCUMENTARYPROOF;
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
