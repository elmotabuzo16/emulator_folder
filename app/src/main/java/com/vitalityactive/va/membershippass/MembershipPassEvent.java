package com.vitalityactive.va.membershippass;

import com.vitalityactive.va.membershippass.interactor.MembershipPassInteractor;
import com.vitalityactive.va.membershippass.model.MembershipPassResponse;

/**
 * Created by christian.j.p.capin on 11/22/2017.
 */

public class MembershipPassEvent {

    public Event eventName;

    public MembershipPassResponse responseBody;
    public MembershipPassInteractor.MembershipPassRequestResult membershipPassRequestResult;

    public MembershipPassEvent(Event event){
        this.eventName= event;
    }

    public MembershipPassEvent(MembershipPassInteractor.MembershipPassRequestResult membershipPassRequestResult,Event event) {
        this.membershipPassRequestResult = membershipPassRequestResult;
        this.eventName= event;
    }

    public MembershipPassEvent(MembershipPassResponse membershipPassResponse, MembershipPassInteractor.MembershipPassRequestResult membershipPassRequestResult,Event event) {
        this.membershipPassRequestResult = membershipPassRequestResult;
        this.responseBody = membershipPassResponse;
        this.eventName= event;
    }

    public Event getEventName(){ return this.eventName;}
    public MembershipPassInteractor.MembershipPassRequestResult  getMembershipPassRequestResult() { return membershipPassRequestResult;    }

    public  MembershipPassResponse getResponseBody() {
        return responseBody;
    }

    public enum Event {
        ProfileImageAvailableEvent,
        RequestMembershipPassStatus
    }
}
