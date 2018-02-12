package com.vitalityactive.va.help.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by christian.j.p.capin on 11/30/2017.
 */

public class HelpResponse {

    @SerializedName("FAQ")
    public List<FAQ> sections;

    public static class FAQ {
        @SerializedName("answer")
        public String answer;
        @SerializedName("question")
        public String question;
    }
}
