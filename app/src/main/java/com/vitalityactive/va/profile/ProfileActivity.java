package com.vitalityactive.va.profile;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.pushnotification.InAppPreferences;
import com.vitalityactive.va.pushnotification.InAppType;

import java.util.Set;

import javax.inject.Inject;

public class ProfileActivity extends BaseActivity {

    public static final String GLOBAL_TINT_COLOR = "GLOBAL_TINT_COLOR";

    @Inject
    AppConfigRepository appConfigRepository;
    @Inject
    EventDispatcher eventDispatcher;
    @Inject
    InAppPreferences inAppPreferences;

    private int globalTintColor, globalTintDarker;

    @Override
    protected void showInAppScreenMessages(InAppType inAppType) {
        if (inAppType == InAppType.PROFILE) {
            Set<String> messages = inAppPreferences.getProfileMessages();
            for (String message: messages) {
                showSnackbar(message);
            }
            inAppPreferences.resetProfileMessage();
            inAppPreferences.resetMenuProfileBadge();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        getDependencyInjector().inject(this);

        globalTintColor = Color.parseColor(appConfigRepository.getGlobalTintColorHex());
        globalTintDarker = Color.parseColor(appConfigRepository.getGlobalTintDarkerColorHex());

        setUpActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            replaceContent(new ProfileFragment());
            setActionBarTitle(getString(R.string.Settings_landing_title_901));
        }
        setActionBarColor(globalTintColor);
        setStatusBarColor(globalTintDarker);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void replaceContent(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(GLOBAL_TINT_COLOR, appConfigRepository.getGlobalTintColorHex());
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
