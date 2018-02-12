package com.vitalityactive.va.userpreferences;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.shared.activities.BaseTermsAndConditionsWithoutAgreeButtonBarActivity;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsWithoutAgreeButtonBarPresenter;

import javax.inject.Inject;
import javax.inject.Named;

public class PrivacyStatementActivity extends BaseTermsAndConditionsWithoutAgreeButtonBarActivity {
    @Inject
    @Named(DependencyNames.PREFERENCES_PRIVACY_POLICY)
    TermsAndConditionsWithoutAgreeButtonBarPresenter presenter;

    @Inject
    AppConfigRepository appConfigRepository;

    private @ColorInt
    int globalTintColor, globalTintDarker;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        getDependencyInjector().inject(this);

        globalTintColor = Color.parseColor(appConfigRepository.getGlobalTintColorHex());
        globalTintDarker = Color.parseColor(appConfigRepository.getGlobalTintDarkerColorHex());

        setActionBarColor(globalTintColor);
        setStatusBarColor(globalTintDarker);
    }

    @Override
    @StringRes
    protected int getActionBarTitle() {
        return R.string.user_prefs_privacy_group_header_link_button_title_72;
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        super.injectDependencies(dependencyInjector);
        getDependencyInjector().inject(this);
    }

    @Override
    protected TermsAndConditionsWithoutAgreeButtonBarPresenter getPresenter() {
        return presenter;
    }
}
