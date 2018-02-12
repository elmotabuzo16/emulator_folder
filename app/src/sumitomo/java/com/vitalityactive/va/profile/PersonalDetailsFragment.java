package com.vitalityactive.va.profile;

import android.view.View;

public class PersonalDetailsFragment extends BasePersonalDetailsFragment {
    @Override
    protected void marketUIUpdate() {

            personalMobileContainer.setVisibility(View.GONE);
            profileImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editProfileImage();
                }
            });

            profileInitialsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editProfileImage();
                }
            });

            profileEditView.setClickable(true);
            profileInitialsView.setClickable(true);

    }
}
