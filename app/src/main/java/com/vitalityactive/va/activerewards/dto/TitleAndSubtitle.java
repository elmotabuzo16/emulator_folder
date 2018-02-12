package com.vitalityactive.va.activerewards.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class TitleAndSubtitle implements Parcelable {
    private String title;
    private String subtitle;

    public TitleAndSubtitle(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    private TitleAndSubtitle(Parcel in) {
        title = in.readString();
        subtitle = in.readString();
    }

    public static final Creator<TitleAndSubtitle> CREATOR = new Creator<TitleAndSubtitle>() {
        @Override
        public TitleAndSubtitle createFromParcel(Parcel in) {
            return new TitleAndSubtitle(in);
        }

        @Override
        public TitleAndSubtitle[] newArray(int size) {
            return new TitleAndSubtitle[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(subtitle);
    }
}
