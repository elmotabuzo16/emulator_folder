package com.vitalityactive.va.myhealth.learnmore;

import android.os.Parcel;
import android.os.Parcelable;


public class DisclaimerItem implements Parcelable {
    public static final Creator<DisclaimerItem> CREATOR = new Creator<DisclaimerItem>() {
        @Override
        public DisclaimerItem createFromParcel(Parcel in) {
            return new DisclaimerItem(in);
        }

        @Override
        public DisclaimerItem[] newArray(int size) {
            return new DisclaimerItem[size];
        }
    };
    private String title;
    private int iconResourceId;

    public DisclaimerItem(String title, int iconResourceId) {
        this.title = title;
        this.iconResourceId = iconResourceId;
    }

    protected DisclaimerItem(Parcel in) {
        title = in.readString();
        iconResourceId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(title);
        dest.writeInt(iconResourceId);
    }

    public String getTitle() {
        return title;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }
}
