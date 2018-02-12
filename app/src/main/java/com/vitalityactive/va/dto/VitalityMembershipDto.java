package com.vitalityactive.va.dto;

import com.vitalityactive.va.persistence.models.MembershipProduct;
import com.vitalityactive.va.persistence.models.VitalityMembership;

import java.util.ArrayList;
import java.util.List;

public class VitalityMembershipDto {
    public Long id;
    public List<MembershipProductDto> membershipProducts;
    public CurrentVitalityMembershipPeriodDTO currentVitalityMembershipPeriod;

    public VitalityMembershipDto(VitalityMembership membership) {
        id = membership.id;
        membershipProducts = new ArrayList<>();
        for (MembershipProduct product: membership.membershipProducts) {
            membershipProducts.add(new MembershipProductDto(product));
        }
        currentVitalityMembershipPeriod = new CurrentVitalityMembershipPeriodDTO(membership.currentVitalityMembershipPeriod);
    }
}
