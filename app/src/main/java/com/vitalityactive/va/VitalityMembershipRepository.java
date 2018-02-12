package com.vitalityactive.va;

import com.vitalityactive.va.dto.VitalityMembershipDto;

import java.util.List;

/**
 * Created by kerry.e.lawagan on 11/26/2017.
 */

public interface VitalityMembershipRepository {
    List<VitalityMembershipDto> getVitalityMembership();
}
