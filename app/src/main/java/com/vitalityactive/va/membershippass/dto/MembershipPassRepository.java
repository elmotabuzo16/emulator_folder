package com.vitalityactive.va.membershippass.dto;

import com.vitalityactive.va.dto.MembershipPassDTO;
import com.vitalityactive.va.membershippass.model.MembershipPassResponse;

/**
 * Created by christian.j.p.capin on 11/21/2017.
 */

public interface MembershipPassRepository{
    boolean persistMembershipPassResponse(MembershipPassResponse response);
    MembershipPassDTO getMembershipPass();
}
