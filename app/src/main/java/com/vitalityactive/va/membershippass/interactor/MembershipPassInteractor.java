package com.vitalityactive.va.membershippass.interactor;

import com.vitalityactive.va.dto.MembershipPassDTO;
import com.vitalityactive.va.membershippass.model.MembershipPassResponse;

import java.util.List;

/**
 * Created by christian.j.p.capin on 11/17/2017.
 */

public interface MembershipPassInteractor {

    boolean isRequestInProgress();
    boolean initialize();

    enum MembershipPassRequestResult {
        NONE,
        CONNECTION_ERROR,
        GENERIC_ERROR,
        SUCCESSFUL
    }
}
