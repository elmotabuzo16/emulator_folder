package com.vitalityactive.va.activerewards.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.vitalityactive.va.persistence.models.goalandprogress.EntryMetadata;

public class EntryMetadataDto implements Parcelable {

    private String typeCode;
    private int typeKey;
    private String typeName;
    private String unitOfMeasure;
    private String value;

    public EntryMetadataDto(EntryMetadata src) {
        this.typeCode = src.getTypeCode();
        this.typeKey = src.getTypeKey();
        this.typeName = src.getTypeName();
        this.unitOfMeasure = src.getUnitOfMeasure();
        this.value = src.getValue();

    }

    public EntryMetadataDto(com.vitalityactive.va.networking.model.goalandprogress.EntryMetadata src) {
        this.typeCode = src.typeCode;
        this.typeKey = src.typeKey;
        this.typeName = src.typeName;
        this.unitOfMeasure = src.unitOfMeasure;
        this.value = src.value;
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

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.typeCode);
        dest.writeInt(this.typeKey);
        dest.writeString(this.typeName);
        dest.writeString(this.unitOfMeasure);
        dest.writeString(this.value);
    }

    protected EntryMetadataDto(Parcel in) {
        this.typeCode = in.readString();
        this.typeKey = in.readInt();
        this.typeName = in.readString();
        this.unitOfMeasure = in.readString();
        this.value = in.readString();
    }

    public static final Parcelable.Creator<EntryMetadataDto> CREATOR = new Parcelable.Creator<EntryMetadataDto>() {
        @Override
        public EntryMetadataDto createFromParcel(Parcel source) {
            return new EntryMetadataDto(source);
        }

        @Override
        public EntryMetadataDto[] newArray(int size) {
            return new EntryMetadataDto[size];
        }
    };
}
