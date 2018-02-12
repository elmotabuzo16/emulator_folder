package com.vitalityactive.va.activerewards.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.vitalityactive.va.networking.model.goalandprogress.Reason;
import com.vitalityactive.va.persistence.models.goalandprogress.GoalProgressReasonRealm;

public class GoalProgressReasonDto implements Parcelable {
    private String categoryCode;
    private int categoryKey;
    private int reasonKey;
    private String reasonName;
    private String reasonCode;
    private String categoryName;

    public GoalProgressReasonDto(Reason reason) {
        this.categoryCode = reason.categoryCode;
        this.categoryKey = reason.categoryKey;
        this.reasonKey = reason.reasonKey;
        this.reasonName = reason.reasonName;
        this.reasonCode = reason.reasonCode;
        this.categoryName = reason.categoryName;
    }

    public GoalProgressReasonDto(GoalProgressReasonRealm reason) {
        this.categoryCode = reason.getCategoryCode();
        this.categoryKey = reason.getCategoryKey();
        this.reasonKey = reason.getReasonKey();
        this.reasonName = reason.getReasonName();
        this.reasonCode = reason.getReasonCode();
        this.categoryName = reason.getCategoryName();
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public int getCategoryKey() {
        return categoryKey;
    }

    public int getReasonKey() {
        return reasonKey;
    }

    public String getReasonName() {
        return reasonName;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryCode);
        dest.writeInt(this.categoryKey);
        dest.writeInt(this.reasonKey);
        dest.writeString(this.reasonName);
        dest.writeString(this.reasonCode);
        dest.writeString(this.categoryName);
    }

    protected GoalProgressReasonDto(Parcel in) {
        this.categoryCode = in.readString();
        this.categoryKey = in.readInt();
        this.reasonKey = in.readInt();
        this.reasonName = in.readString();
        this.reasonCode = in.readString();
        this.categoryName = in.readString();
    }

    public static final Creator<GoalProgressReasonDto> CREATOR = new Creator<GoalProgressReasonDto>() {
        @Override
        public GoalProgressReasonDto createFromParcel(Parcel source) {
            return new GoalProgressReasonDto(source);
        }

        @Override
        public GoalProgressReasonDto[] newArray(int size) {
            return new GoalProgressReasonDto[size];
        }
    };
}
