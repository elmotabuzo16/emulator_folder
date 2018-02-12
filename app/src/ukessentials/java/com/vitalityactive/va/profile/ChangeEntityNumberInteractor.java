package com.vitalityactive.va.profile;

import android.support.annotation.NonNull;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.WebServiceResponseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangeEntityNumberInteractor {

    public final EventDispatcher eventDispatcher;
    public final ChangeEntityNumberClient changeEntityNumberClient;
    public final PartyInformationRepository partyInfoRepo;
    public final DeviceSpecificPreferences deviceSpecificPreferences;

    public ChangeEntityNumberInteractor(EventDispatcher eventDispatcher, ChangeEntityNumberClient changeEntityNumberClient, PartyInformationRepository partyInfoRepo, DeviceSpecificPreferences deviceSpecificPreferences) {
        this.eventDispatcher = eventDispatcher;
        this.changeEntityNumberClient = changeEntityNumberClient;
        this.partyInfoRepo = partyInfoRepo;
        this.deviceSpecificPreferences = deviceSpecificPreferences;
    }

    protected void changeEntityNumber(@NonNull String partyId, @NonNull String dateOfBirth, @NonNull String entityNumber){
        changeEntityNumberClient.changeNewNumber(partyId, dateOfBirth, entityNumber, new WebServiceInstance(eventDispatcher, partyInfoRepo, deviceSpecificPreferences, entityNumber));
    }

    protected void addEntityNumber(@NonNull String partyId, @NonNull String dateOfBirth, @NonNull String entityNumber){
        changeEntityNumberClient.addNewNumber(partyId, dateOfBirth, entityNumber, new WebServiceInstance(eventDispatcher, partyInfoRepo, deviceSpecificPreferences, entityNumber));
    }

    public static class WebServiceInstance implements WebServiceResponseParser<Void>{

        private static final int ENTITY_BAD_REQUEST = 400;
        private static final int ENTITY_NUMBER_INVALID = 4;
        private static final int ENTITY_BIRTHDATE_INVALID = 15;

        private String newEntityNumber;
        public final EventDispatcher eventDispatcher;
        public final PartyInformationRepository partyInfoRepo;
        public final DeviceSpecificPreferences deviceSpecificPreferences;

        private WebServiceInstance(EventDispatcher eventDispatcher, PartyInformationRepository partyInfoRepo, DeviceSpecificPreferences deviceSpecificPreferences, String newEntityNumber) {
            this.eventDispatcher = eventDispatcher;
            this.partyInfoRepo = partyInfoRepo;
            this.deviceSpecificPreferences = deviceSpecificPreferences;
            this.newEntityNumber = newEntityNumber;
        }

        @Override
        public void parseResponse(Void response) {
            partyInfoRepo.setNewEntityNumber(partyInfoRepo.getEntityNumber(), newEntityNumber);
            deviceSpecificPreferences.setRememberedEntity(newEntityNumber);
            eventDispatcher.dispatchEvent(new ChangeEntityNumberEvent(ChangeEntityNumberEvent.EventType.CHANGE_ENTITY_SUCCESS));
        }

        @Override
        public void parseErrorResponse(String errorBody, int code) {
            int errorCode = 0;
            try {
                JSONObject jsonError = new JSONObject(errorBody);
                JSONArray jsonArray = jsonError.getJSONArray("errors");
                JSONObject errorObj = jsonArray.getJSONObject(0);
                errorCode = errorObj.optInt("code", 0);
            } catch (JSONException e) {
                e.printStackTrace();
                eventDispatcher.dispatchEvent(new ChangeEntityNumberEvent(ChangeEntityNumberEvent.EventType.CHANGE_ENTITY_ERROR));
            }
            if(code == ENTITY_BAD_REQUEST && errorCode == ENTITY_NUMBER_INVALID){
                eventDispatcher.dispatchEvent(new ChangeEntityNumberEvent(ChangeEntityNumberEvent.EventType.CHANGE_ENTITY_FAILED));
            } else if(code == ENTITY_BAD_REQUEST && errorCode == ENTITY_BIRTHDATE_INVALID){
                eventDispatcher.dispatchEvent(new ChangeEntityNumberEvent(ChangeEntityNumberEvent.EventType.CHANGE_ENTITY_ERROR));
            } else {
                eventDispatcher.dispatchEvent(new ChangeEntityNumberEvent(ChangeEntityNumberEvent.EventType.CHANGE_ENTITY_ERROR));
            }
        }

        @Override
        public void handleGenericError(Exception exception) {
            eventDispatcher.dispatchEvent(new ChangeEntityNumberEvent(ChangeEntityNumberEvent.EventType.CHANGE_ENTITY_ERROR));
        }

        @Override
        public void handleConnectionError() {
            eventDispatcher.dispatchEvent(new ChangeEntityNumberEvent(ChangeEntityNumberEvent.EventType.CHANGE_ENTITY_CONNECTION_FAILED));
        }
    }
}
