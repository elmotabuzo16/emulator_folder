package com.vitalityactive.va.userpreferences;

import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.userpreferences.entities.DefaultUserPreferencePresenter;

import java.util.ArrayList;

public class CommunicationPreferencesActivity extends BaseCommunicationPreferencesActivity {

    @Override
    protected void marketUiUpdate() {
        if (findViewById(R.id.preference_description) != null) {
            findViewById(R.id.preference_description).setVisibility(View.GONE);
        }

        userPreferencePresenters.add(new DefaultUserPreferencePresenter(0,
                R.string.user_prefs_email_toggle_note_9999,
                false,
                0,
                null,
                false,
                false, null));
    }

    @Override
    protected void buildPreferencePresenterList(){
        userPreferencePresenters = new ArrayList<>();
        userPreferencePresenters.add(emailPreferencePresenter);
        marketUiUpdate();

        userPreferencePresenters.add(new DefaultUserPreferencePresenter(R.string.user_prefs_notifications_title_89,
                R.string.user_prefs_manage_in_settings_button_title_91,
                false,
                R.drawable.icn_notification,
                UserPreferencePresenter.Type.Notifications,
                false,
                false, null));
    }

}
