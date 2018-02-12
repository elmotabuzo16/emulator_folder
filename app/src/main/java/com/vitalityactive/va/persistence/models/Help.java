package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.dto.HelpDTO;
import com.vitalityactive.va.help.model.HelpResponse;
import com.vitalityactive.va.persistence.Model;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;

/**
 * Created by sean.d.penacerrada on 2/7/2018.
 */

public class Help extends RealmObject implements Model{
    private String answer, question;

    public Help(){}

    public Help(HelpResponse.FAQ model){
        answer = model.answer;
        question = model.question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
