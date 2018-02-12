package com.vitalityactive.va.userpreferences;

import android.content.Intent;
import android.view.View;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.R;
import com.vitalityactive.va.userpreferences.learnmore.ShareVitalityStatusActivity;
import com.vitalityactive.va.userpreferences.learnmore.presenter.ShareVitalityStatusPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

public class PrivacyPreferencesActivity extends BasePrivacyPreferencesActivity  implements View.OnClickListener {

    @Override
    protected void marketUiUpdate(){

    }

    @Override
    protected void ukeUiUpdate() {
        userPreferencePresenters.add(new ShareVitalityStatusPresenter(R.string.uke_communication_pref_title_status_375,
                R.string.uke_communication_pref_message_status_376,
                deviceSpecificPreferences.isSharedStatus(),
                R.drawable.vitality_status, UserPreferencePresenter.Type.ShareVitalityStatus,
                true, true, deviceSpecificPreferences, this,
                scheduler, partyInformationRepository, eventDispatcher, serviceClient));
    }

    @Override
    protected void buildPreferencePresenterList() {
        userPreferencePresenters = new ArrayList<>();
        ukeUiUpdate();
        userPreferencePresenters.add(analyticsPreferencePresenter);
        userPreferencePresenters.add(crashReportsPreferencePresenter);
    }

    @Override
    public void onClick(View v) {
//        ukeNavigationCoordinator.navigateToShareVitalityLearnMore(this);
        Intent intent = new Intent(this, ShareVitalityStatusActivity.class);
        startActivity(intent);
    }
}