package com.vitalityactive.va.dto;

import com.vitalityactive.va.persistence.models.ContentHelp;

/**
 * Created by chelsea.b.pioquinto on 2/1/2018.
 */

public class ContentHelpDTO {

    public String question;
    public String answer;

    public ContentHelpDTO(){

    }

    public ContentHelpDTO(ContentHelp contentHelp) {
       question = contentHelp.getQuestion();
       answer = contentHelp.getAnswer();
    }

    public String getQuestion() {
        return question.replaceAll("</?(span|p|div){1}.*?/?>", "");
    }

    public String getAnswer() {
        return answer;
    }
}
