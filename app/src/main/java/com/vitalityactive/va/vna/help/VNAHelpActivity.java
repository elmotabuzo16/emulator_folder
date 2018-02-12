package com.vitalityactive.va.vna.help;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;

import javax.inject.Inject;

public class VNAHelpActivity extends BaseActivity {

    public static final String GLOBAL_TINT_COLOR = "GLOBAL_TINT_COLOR";
    public static final String GLOBAL_HELP_KEY="GLOBAL_HELP_KEY";

    @Inject
    AppConfigRepository appConfigRepository;

    private int globalTintColour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenings_and_vaccinations_help);
        getDependencyInjector().inject(this);

        globalTintColour = Color.parseColor(appConfigRepository.getGlobalTintColorHex());
        setUpActionBar().setDisplayHomeAsUpEnabled(true);
        setActionBarTitle(getResources().getString(R.string.help_button_141));
        replaceContent(new VNAHelpFragment());

        setActionBarColor(ContextCompat.getColor(this, R.color.jungle_green));
        setStatusBarColor(ContextCompat.getColor(this, R.color.jungle_green_dark));
    }

    private void replaceContent(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putInt(GLOBAL_TINT_COLOR,globalTintColour);
        bundle.putString(GLOBAL_HELP_KEY,"REMOVE_MARGIN_TOP");
        fragment.setArguments(bundle);
        if( fragment instanceof VNAHelpFragment){
            commitTransactionOnly(fragment);
        }else{
            addToBackStackAndCommitTransaction(fragment);
        }
    }

    private void addToBackStackAndCommitTransaction(Fragment fragment){
        Log.e("S&V:","addToBackStackAndCommitTransaction");
        getSupportFragmentManager().beginTransaction().replace(R.id.s_and_v_help_container, fragment).addToBackStack(null).commit();
    }

    private void commitTransactionOnly(Fragment fragment){
        Log.e("S&V:","commitTransactionOnly");
        getSupportFragmentManager().beginTransaction().replace(R.id.s_and_v_help_container, fragment).commit();
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
