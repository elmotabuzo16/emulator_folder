package com.vitalityactive.va.wellnessdevices;

import android.text.Html;
import android.text.Spanned;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    private static final String COMMENT = "_comment_";

    public static boolean isDeviceLinked(PartnerDto partner){
        return !Constants.UNLINKED.equalsIgnoreCase(partner.getPartnerLinkedStatus());
    }

    public static Map<String, int[]> parseDeviceActivityResponse(String response) {
        Type mapStringObjectType = new TypeToken<Map<String, int[]>>() {}.getType();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(mapStringObjectType, new RandomMapKeysAdapter());
        Gson gson = gsonBuilder.create();

        Map<String, int[]> map = gson.fromJson(response, mapStringObjectType);
        return map;
    }

    static class RandomMapKeysAdapter implements JsonDeserializer<Map<String, int[]>> {
        @Override
        public Map<String, int[]> deserialize(JsonElement json, Type unused, JsonDeserializationContext context)
                throws JsonParseException {
            if (!json.isJsonObject()) {
                throw new JsonParseException("Response is not in JSON format");
            }

            Map<String, int[]> result = new HashMap<>();
            JsonObject jsonObject = json.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String key = entry.getKey();
                if(!COMMENT.equals(key)) {
                    JsonElement element = entry.getValue();
                    if (element.isJsonPrimitive()) {
                        // Skip for now
                    } else if (element.isJsonObject()) {
                        // Skip for now
                    } else if (element.isJsonArray()) {
                        JsonArray arr = element.getAsJsonArray();
                        int[] res = new int[arr.size()];
                        for (int i = 0; i < arr.size(); i++) {
                            res[i] = arr.get(i).getAsInt();
                        }
                        result.put(key, res);
                    } else {
                        throw new JsonParseException("Unexpected value format");
                    }
                }
            }
            return result;
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

}
