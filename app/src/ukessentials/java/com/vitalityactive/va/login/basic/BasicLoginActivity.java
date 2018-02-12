package com.vitalityactive.va.login.basic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.vitalityactive.va.BaseUkeActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.login.getstarted.UKEGetStartedPresenter;

import javax.inject.Inject;

public class BasicLoginActivity extends BaseUkeActivity {
    @Inject
    UKEGetStartedPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDependencyInjector().inject(this);
        setContentView(R.layout.activity_basic_login);
        setupViewPager();
    }

    private void setupViewPager() {
        ViewPager viewPager = findViewById(R.id.login_viewpager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public Fragment getItem(int position) {
                return new BasicLoginFragment();
            }
        });
    }

    public void handleRegisterButtonTapped(View view) {
        navigationCoordinator.navigateFromLoginAfterRegisterTapped(this, "");
    }

    public void handleForgotPasswordButtonTapped(View view) {
        navigationCoordinator.navigateFromLoginAfterForgotPasswordTapped(this);
    }
}
