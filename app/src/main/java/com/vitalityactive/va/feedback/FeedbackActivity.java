package com.vitalityactive.va.feedback;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.drew.lang.annotations.Nullable;
import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.databinding.ActivityFeedbackBinding;

import javax.inject.Inject;

public class FeedbackActivity extends BaseActivity {

    public static final String GLOBAL_TINT_COLOR = "GLOBAL_TINT_COLOR";

    @Inject
    AppConfigRepository appConfigRepository;

    private int globalTintColour,globalTintDarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);

        getDependencyInjector().inject(this);

        globalTintColour = Color.parseColor(appConfigRepository.getGlobalTintColorHex());
        globalTintDarker = Color.parseColor(appConfigRepository.getGlobalTintDarkerColorHex());

        setUpActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            replaceContent(new FeedbackFragment());
            setActionBarTitle(getString(R.string.Settings_feedback_section_title_949));
        }

        setActionBarColor(globalTintColour);
        setStatusBarColor(globalTintDarker);

    }

    private void replaceContent(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(GLOBAL_TINT_COLOR, appConfigRepository.getGlobalTintColorHex());
        fragment.setArguments(bundle);
        if( fragment instanceof BaseFeedbackFragment){
            commitTransactionOnly(fragment);
        }else{
            addToBackStackAndCommitTransaction(fragment);
        }

    }
    private void addToBackStackAndCommitTransaction(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).addToBackStack(null).commit();
    }

    private void commitTransactionOnly(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
