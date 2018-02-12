package com.vitalityactive.va.vitalitystatus.repository;

import com.vitalityactive.va.home.events.ProductFeaturePointsResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.utilities.InvalidModelLogger;

import io.realm.RealmObject;

public class EventTypeModel extends RealmObject implements Model {
    private String code;
    private Integer key;
    private String name;

    public EventTypeModel() {}

    public static EventTypeModel create(ProductFeaturePointsResponse.EventType response) {
        if (response.key == null) {
            InvalidModelLogger.warn(response, 0, "required fields are null");
            return null;
        }

        EventTypeModel instance = new EventTypeModel();

        instance.code = response.code;
        instance.key = response.key;
        instance.name = response.name;

        return instance;
    }
}
