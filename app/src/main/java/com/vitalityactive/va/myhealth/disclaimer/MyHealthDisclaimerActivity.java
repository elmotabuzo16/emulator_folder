package com.vitalityactive.va.myhealth.disclaimer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.dependencyinjection.myhealth.MyHealthDependencyInjector;
import com.vitalityactive.va.shared.activities.BaseTermsAndConditionsWithoutAgreeButtonBarActivity;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsWithoutAgreeButtonBarPresenter;

import javax.inject.Inject;
import javax.inject.Named;

public class MyHealthDisclaimerActivity extends BaseTermsAndConditionsWithoutAgreeButtonBarActivity {

    @Inject
    @Named(DependencyNames.MY_HEALTH)
    MyHealthDisclaimerPresenter presenter;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        super.injectDependencies(dependencyInjector);
        MyHealthDependencyInjector myHealthDependencyInjector = navigationCoordinator.getMyHealthDependencyInjector();
        myHealthDependencyInjector.inject(this);
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.disclaimer_title_265;
    }

    @Override
    protected TermsAndConditionsWithoutAgreeButtonBarPresenter getPresenter() {
        return presenter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
