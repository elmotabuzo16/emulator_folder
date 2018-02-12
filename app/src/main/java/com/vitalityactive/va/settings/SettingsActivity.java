package com.vitalityactive.va.settings;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;

import javax.inject.Inject;

public class SettingsActivity extends BaseActivity {

    public static final String GLOBAL_TINT_COLOR = "GLOBAL_TINT_COLOR";

    @Inject
    AppConfigRepository appConfigRepository;

    private int globalTintColour, globalTintDarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        getDependencyInjector().inject(this);

        globalTintColour = Color.parseColor(appConfigRepository.getGlobalTintColorHex());
        globalTintDarker = Color.parseColor(appConfigRepository.getGlobalTintDarkerColorHex());

        setUpActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            replaceContent(new SettingsFragment());
            setActionBarTitle(getString(R.string.settings_button_271));
        }
        setActionBarColor(globalTintColour);
        setStatusBarColor(globalTintDarker);
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
