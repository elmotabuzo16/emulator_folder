package com.vitalityactive.va.search;

import com.google.gson.annotations.SerializedName;
import com.vitalityactive.va.help.model.HelpResponse;
import com.vitalityactive.va.partnerjourney.service.models.PartnerDetailResponse;

import java.util.List;

/**
 * Created by chelsea.b.pioquinto on 1/29/2018.
 */

public class ContentHelpResponse {
    @SerializedName("FAQ")
    public List<FAQ> FAQ;

    public static class FAQ{
        @SerializedName("answer")
        public String answer;
        @SerializedName("question")
        public String question;
    }
}
