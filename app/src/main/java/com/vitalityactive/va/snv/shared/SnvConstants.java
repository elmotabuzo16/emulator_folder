package com.vitalityactive.va.snv.shared;

/**
 * Created by kerry.e.lawagan on 11/28/2017.
 */

public class SnvConstants {
    public static final String HEALTH_ACTION_SCREENINGS = "HEALTH_ACTION_SCREENINGS";
    public static final String HEALTH_ACTION_VACCINATIONS = "HEALTH_ACTION_VACCINATIONS";

    public static final String SNV_CONNECTION_ERROR_ALERT = "SNV_CONNECTION_ERROR_ALERT";
    public static final String SNV_GENERIC_ERROR_ALERT = "SNV_GENERIC_ERROR_ALERT";

    public enum SnvApiResult {
        CONNECTION_ERROR,
        GENERIC_ERROR,
        SUCCESSFUL
    }
}
