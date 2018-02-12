package com.vitalityactive.va.userpreferences;

import android.content.Intent;
import android.view.View;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.R;
import com.vitalityactive.va.userpreferences.entities.UserPreferenceGroup;
import com.vitalityactive.va.userpreferences.learnmore.ShareVitalityStatusActivity;
import com.vitalityactive.va.userpreferences.learnmore.presenter.ShareVitalityStatusPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

public class FirstTimeUserPreferencesActivity extends BaseFirstTimeUserPreferencesActivity implements View.OnClickListener {


    @Override
    protected void buildPreferencePresenterList(){
        preferenceGroups = new ArrayList<>();
        preferenceGroups.add(UserPreferenceGroup.Builder.communication(emailPreferencePresenter));

        ShareVitalityStatusPresenter shareVitalityStatusPresenter =
                new ShareVitalityStatusPresenter(R.string.uke_communication_pref_title_status_375,
                R.string.uke_communication_pref_message_status_376,
                deviceSpecificPreferences.isSharedStatus(),
                R.drawable.vitality_status, UserPreferencePresenter.Type.ShareVitalityStatus,
                true, true, deviceSpecificPreferences, this,
                scheduler, partyInformationRepository, eventDispatcher, serviceClient);

        deviceSpecificPreferences.setRememberMe(false);
//        preferenceGroups.add(UserPreferenceGroup.Builder.privacy(deviceSpecificPreferences));
        preferenceGroups.add(UserPreferenceGroup.Builder.ukePrivacy(deviceSpecificPreferences, shareVitalityStatusPresenter));
        preferenceGroups.add(UserPreferenceGroup.Builder.security(deviceSpecificPreferences, mSettingsToggleChangeListener, false,
                rememberMePreferencePresenter));

        userPreferencePresenters = new ArrayList<>();
        for (UserPreferenceGroup group : preferenceGroups) {
            userPreferencePresenters.addAll(group.userPreferenceItems);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ShareVitalityStatusActivity.class);
        startActivity(intent);
    }
}
