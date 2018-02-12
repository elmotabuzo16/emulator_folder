package com.vitalityactive.va.snv.learnmore;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by stephen.rey.w.avila on 12/5/2017.
 */

public class LearnMoreScreeningItem implements Parcelable{
    public static final Creator<LearnMoreScreeningItem> CREATOR = new Creator<LearnMoreScreeningItem>() {
        @Override
        public LearnMoreScreeningItem createFromParcel(Parcel in) {
            return new LearnMoreScreeningItem(in);
        }

        @Override
        public LearnMoreScreeningItem[] newArray(int size) {
            return new LearnMoreScreeningItem[size];
        }
    };
    private String title1;
    private int iconResourceId1;

    private String title2;
    private int iconResourceId2;

    public LearnMoreScreeningItem(String title1, int iconResourceId1,String title2, int iconResourceId2) {
        this.title1 = title1;
        this.iconResourceId1 = iconResourceId1;

        this.title2 = title2;
        this.iconResourceId2 = iconResourceId2;
    }

    protected LearnMoreScreeningItem(Parcel in) {
        title1 = in.readString();
        iconResourceId1 = in.readInt();
        title2 = in.readString();
        iconResourceId2 = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(title1);
        dest.writeInt(iconResourceId1);

        dest.writeString(title2);
        dest.writeInt(iconResourceId2);
    }

    public String getTitle1() {
        return title1;
    }
    public String getTitle2() {
        return title2;
    }

    public int getIconResourceId1() {
        return iconResourceId1;
    }
    public int getIconResourceId2() {return iconResourceId2;}
}
