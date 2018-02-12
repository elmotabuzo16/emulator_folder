package com.vitalityactive.va.profile;

import android.support.v4.content.ContextCompat;
import android.view.View;

import com.vitalityactive.va.R;

public class PersonalDetailsFragment extends BasePersonalDetailsFragment {
    @Override
    protected void marketUIUpdate() {

        personalEmailContainer.setVisibility(View.GONE);
        personalEntityIcon = parentView.findViewById(R.id.personal_entity_number_icon);
        personalEntityLabel = parentView.findViewById(R.id.personal_entity_number_label);
        personalEntityView = parentView.findViewById(R.id.personal_entity_number);

        if(currentEntityNumber.length() > 0){
            personalEntityView.setText(currentEntityNumber);
            personalEntityIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.card_active));
        } else {
            personalEntityView.setText(getResources().getString(R.string.profile_footer_entity_number_title_9999));
            personalEntityIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.card_inactive));
        }


        personalEntityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PersonalDetailsActivity) getActivity()).replaceContentWithChangeEntityNumber();
            }
        });
    }


}
