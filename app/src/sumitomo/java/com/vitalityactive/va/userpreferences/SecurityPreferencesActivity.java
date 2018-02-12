package com.vitalityactive.va.userpreferences;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.userpreferences.entities.DefaultUserPreferencePresenter;

public class SecurityPreferencesActivity extends BaseSecurityPreferencesActivity{

    protected TextView changePasswordHeader;

    @Override
    protected void marketUIUpdate() {
        changePasswordHeader = findViewById(R.id.change_password_header_title_sli);
        changePasswordHeader.setVisibility(View.VISIBLE);
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

    @Override
    protected DefaultUserPreferencePresenter fingerprintPreferenceMarket(UserPreferencePresenter.SettingsToggleChangeListener listener) {

        return new DefaultUserPreferencePresenter(R.string.user_prefs_fingerprint_title_92,
                R.string.user_prefs_fingerprint_message_93,
                deviceSpecificPreferences.isFingerprint(),
                R.drawable.icn_fingerprint,
                UserPreferencePresenter.Type.Fingerprint,
                true,
                false,
                listener);
    }
}
