package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.snv.confirmandsubmit.model.ConfirmAndSubmitItemUI;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ConfirmAndSubmitItem extends RealmObject implements Model {

    @PrimaryKey
    private int typeKey;
    private String fieldTitle;
    private String dateTested;
    private boolean isEnabled;
    private int type;
    private long dateTestedinLong;

    public ConfirmAndSubmitItem() {
    }

    public ConfirmAndSubmitItem(ConfirmAndSubmitItemUI item, int type, int typeKey, long dateTestedinLong) {
        this.fieldTitle = item.getFieldTitle();
        this.dateTested = item.getDateTested();
        this.isEnabled = item.isEnabled();
        this.type = type;
        this.typeKey = typeKey;
        this.dateTestedinLong = dateTestedinLong;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public String getDateTested() {
        return dateTested;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public int getType() {
        return type;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public long getDateTestedinLong() {
        return dateTestedinLong;
    }
}
