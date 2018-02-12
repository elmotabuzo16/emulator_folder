package com.vitalityactive.va.activerewards.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ActivityItem implements Parcelable {
    private String title;
    private String description;
    private String date;
    private String device;
    private List<TitleAndSubtitle> metadata;

    public ActivityItem(String title,
                        String description,
                        String date,
                        String device){
        this(title, description, date, device, null);
    }

    public ActivityItem(String title,
                        String description,
                        String date,
                        String device,
                        List<TitleAndSubtitle> metadata){
        this.title = title;
        this.description = description;
        this.date = date;
        this.device = device;
        this.metadata = ((metadata == null) ? new ArrayList<TitleAndSubtitle>() : metadata);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.date);
        dest.writeString(this.device);
        dest.writeTypedList(this.metadata);
    }

    protected ActivityItem(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.date = in.readString();
        this.device = in.readString();
        this.metadata = in.createTypedArrayList(TitleAndSubtitle.CREATOR);
    }

    public static final Creator<ActivityItem> CREATOR = new Creator<ActivityItem>() {
        @Override
        public ActivityItem createFromParcel(Parcel source) {
            return new ActivityItem(source);
        }

        @Override
        public ActivityItem[] newArray(int size) {
            return new ActivityItem[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getDevice() {
        return device;
    }

    public List<TitleAndSubtitle> getMetadata() {
        return metadata;
    }
}
