package com.vitalityactive.va.networking.model;

import com.google.gson.annotations.SerializedName;
import com.vitalityactive.va.constants.EventMetadataType;
import com.vitalityactive.va.constants.EventSource;
import com.vitalityactive.va.constants.PartyReferenceType;

import java.util.Arrays;
import java.util.List;

public class ProcessEventsV2ServiceRequest {
    @SerializedName("processEventsV2Request")
    public ProcessEventsV2Request processEventsV2Request;

    public ProcessEventsV2ServiceRequest(long[] eventIds, String now, long partyId, String rewardName, String rewardKey, String instructionType) {
        processEventsV2Request = new ProcessEventsV2Request(eventIds, now, partyId, rewardName, rewardKey, instructionType);
    }

    public static class ProcessEventsV2Request {
        @SerializedName("partyReferenceEvents")
        public PartyReferenceEvents[] events;

        public ProcessEventsV2Request(long[] eventIds, String now, long partyId, String rewardName, String rewardKey, String instructionType) {
            events = new PartyReferenceEvents[eventIds.length];
            int i = 0;
            for (long eventTypeKey : eventIds) {
                events[i++] = new PartyReferenceEvents(eventTypeKey, now, partyId, rewardName, rewardKey, instructionType);
            }
        }
    }

    public static class PartyReferenceEvents {
        @SerializedName("applicableTo")
        public ApplicableTo applicableTo;
        @SerializedName("associatedEvents")
        public AssociatedEvent[] associatedEvents;
        @SerializedName("eventCapturedOn")
        public String eventCapturedOn;
        @SerializedName("eventExternalReference")
        public EventExternalReference[] eventExternalReference;
        @SerializedName("eventMetaData")
        public EventMetadata[] eventMetadata;
        @SerializedName("eventOccuredOn")
        public String eventOccurredOn;
        @SerializedName("eventSourceTypeKey")
        public Integer eventSourceTypeKey;
        @SerializedName("reportedBy")
        public ReportedBy reportedBy;
        @SerializedName("typeKey")
        public Long typeKey;
        @SerializedName("eventId")
        public Integer eventId;

        public PartyReferenceEvents(long eventTypeKey, String now, long partyId, String rewardName, String rewardKey, String instructionType) {
            eventSourceTypeKey = EventSource._MOBILEAPP;

            eventMetadata = new EventMetadata[3];
            eventMetadata[0] = new EventMetadata(EventMetadataType._REWARDNAME, rewardName);
            eventMetadata[1] = new EventMetadata(EventMetadataType._INSTRUCTIONTYPEKEY, instructionType);
            eventMetadata[2] = new EventMetadata(EventMetadataType._REWARDKEY, rewardKey);

            typeKey = eventTypeKey;
            eventCapturedOn = now;
            eventOccurredOn = now;
            reportedBy = new ReportedBy(partyId);
            applicableTo = new ApplicableTo(partyId);
        }
    }

    public static class EventMetadata {
        @SerializedName("typeKey")
        public long typeKey;
        @SerializedName("value")
        public String value;

        public EventMetadata(int type, String value) {
            typeKey = type;
            this.value = value;
        }
    }

    private static class ApplicableTo {
        @SerializedName("referenceTypeKey")
        public int referenceTypeKey;
        @SerializedName("value")
        public String value;

        public ApplicableTo(long partyId) {
            referenceTypeKey = PartyReferenceType._PARTY;
            value = String.valueOf(partyId);
        }
    }

    private static class ReportedBy {
        @SerializedName("referenceTypeKey")
        public int referenceTypeKey;
        @SerializedName("value")
        public String value;

        public ReportedBy(long partyId) {
            value = String.valueOf(partyId);
            referenceTypeKey = PartyReferenceType._PARTY;
        }
    }

    public class AssociatedEvent {
        @SerializedName("associatedEventApplicableTo")
        public AssociatedEventApplicableTo associatedEventApplicableTo;
        @SerializedName("associatedEventExternalReference")
        public AssociatedEventExternalReference[] associatedEventExternalReference = null;
        @SerializedName("associatedEventMetadatas")
        public AssociatedEventMetadata[] associatedEventMetadatas = null;
        @SerializedName("associatedEventReportedBy")
        public AssociatedEventReportedBy associatedEventReportedBy;
        @SerializedName("eventAssociationTypeKey")
        public Integer eventAssociationTypeKey;
        @SerializedName("eventCapturedOn")
        public String eventCapturedOn;
        @SerializedName("eventId")
        public Integer eventId;
        @SerializedName("eventOccuredOn")
        public String eventOccuredOn;
        @SerializedName("eventSourceTypeKey")
        public Integer eventSourceTypeKey;
        @SerializedName("typeKey")
        public Integer typeKey;

        public AssociatedEvent(AssociatedEventApplicableTo associatedEventApplicableTo, List<AssociatedEventExternalReference> associatedEventExternalReference, List<AssociatedEventMetadata> associatedEventMetadatas, AssociatedEventReportedBy associatedEventReportedBy, Integer eventAssociationTypeKey, String eventCapturedOn, Integer eventId, String eventOccuredOn, Integer eventSourceTypeKey, Integer typeKey) {
            this.associatedEventApplicableTo = associatedEventApplicableTo;
            this.associatedEventExternalReference = Arrays.copyOf(associatedEventExternalReference.toArray(), associatedEventExternalReference.size(), AssociatedEventExternalReference[].class);
            this.associatedEventMetadatas = Arrays.copyOf(associatedEventMetadatas.toArray(), associatedEventMetadatas.size(), AssociatedEventMetadata[].class);
            this.associatedEventReportedBy = associatedEventReportedBy;
            this.eventAssociationTypeKey = eventAssociationTypeKey;
            this.eventCapturedOn = eventCapturedOn;
            this.eventId = eventId;
            this.eventOccuredOn = eventOccuredOn;
            this.eventSourceTypeKey = eventSourceTypeKey;
            this.typeKey = typeKey;
        }
    }

    public class AssociatedEventApplicableTo {

        @SerializedName("referenceTypeKey")
        public Integer referenceTypeKey;
        @SerializedName("value")
        public String value;


        public AssociatedEventApplicableTo(Integer referenceTypeKey, String value) {
            this.referenceTypeKey = referenceTypeKey;
            this.value = value;
        }
    }

    public class AssociatedEventExternalReference {

        @SerializedName("typeKey")
        public Integer typeKey;
        @SerializedName("value")
        public String value;

        public AssociatedEventExternalReference(Integer typeKey, String value) {
            this.typeKey = typeKey;
            this.value = value;
        }
    }

    public class AssociatedEventMetadata {

        @SerializedName("typeKey")
        private Integer typeKey;
        @SerializedName("unitOfMeasure")
        private String unitOfMeasure;
        @SerializedName("value")
        private String value;

        public AssociatedEventMetadata(Integer typeKey, String unitOfMeasure, String value) {
            this.typeKey = typeKey;
            this.unitOfMeasure = unitOfMeasure;
            this.value = value;
        }
    }

    public class AssociatedEventReportedBy {

        @SerializedName("referenceTypeKey")
        private Integer referenceTypeKey;
        @SerializedName("value")
        private String value;

        public AssociatedEventReportedBy(Integer referenceTypeKey, String value) {
            this.referenceTypeKey = referenceTypeKey;
            this.value = value;
        }
    }

    public class EventExternalReference {

        @SerializedName("typeKey")
        public Integer typeKey;
        @SerializedName("value")
        public String value;

        public EventExternalReference(Integer typeKey, String value) {
            this.typeKey = typeKey;
            this.value = value;
        }
    }


}
