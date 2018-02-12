package com.vitalityactive.va.userpreferences;

import android.view.View;
import android.widget.ImageView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.userpreferences.entities.DefaultUserPreferencePresenter;

import java.util.ArrayList;

public class SecurityPreferencesActivity extends BaseSecurityPreferencesActivity{


    @Override
    protected void marketUIUpdate() {

    }

    @Override
    protected void showChangePasswordItem(){

    }

    @Override
    protected void buildPreferencePresenterList(){
        userPreferencePresenters = new ArrayList<>();
        deviceSpecificPreferences.setRememberMe(false);
        userPreferencePresenters.add(rememberMePreferencePresenter);
    }
}