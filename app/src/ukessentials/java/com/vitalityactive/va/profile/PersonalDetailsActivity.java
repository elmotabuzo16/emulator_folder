package com.vitalityactive.va.profile;

import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.R;

public class PersonalDetailsActivity extends BasePersonalDetailsActivity {

    @Override
    protected void marketUIUpdate(){

    }

    protected void replaceContentWithChangeEntityNumber() {
        if(BuildConfig.FLAVOR.contains("ukessentials")){
            replaceContent(new ChangeEntityNumberFragment());
            setActionBarTitle(getResources().getString( R.string.profile_landing_entity_number_title_9999));
            updateActionBarIcon(R.drawable.ic_close_white_24dp);
        }

    }

}
