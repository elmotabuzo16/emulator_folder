package com.vitalityactive.va.profile;

import android.view.View;

import com.vitalityactive.va.R;

public class ProfileFragment extends BaseProfileFragment {

    @Override
    protected void marketUiUpdate(View parentView){
        parentView.findViewById(R.id.events_feed).setVisibility(View.GONE);
    }

}
