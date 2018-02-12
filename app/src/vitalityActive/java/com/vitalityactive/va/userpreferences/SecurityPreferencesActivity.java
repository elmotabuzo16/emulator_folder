package com.vitalityactive.va.userpreferences;

import android.view.View;
import android.widget.ImageView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.userpreferences.entities.DefaultUserPreferencePresenter;

public class SecurityPreferencesActivity extends BaseSecurityPreferencesActivity{


    @Override
    protected void marketUIUpdate() {
        
    }

    @Override
    protected void showChangePasswordItem(){
        preferenceItemIcon = (ImageView) findViewById(R.id.security_change_password).findViewById(R.id.preference_item_icon);
        preferenceItemIcon.setColorFilter(globalTintColor);

        findViewById(R.id.security_change_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationCoordinator.navigateToChangePassword(SecurityPreferencesActivity.this);
            }
        });

    }

}
