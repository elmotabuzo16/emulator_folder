package com.vitalityactive.va.membershippass;


import android.support.v4.app.Fragment;
import android.view.View;

import com.vitalityactive.va.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MembershipPassFragment extends BaseMembershipPassFragment {

    @Override
    protected void marketUIUpdate(String membershipNumber, String partyId) {
        digitalPassCardview.setVisibility(View.GONE);
        membershipNumbeContainer.setVisibility(View.VISIBLE);
        customerNumberContainer.setVisibility(View.GONE);
        vitalityConcat = partyId;
        membershipNumberLabel.setText(getResources().getString(R.string.settings_membership_party_id_title_1105));
        /*memberpassHeader.setImageDrawable(getResources().getDrawable(R.drawable.carrier_branding_area_uke));
        memberpassHeader.setColorFilter(ContextCompat.getColor(getActivity(), android.R.color.transparent), PorterDuff.Mode.OVERLAY);*/
        memberpassHeader.setVisibility(View.GONE);
        infoButtonView2.setVisibility(View.GONE);
        infoButtonView1.setVisibility(View.GONE);
        vitalityNumberContainer.setVisibility(View.GONE);
    }

    @Override
    protected boolean isDigitalPassShow() {
        return false;
    }
}
