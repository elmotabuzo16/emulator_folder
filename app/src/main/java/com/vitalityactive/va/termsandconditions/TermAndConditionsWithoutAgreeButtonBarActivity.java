package com.vitalityactive.va.termsandconditions;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dependencyinjection.DependencyNames;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by kerry.e.lawagan on 11/21/2017.
 */

public class TermAndConditionsWithoutAgreeButtonBarActivity extends TermsAndConditionsActivity implements TermsAndConditionsWithoutAgreeButtonBarUserInterface {

    @Inject
    @Named(DependencyNames.TERMS_AND_CONDITIONS_NO_AGREE_BUTTON)
    TermsAndConditionsPresenter termsAndConditionsPresenter;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setActionBarTitleAndDisplayHomeAsUp(getResources().getString(R.string.Settings_terms_conditions_title_905));
        setStatusBarColor(getGlobalTintDarkerColor());
    }


    private @ColorInt
    int getGlobalTintDarkerColor() {
        return Color.parseColor(appConfigRepository.getGlobalTintDarkerColorHex());
    }


    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected TermsAndConditionsPresenter getPresenter() {
        return termsAndConditionsPresenter;
    }

    @Override
    public void hideButtonBar() {
        super.findViewById(R.id.agree_button_bar).setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        finish();
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
