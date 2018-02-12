package com.vitalityactive.va.dto;

import android.util.Log;

import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.Membership;
import com.vitalityactive.va.persistence.models.User;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.vitalitystatus.VitalityStatusDTO;

/**
 * Created by christian.j.p.capin on 11/14/2017.
 */

public class MembershipPassDTO{
    private String membershipNumber, vitalityNumber,customerNumber,vitalityStatus, membershipStartDate,membershipStatus;
    private HomeCardDTO homeCardDTO;


    public MembershipPassDTO(){}
    public MembershipPassDTO(Membership membership,String vitalityNumber,String membershipNumber,String customerNumber,String vitalityStatus,String membershipStartDate,String membershipStatus){

        this.vitalityNumber = vitalityNumber;
        this.membershipNumber = membershipNumber;
        this.customerNumber = customerNumber;
        this.vitalityStatus = vitalityStatus;
        this.membershipStartDate = membershipStartDate;
        this.membershipStatus= membershipStatus;
    }

    public String getMembershipNumber() {
        return this.membershipNumber;
    }
    public String getVitalityNumber(){ return this.vitalityNumber;}
    public String getVitalityStatus(){return this.vitalityStatus;}
    public String getCustomerNumber(){return this.customerNumber;}
    public String getMembershipStartDate(){return this.membershipStartDate;}
    public String getMembershipStatus(){ return this.membershipStatus;}

    public static class Mapper implements DataStore.ModelMapper<Membership,MembershipPassDTO> {
        @Override
        public MembershipPassDTO mapModel(Membership model) {
            Log.e("SULOD Membership Mapper",""+ model.toString());

            if (model == null) {
                return new MembershipPassDTO(model,"", "", "", "", "","");
            }
            return new MembershipPassDTO(model,model.getVitalityMembershipId(), model.getMembershipNumber(), model.getCustomerNumber(),
                      model.getVitalityStatus(), model.getMembershipStartDate(),model.getMembershipStatus());
        }
    }
}
