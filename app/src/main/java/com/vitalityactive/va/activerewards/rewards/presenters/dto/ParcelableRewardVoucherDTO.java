package com.vitalityactive.va.activerewards.rewards.presenters.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableRewardVoucherDTO implements Parcelable {
    public final String voucherNumber;
    public final String name;
    public final String rewardValue;
    public final String rewardType;
    public final boolean showCodePending;
    public final String expiryDate;

    public ParcelableRewardVoucherDTO(String voucherNumber, String name, String rewardValue, String rewardType, String expiryDate, boolean showCodePending) {
        this.voucherNumber = voucherNumber;
        this.name = name;
        this.rewardValue = rewardValue;
        this.rewardType = rewardType;
        this.expiryDate = expiryDate;
        this.showCodePending =  showCodePending;
    }

    private ParcelableRewardVoucherDTO(Parcel in) {
        this(in.readString(), in.readString(), in.readString(), in.readString(), in.readString(), in.readInt() != 0);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(voucherNumber);
        dest.writeString(name);
        dest.writeString(rewardValue);
        dest.writeString(rewardType);
        dest.writeString(expiryDate);
        dest.writeInt(showCodePending ? 1 : 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParcelableRewardVoucherDTO> CREATOR = new Creator<ParcelableRewardVoucherDTO>() {
        @Override
        public ParcelableRewardVoucherDTO createFromParcel(Parcel in) {
            return new ParcelableRewardVoucherDTO(in);
        }

        @Override
        public ParcelableRewardVoucherDTO[] newArray(int size) {
            return new ParcelableRewardVoucherDTO[size];
        }
    };
}
