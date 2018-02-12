package com.vitalityactive.va.membershippass;

/**
 * Created by peter.ian.t.betos on 15/12/2017.
 */

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.vitalityactive.va.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MembershipPassFragment extends BaseMembershipPassFragment {

    @Override
    protected void marketUIUpdate(String membershipNumber, String partyId) {
        digitalPassCardview.setVisibility(View.VISIBLE);
        membershipNumbeContainer.setVisibility(View.GONE);
        customerNumberContainer.setVisibility(View.GONE);
        profileImageContainer.setEnabled(false);
        vitalityConcat = partyId;
        membershipPassHeader1.setText(getResources().getString(R.string.Settings_membership_pass_911));
    }

    @Override
    protected void showHideVitalityNumber() {
        if (TextUtils.isEmpty(vitalityConcat) || TextUtils.isEmpty(vitalityConcat.trim())) {
            vitalityNumberContainer.setVisibility(View.GONE);
        } else {
            vitalityNumberContainer.setVisibility(View.VISIBLE);
        }
    }
}
