package com.vitalityactive.va.eventsfeed.data.net.request;

/**
 * Created by jayellos on 11/17/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventType {

    @SerializedName("typeKey")
    @Expose
    private Integer typeKey;

    public Integer getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(Integer typeKey) {
        this.typeKey = typeKey;
    }

}
