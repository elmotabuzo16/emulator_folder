package com.vitalityactive.va.wellnessdevices.pointsmonitor.service;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EventType {
    @SerializedName("eventType")
    public List<EventType> eventType;
    @SerializedName("totalPotentialPoints")
    public int totalPotentialPoints;
    @SerializedName("potentialPointsValue")
    public int potentialPointsValue;
    @SerializedName("typeCode")
    public String typeCode;
    @SerializedName("typeKey")
    public int typeKey;
    @SerializedName("typeName")
    public String typeName;
    @SerializedName("pointsEntryDetails")
    public ArrayList<PointsEntryDetails> pointsEntryDetails;

    public static class PointsEntryDetails implements Parcelable {
        @SerializedName("conditions")
        public List<Conditions> conditions;
        @SerializedName("potentialPoints")
        public int potentialPoints;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeTypedList(this.conditions);
            dest.writeInt(this.potentialPoints);
        }

        public PointsEntryDetails() {
        }

        protected PointsEntryDetails(Parcel in) {
            this.conditions = in.createTypedArrayList(Conditions.CREATOR);
            this.potentialPoints = in.readInt();
        }

        public static final Creator<PointsEntryDetails> CREATOR = new Creator<PointsEntryDetails>() {
            @Override
            public PointsEntryDetails createFromParcel(Parcel source) {
                return new PointsEntryDetails(source);
            }

            @Override
            public PointsEntryDetails[] newArray(int size) {
                return new PointsEntryDetails[size];
            }
        };
    }

    public static class Conditions implements Parcelable{
        @SerializedName("metadataTypeKey")
        public int metadataTypeKey;
        @SerializedName("metadataTypeCode")
        public String metadataTypeCode;
        @SerializedName("metadataTypeName")
        public String metadataTypeName;
        @SerializedName("greaterThan")
        public String greaterThan;
        @SerializedName("greaterThanOrEqualTo")
        public String greaterThanOrEqualTo;
        @SerializedName("lessThan")
        public String lessThan;
        @SerializedName("lessThanOrEqualTo")
        public String lessThanOrEqualTo;
        @SerializedName("unitOfMeasure")
        public String unitOfMeasure;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.metadataTypeKey);
            dest.writeString(this.metadataTypeCode);
            dest.writeString(this.metadataTypeName);
            dest.writeString(this.greaterThan);
            dest.writeString(this.greaterThanOrEqualTo);
            dest.writeString(this.lessThan);
            dest.writeString(this.lessThanOrEqualTo);
            dest.writeString(this.unitOfMeasure);
        }

        public Conditions() {
        }

        protected Conditions(Parcel in) {
            this.metadataTypeKey = in.readInt();
            this.metadataTypeCode = in.readString();
            this.metadataTypeName = in.readString();
            this.greaterThan = in.readString();
            this.greaterThanOrEqualTo = in.readString();
            this.lessThan = in.readString();
            this.lessThanOrEqualTo = in.readString();
            this.unitOfMeasure = in.readString();
        }

        public static final Creator<Conditions> CREATOR = new Creator<Conditions>() {
            @Override
            public Conditions createFromParcel(Parcel source) {
                return new Conditions(source);
            }

            @Override
            public Conditions[] newArray(int size) {
                return new Conditions[size];
            }
        };
    }
}
