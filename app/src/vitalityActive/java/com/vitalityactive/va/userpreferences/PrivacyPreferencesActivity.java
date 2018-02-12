package com.vitalityactive.va.userpreferences;

/**
 * Created by dharel.h.rosell on 12/15/2017.
 */

public class PrivacyPreferencesActivity extends BasePrivacyPreferencesActivity {

    @Override
    protected void marketUiUpdate(){
        params.setMargins(0, 24, 0, 0);
        privacyStatementButton.setTextColor(globalTintColor);
        privacyStatementButton.setLayoutParams(params);
    }

    @Override
    protected void ukeUiUpdate() {

    }
}
