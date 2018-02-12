package com.vitalityactive.va.membershippass;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;

import javax.inject.Inject;

public class VitalityNumberActivity extends BaseActivity {
    public static final String GLOBAL_TINT_COLOR = "GLOBAL_TINT_COLOR";
    private String activityTitle="",activityContent="";
    private TextView membershipPassInfoContentView;
    @Inject
    AppConfigRepository appConfigRepository;

    private int globalTintColour, globalTintDarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vitality_number);

        getDependencyInjector().inject(this);

        globalTintColour = Color.parseColor(appConfigRepository.getGlobalTintColorHex());
        globalTintDarker =  Color.parseColor(appConfigRepository.getGlobalTintDarkerColorHex());

        setUpActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle extras  =getIntent().getExtras();
            if(extras!=null){
                switch(extras.get(MembershipPassFragment.KEY_TITLE).toString()){
                     case "membership":
                        activityTitle=getResources().getString(R.string.profile_field_membership_number_title_1125);
                        activityContent=getResources().getString(R.string.profile_field_vitality_membership_number);
                        break;
                    case "customer":
                        activityTitle=getResources().getString(R.string.profile_field_customer_number_title_1124);
                        activityContent=getResources().getString(R.string.profile_field_vitality_customer_number);
                        break;
                    default:
                        activityTitle=getResources().getString(R.string.profile_field_vitality_number_title);
                        activityContent=getResources().getString(R.string.profile_field_vitality_number_information);
                        break;
                }
            }
            setActionBarTitle(activityTitle);
        }

        setActionBarColor(globalTintColour);
        setStatusBarColor(globalTintDarker);
        membershipPassInfoContentView = findViewById(R.id.membership_pass_info_content);
        membershipPassInfoContentView.setText(activityContent);
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
