package com.vitalityactive.va.register;

import android.os.Parcel;
import android.os.Parcelable;

public class ActivationValues implements Parcelable {
    public final String dateOfBirth;
    public final String authenticationCode;
    public final String entityNumber;

    public ActivationValues(String dateOfBirth, String authenticationCode, String entityNumber) {
        this.dateOfBirth = dateOfBirth;
        this.authenticationCode = authenticationCode;
        this.entityNumber = entityNumber;
    }

    protected ActivationValues(Parcel in) {
        dateOfBirth = in.readString();
        authenticationCode = in.readString();
        entityNumber = in.readString();
    }

    public static final Creator<ActivationValues> CREATOR = new Creator<ActivationValues>() {
        @Override
        public ActivationValues createFromParcel(Parcel in) {
            return new ActivationValues(in);
        }

        @Override
        public ActivationValues[] newArray(int size) {
            return new ActivationValues[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(dateOfBirth);
        parcel.writeString(authenticationCode);
        parcel.writeString(entityNumber);
    }
}
