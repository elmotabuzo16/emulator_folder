package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.help.model.HelpResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.search.ContentHelpResponse;

import io.realm.RealmObject;

/**
 * Created by chelsea.b.pioquinto on 2/8/2018.
 */

public class ContentHelp extends RealmObject implements Model {

    public String question;
    public String answer;

    public ContentHelp() {

    }

    public ContentHelp(ContentHelpResponse.FAQ faq) {
        question = faq.question;
        answer = faq.answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
