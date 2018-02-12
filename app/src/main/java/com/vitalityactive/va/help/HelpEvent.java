package com.vitalityactive.va.help;

import com.vitalityactive.va.help.interactor.HelpInteractor;
import com.vitalityactive.va.help.model.HelpResponse;

/**
 * Created by sean.d.penacerrada on 2/7/2018.
 */

public class HelpEvent {

    public Event event;

    public HelpResponse response;
    public HelpInteractor.HelpRequestResult helpRequestResult;

    public HelpEvent(Event event) {
        this.event =  event;
    }

    public HelpEvent(HelpInteractor.HelpRequestResult helpRequestResult,Event event) {
        this.helpRequestResult = helpRequestResult;
        this.event = event;
    }

    public HelpEvent(HelpResponse helpResponse, HelpInteractor.HelpRequestResult helpRequestResult,Event event) {
        this.helpRequestResult = helpRequestResult;
        this.response = helpResponse;
        this.event = event;
    }

    public Event getEventName(){ return this.event;}

    public HelpInteractor.HelpRequestResult getHelpRequestResult() { return helpRequestResult;    }

    public  HelpResponse getResponse() {
        return response;
    }

    public enum Event {
        RequestMembershipPassStatus
    }
}
