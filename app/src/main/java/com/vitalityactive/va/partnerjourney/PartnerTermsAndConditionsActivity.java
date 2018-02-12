package com.vitalityactive.va.partnerjourney;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.MenuItem;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.shared.activities.BaseTermsAndConditionsWithoutAgreeButtonBarActivity;

import javax.inject.Inject;

public class PartnerTermsAndConditionsActivity extends BaseTermsAndConditionsWithoutAgreeButtonBarActivity {

    public static final String TERMS_AND_CONDITIONS_ARTICLE_ID = "TERMS_AND_CONDITIONS_ARTICLE_ID";

    @Inject
    PartnerTermsAndConditionsPresenter presenter;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        presenter.setArticleId(getIntent().getStringExtra(TERMS_AND_CONDITIONS_ARTICLE_ID));
    }

    @Override
    @StringRes
    protected int getActionBarTitle() {
        return R.string.terms_and_conditions_screen_title_94;
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        super.injectDependencies(dependencyInjector);
        navigationCoordinator.getPartnerJourneyDependencyInjector().inject(this);
    }

    @Override
    protected PartnerTermsAndConditionsPresenter getPresenter() {
        return presenter;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
