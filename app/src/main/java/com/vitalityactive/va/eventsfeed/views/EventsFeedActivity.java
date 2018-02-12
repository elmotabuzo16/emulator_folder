package com.vitalityactive.va.eventsfeed.views;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.eventsfeed.views.fragments.EventsFeedFragment;

import javax.inject.Inject;

public class EventsFeedActivity extends BaseActivity {

    public static final String GLOBAL_TINT_COLOR = "GLOBAL_TINT_COLOR";

    @Inject
    AppConfigRepository appConfigRepository;

    private int globalTintColour, globalTintDarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_events_feed);


        getDependencyInjector().inject(this);

        globalTintColour = Color.parseColor(appConfigRepository.getGlobalTintColorHex());
        globalTintDarker =  Color.parseColor(appConfigRepository.getGlobalTintDarkerColorHex());

        setUpActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            replaceContent(new EventsFeedFragment());
            setActionBarTitle(getString(R.string.Settings_landing_events_title_900));
        }
        setActionBarColor(globalTintColour);
        setStatusBarColor(globalTintDarker);
    }

    private void replaceContent(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(GLOBAL_TINT_COLOR, appConfigRepository.getGlobalTintColorHex());
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.events_feed_container, fragment).commit();
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
