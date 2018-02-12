package com.vitalityactive.va.home.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventByPartyResponse {
    @SerializedName("event")
    public List<EventByPartyOutbound> event;

    public List<EventByPartyOutbound> getEvent() {
        return event;
    }

    public static class EventByPartyOutbound {
        @SerializedName("associatedEvents")
        public List<AssociatedEvent> associatedEvents;
        @SerializedName("categoryCode")
        public String categoryCode;
        @SerializedName("categoryKey")
        public int categoryKey;
        @SerializedName("categoryName")
        public String categoryName;
        @SerializedName("dateLogged")
        public String dateLogged;
        @SerializedName("eventDateTime")
        public String eventDateTime;
        @SerializedName("eventExternalReference")
        public List<EventExternalReference> eventExternalReference;
        @SerializedName("eventMetaDataOuts")
        public List<EventMetaData> eventMetaDataOuts;
        @SerializedName("eventSourceCode")
        public String eventSourceCode;
        @SerializedName("eventSourceKey")
        public int eventSourceKey;
        @SerializedName("eventSourceName")
        public String eventSourceName;
        @SerializedName("id")
        public int id;
        @SerializedName("partyId")
        public long partyId;
        @SerializedName("reportedBy")
        public long reportedBy;
        @SerializedName("typeCode")
        public String typeCode;
        @SerializedName("typeKey")
        public int typeKey;
        @SerializedName("typeName")
        public String typeName;

        public List<AssociatedEvent> getAssociatedEvents() {
            return associatedEvents;
        }

        public String getCategoryCode() {
            return categoryCode;
        }

        public int getCategoryKey() {
            return categoryKey;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public String getDateLogged() {
            return dateLogged;
        }

        public String getEventDateTime() {
            return eventDateTime;
        }

        public List<EventExternalReference> getEventExternalReference() {
            return eventExternalReference;
        }

        public List<EventMetaData> getEventMetaDataOuts() {
            return eventMetaDataOuts;
        }

        public String getEventSourceCode() {
            return eventSourceCode;
        }

        public int getEventSourceKey() {
            return eventSourceKey;
        }

        public String getEventSourceName() {
            return eventSourceName;
        }

        public int getId() {
            return id;
        }

        public long getPartyId() {
            return partyId;
        }

        public long getReportedBy() {
            return reportedBy;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public int getTypeKey() {
            return typeKey;
        }

        public String getTypeName() {
            return typeName;
        }
    }

    public static class AssociatedEvent {

        @SerializedName("associationTypeCode")
        public String associationTypeCode;
        @SerializedName("associationTypeKey")
        public int associationTypeKey;
        @SerializedName("associationTypeName")
        public String associationTypeName;
        @SerializedName("categoryCode")
        public String categoryCode;
        @SerializedName("categoryKey")
        public int categoryKey;
        @SerializedName("categoryName")
        public String categoryName;
        @SerializedName("dateLogged")
        public String dateLogged;
        @SerializedName("dateTimeAssociated")
        public String dateTimeAssociated;
        @SerializedName("eventDateTime")
        public String eventDateTime;
        @SerializedName("eventId")
        public int eventId;
        @SerializedName("eventSourceCode")
        public String eventSourceCode;
        @SerializedName("eventSourceKey")
        public int eventSourceKey;
        @SerializedName("eventSourceName")
        public String eventSourceName;
        @SerializedName("eventTypeCode")
        public String eventTypeCode;
        @SerializedName("eventTypeKey")
        public int eventTypeKey;
        @SerializedName("eventTypeName")
        public String eventTypeName;

        public String getAssociationTypeCode() {
            return associationTypeCode;
        }

        public int getAssociationTypeKey() {
            return associationTypeKey;
        }

        public String getAssociationTypeName() {
            return associationTypeName;
        }

        public String getCategoryCode() {
            return categoryCode;
        }

        public int getCategoryKey() {
            return categoryKey;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public String getDateLogged() {
            return dateLogged;
        }

        public String getDateTimeAssociated() {
            return dateTimeAssociated;
        }

        public String getEventDateTime() {
            return eventDateTime;
        }

        public int getEventId() {
            return eventId;
        }

        public String getEventSourceCode() {
            return eventSourceCode;
        }

        public int getEventSourceKey() {
            return eventSourceKey;
        }

        public String getEventSourceName() {
            return eventSourceName;
        }

        public String getEventTypeCode() {
            return eventTypeCode;
        }

        public int getEventTypeKey() {
            return eventTypeKey;
        }

        public String getEventTypeName() {
            return eventTypeName;
        }
    }

    public static class EventExternalReference {
        @SerializedName("typeCode")
        public String typeCode;
        @SerializedName("typeKey")
        public int typeKey;
        @SerializedName("typeName")
        public String typeName;
        @SerializedName("value")
        public String value;

        public String getTypeCode() {
            return typeCode;
        }

        public int getTypeKey() {
            return typeKey;
        }

        public String getTypeName() {
            return typeName;
        }

        public String getValue() {
            return value;
        }
    }

    public static class EventMetaData {
        @SerializedName("typeCode")
        public String typeCode;
        @SerializedName("typeKey")
        public int typeKey;
        @SerializedName("typeName")
        public String typeName;
        @SerializedName("unitOfMeasure")
        public String unitOfMeasure;
        @SerializedName("value")
        public String value;

        public String getTypeCode() {
            return typeCode;
        }

        public int getTypeKey() {
            return typeKey;
        }

        public String getTypeName() {
            return typeName;
        }

        public String getUnitOfMeasure() {
            return unitOfMeasure;
        }

        public String getValue() {
            return value;
        }
    }
}
