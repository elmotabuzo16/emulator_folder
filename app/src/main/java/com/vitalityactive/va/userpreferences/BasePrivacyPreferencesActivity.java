package com.vitalityactive.va.userpreferences;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.userpreferences.entities.DefaultUserPreferencePresenter;
import com.vitalityactive.va.userpreferences.viewholder.UserPreferenceItemViewHolder;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public abstract class BasePrivacyPreferencesActivity extends BaseActivity {

    @Inject
    AppConfigRepository appConfigRepository;

    @Inject
    AnalyticsPreferencePresenter analyticsPreferencePresenter;

    @Inject
    CrashReportsPreferencePresenter crashReportsPreferencePresenter;

    @Inject
    protected EventDispatcher eventDispatcher;

    @Inject
    protected DeviceSpecificPreferences deviceSpecificPreferences;

    @Inject
    protected PartyInformationRepository partyInformationRepository;

    @Inject
    protected ShareVitalityStatusPreferenceServiceClient serviceClient;

    @Inject
    MainThreadScheduler scheduler;

    protected @ColorInt int globalTintColor, globalTintDarker;

    protected List<DefaultUserPreferencePresenter> userPreferencePresenters;

    protected Button privacyStatementButton;
    protected TextView preferenceDescription;
    protected LinearLayout.LayoutParams params;
    protected TextView privacyTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_preferences);

        navigationCoordinator.getPNSCaptureDependencyInjector().inject(this);

        globalTintColor = Color.parseColor(appConfigRepository.getGlobalTintColorHex());
        globalTintDarker = Color.parseColor(appConfigRepository.getGlobalTintDarkerColorHex());

        setUpActionBarWithTitle(R.string.user_prefs_privacy_group_header_title_70)
                .setDisplayHomeAsUpEnabled(true);

        setActionBarColor(globalTintColor);
        setStatusBarColor(globalTintDarker);

        privacyTitle = findViewById(R.id.preference_title);
        preferenceDescription = findViewById(R.id.preference_description);
        privacyStatementButton = findViewById(R.id.privacy_statement_button);

        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        marketUiUpdate();
        buildPreferencePresenterList();
        setUpRecyclerView();
    }

    protected void buildPreferencePresenterList() {
        userPreferencePresenters = new ArrayList<>();
        ukeUiUpdate();
        userPreferencePresenters.add(analyticsPreferencePresenter);
        userPreferencePresenters.add(crashReportsPreferencePresenter);
    }

    private void setUpRecyclerView() {
        GenericRecyclerViewAdapter<DefaultUserPreferencePresenter, UserPreferenceItemViewHolder> adapter =
                new GenericRecyclerViewAdapter<>(this,
                        userPreferencePresenters,
                        R.layout.preference_item,
                        new UserPreferenceItemViewHolder.Factory(globalTintColor));

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        ViewUtilities.addDividers(this, recyclerView, ViewUtilities.pxFromDp(72));
        recyclerView.setAdapter(adapter);
    }

    public void handlePrivacyStatementButtonTapped(View view) {
        navigationCoordinator.AfterPrivacyStatementButtonTapped(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (DefaultUserPreferencePresenter presenter : userPreferencePresenters) {
            presenter.onUserInterfaceAppeared();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (DefaultUserPreferencePresenter presenter : userPreferencePresenters) {
            presenter.onUserInterfaceDisappeared(isFinishing());
        }
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

    protected abstract void marketUiUpdate();
    protected abstract void ukeUiUpdate();

}
