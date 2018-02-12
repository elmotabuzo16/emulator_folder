package com.vitalityactive.va.dto;

import android.util.Log;

import com.vitalityactive.va.help.model.HelpResponse;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.Help;

/**
 * Created by sean.d.penacerrada on 2/7/2018.
 */

public class HelpDTO {
    private String answer, question;

    public HelpDTO(){}

    public HelpDTO(Help help){
        answer = help.getAnswer();
        question = help.getQuestion();
    }

    public String getAnswer() {
        return answer;
    }
    public String getQuestion() {
        return question.replaceAll("</?(span|p|div){1}.*?/?>", "");
    }
}
