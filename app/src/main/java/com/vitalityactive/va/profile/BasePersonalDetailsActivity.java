package com.vitalityactive.va.profile;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;

import javax.inject.Inject;

public abstract class BasePersonalDetailsActivity extends BaseActivity {

    public static final String GLOBAL_TINT_COLOR = "GLOBAL_TINT_COLOR";

    @Inject
    AppConfigRepository appConfigRepository;

    private int globalTintColour, globalTintDarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_details);


        getDependencyInjector().inject(this);

        globalTintColour = Color.parseColor(appConfigRepository.getGlobalTintColorHex());
        globalTintDarker = Color.parseColor(appConfigRepository.getGlobalTintDarkerColorHex());

        setUpActionBar()
                .setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            replaceContent(new PersonalDetailsFragment());
            setActionBarTitle(getResources().getString( R.string.Settings_profile_landing_personal_details_title_912 ));
        }
        setStatusBarColor(globalTintDarker);
        setActionBarColor(globalTintColour);
        marketUIUpdate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                updateActionBarIcon(R.drawable.ic_arrow_back_white_24dp);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void replaceContent(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(GLOBAL_TINT_COLOR, appConfigRepository.getGlobalTintColorHex());
        fragment.setArguments(bundle);
        if( fragment instanceof PersonalDetailsFragment){
            commitTransactionOnly(fragment);
        }else{
            addToBackStackAndCommitTransaction(fragment);
        }
    }

    private void addToBackStackAndCommitTransaction(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.personal_details_container, fragment).addToBackStack(null).commit();
    }

    private void commitTransactionOnly(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.personal_details_container, fragment).commit();
    }

    void replaceContentWithChangeEmail() {
        replaceContent(new ChangeEmailFragment());
        setActionBarTitle(getResources().getString( R.string.Settings_profile_change_email_title_921));
        updateActionBarIcon(R.drawable.ic_close_white_24dp);
    }



    public void changeActionBarHomeIconToBackArrow(){
        updateActionBarIcon(R.drawable.ic_arrow_back_white_24dp);
    }

    protected abstract void marketUIUpdate();
}
