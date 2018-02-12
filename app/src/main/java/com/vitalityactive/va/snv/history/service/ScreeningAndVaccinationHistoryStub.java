package com.vitalityactive.va.snv.history.service;

import android.content.Context;

import com.google.gson.Gson;
import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.dependencyinjection.NetworkModule;
import com.vitalityactive.va.home.service.EventByPartyResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

/**
 * Created by dharel.h.rosell on 11/30/2017.
 */

public class ScreeningAndVaccinationHistoryStub {

    private Context context;

    public ScreeningAndVaccinationHistoryStub(Context contextParam) {
        this.context = contextParam;
    }

    public EventByPartyResponse getResponse() throws IOException {
        return getGson().fromJson(readFile(), EventByPartyResponse.class);
    }

    private Gson getGson() {
        return new NetworkModule(BuildConfig.TEST_BASE_URL, UUID.randomUUID().toString()).provideGson();
    }

    public InputStream getResourceAsStream(String path) {
        return getClass().getClassLoader().getResourceAsStream(path);
    }

    public String readFile() {
//        final StringBuilder sb = new StringBuilder();
//        String strLine;
//        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
//            while ((strLine = reader.readLine()) != null) {
//                sb.append(strLine);
//            }
//        } catch (final IOException ignore) {
//            //ignore
//        }
//        return sb.toString();
        String json = null;
        try {
            InputStream is = context.getAssets().open("sv_view_history_actual_response.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
