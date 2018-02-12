package com.vitalityactive.va.vhr.disclaimer;

import android.support.annotation.StringRes;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.shared.activities.BaseTermsAndConditionsWithoutAgreeButtonBarActivity;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsWithoutAgreeButtonBarPresenter;

import javax.inject.Inject;
import javax.inject.Named;

public class VHRDisclaimerActivity extends BaseTermsAndConditionsWithoutAgreeButtonBarActivity {
    @Inject
    @Named(DependencyNames.VHR)
    TermsAndConditionsWithoutAgreeButtonBarPresenter presenter;

    @Override
    @StringRes
    protected int getActionBarTitle() {
        return R.string.disclaimer_title_265;
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
