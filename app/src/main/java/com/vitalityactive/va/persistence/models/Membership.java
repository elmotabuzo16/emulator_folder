package com.vitalityactive.va.persistence.models;

import android.util.Log;

import com.vitalityactive.va.home.service.HomeScreenCardStatusResponse;
import com.vitalityactive.va.membershippass.model.MembershipPassResponse;
import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.vitalitystatus.repository.VitalityStatusRepository;

import io.realm.RealmObject;

/**
 * Created by christian.j.p.capin on 11/15/2017.
 */

public class Membership extends RealmObject implements Model {
    private long partyId;
    private String vitalityMembershipId, membershipNumber, customerNumber, vitalityStatus, membershipStartDate, membershipStatus;
    //private LoginServiceResponse.Person person;
   // private LoginServiceResponse loginServiceResponse;
   // private LoginServiceResponse.PartyDetails partyDetails;

    public static MembershipPassResponse model;
    public static HomeScreenCardStatusResponse homeScreenCardStatusResponse;

    public Membership(){}

   // public Membership(MembershipPassResponse model,LoginServiceResponse.PartyDetails partyDetails,HomeScreenCardStatusResponse homeScreenCardStatusResponse){
    public Membership(MembershipPassResponse model){
        this.vitalityMembershipId = model.sections.get(0).id;
        this.vitalityStatus= model.sections.get(0).stateCategories.get(0).stateList.get(0).statusTypeName; //homeScreenCardStatusResponse.vitalityStatus.overallVitalityStatusName;
        this.membershipNumber= "";//String.valueOf(partyDetails.partyId);
        this.membershipStartDate = model.sections.get(0).currentVitalityMembershipPeriod.get(0).effectiveFrom;
        this.membershipStatus= "";//String.valueOf(homeScreenCardStatusResponse.sections.get(0).cards.get(0).statusTypeKey);
        this.customerNumber= ""; //partyDetails.telephones.get(0).contactNumber;

    }

 /*    public Membership(LoginServiceResponse.PartyDetails partyDetails, Long vitalityMembershipId, MembershipPassResponse.CurrentVitalityMembershipPeriod currentVitalityMembershipPeriod) {
        //public Membership(LoginServiceResponse loginServiceResponse, Long vitalityMembershipId, String username) {
       // this.loginServiceResponse= loginServiceResponse;
        //this.partyDetails = loginServiceResponse.partyDetails;

        this.partyId = partyDetails.partyId;
        // person = partyDetails.person;
        this.vitalityMembershipId = vitalityMembershipId.toString();
        this.mobile= "987654321";
        this.membershipStartDate= currentVitalityMembershipPeriod.effectiveFrom;
    }*/

    public MembershipPassResponse getMembershipPassResponse(){return this.model;}

    public String getVitalityMembershipId(){ return vitalityMembershipId;}
    public String getVitalityStatus(){ return  this.vitalityStatus;}
    public String getMembershipNumber(){return membershipNumber;}
    public String getMembershipStartDate(){ return membershipStartDate;}
    public String getMembershipStatus(){return  membershipStatus;}
    public String getCustomerNumber(){return customerNumber;}

//   public void setHomeScreenCardStatusResponse(HomeScreenCardStatusResponse homeScreenCardStatusResponse){ this.homeScreenCardStatusResponse =homeScreenCardStatusResponse;    }
}
