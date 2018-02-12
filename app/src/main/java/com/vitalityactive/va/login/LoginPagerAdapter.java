package com.vitalityactive.va.login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vitalityactive.va.onboarding.OnboardingFragment;
import com.vitalityactive.va.onboarding.OnboardingViewModel;

public class LoginPagerAdapter extends FragmentPagerAdapter {
    private int numberOfPages;
    private OnboardingViewModel onboardingViewModel;

    public LoginPagerAdapter(FragmentManager fragmentManager, OnboardingViewModel onboardingViewModel) {
        super(fragmentManager);
        this.onboardingViewModel = onboardingViewModel;
        numberOfPages = onboardingViewModel.getNumberOfPages();
    }

    @Override
    public int getCount() {
        if (onboardingViewModel.getNumberOfPages() != numberOfPages) {
            numberOfPages = onboardingViewModel.getNumberOfPages();
            notifyDataSetChanged();
        }
        return numberOfPages;
    }

    @Override
    public Fragment getItem(int position) {
        if (onboardingViewModel.isOnboardingPageAtPosition(position))
            return OnboardingFragment.create(position);
        return new LoginFragment();
    }
}
