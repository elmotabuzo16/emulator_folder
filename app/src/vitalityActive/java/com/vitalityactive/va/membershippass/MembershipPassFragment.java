package com.vitalityactive.va.membershippass;

/**
 * Created by peter.ian.t.betos on 15/12/2017.
 */

import android.support.v4.app.Fragment;
import android.view.View;


/**
 * A simple {@link Fragment} subclass.
 */
public class MembershipPassFragment extends BaseMembershipPassFragment {

    @Override
    protected void marketUIUpdate(String membershipNumber, String partyId) {
        digitalPassCardview.setVisibility(View.VISIBLE);
        membershipNumbeContainer.setVisibility(View.VISIBLE);
        customerNumberContainer.setVisibility(View.VISIBLE);
        vitalityConcat = membershipNumber;
    }
}
