package com.vitalityactive.va.membershippass;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.databinding.ActivityContentHelpBinding;
import com.vitalityactive.va.help.HelpFragment;
import javax.inject.Inject;

public class MembershipPassHelpActivity extends BaseActivity {
    public static final String GLOBAL_TINT_COLOR = "GLOBAL_TINT_COLOR";
    public static final String GLOBAL_HELP_KEY="GLOBAL_HELP_KEY";



    @Inject
    AppConfigRepository appConfigRepository;

    private int globalTintColour, globalTintDarker;
    private String passedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_membership_pass_help);

        getDependencyInjector().inject(this);

        globalTintColour = Color.parseColor(appConfigRepository.getGlobalTintColorHex());
        globalTintDarker = Color.parseColor(appConfigRepository.getGlobalTintColorHex());
        setUpActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();
            if(extras!=null){
                passedColor=extras.getString(MembershipPassHelpActivity.GLOBAL_TINT_COLOR);

                if(passedColor.equals("green")){
                    globalTintColour= ContextCompat.getColor(getApplicationContext(),R.color.jungle_green_dark);
                    setActionBarColor(ContextCompat.getColor(this, R.color.jungle_green));
                }
            }
            setActionBarTitle(getResources().getString(R.string.help_button_141));
            replaceContent(new HelpFragment());
        }
        setStatusBarColor(globalTintDarker);
    }

    private void replaceContent(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putInt(GLOBAL_TINT_COLOR,globalTintColour);
        bundle.putString(GLOBAL_HELP_KEY,"REMOVE_MARGIN_TOP");
        fragment.setArguments(bundle);
        if( fragment instanceof HelpFragment){
            commitTransactionOnly(fragment);
        }else{
            addToBackStackAndCommitTransaction(fragment);
        }
    }

    private void addToBackStackAndCommitTransaction(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.membership_pass_help_container, fragment).addToBackStack(null).commit();
    }

    private void commitTransactionOnly(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.membership_pass_help_container, fragment).commit();
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
