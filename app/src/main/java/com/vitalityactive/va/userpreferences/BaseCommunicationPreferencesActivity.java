package com.vitalityactive.va.userpreferences;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.userpreferences.entities.DefaultUserPreferencePresenter;
import com.vitalityactive.va.userpreferences.viewholder.UserPreferenceItemViewHolder;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public abstract class BaseCommunicationPreferencesActivity extends BaseActivity {

    @Inject
    AppConfigRepository appConfigRepository;

    @Inject
    EmailPreferencePresenter emailPreferencePresenter;

    private @ColorInt int globalTintColor, globalTintDarker;

    protected List<DefaultUserPreferencePresenter> userPreferencePresenters;
    protected RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_communication_preferences);

        navigationCoordinator.getPNSCaptureDependencyInjector().inject(this);

        globalTintColor = Color.parseColor(appConfigRepository.getGlobalTintColorHex());
        globalTintDarker = Color.parseColor(appConfigRepository.getGlobalTintDarkerColorHex());

        setUpActionBarWithTitle(R.string.user_prefs_communication_group_header_title_64)
                .setDisplayHomeAsUpEnabled(true);

        buildPreferencePresenterList();
        setUpRecyclerView();

        setActionBarColor(globalTintColor);
        setStatusBarColor(globalTintDarker);
    }


    protected void buildPreferencePresenterList() {
        userPreferencePresenters = new ArrayList<>();
        userPreferencePresenters.add(emailPreferencePresenter);
        marketUiUpdate();
        userPreferencePresenters.add(new DefaultUserPreferencePresenter(R.string.user_prefs_push_message_toggle_title_67,
                    R.string.user_prefs_push_message_toggle_message_68,
                    false,
                    R.drawable.icn_notification,
                    UserPreferencePresenter.Type.Notifications,
                    false,
                    true, null));
    }


    protected void setUpRecyclerView () {
        GenericRecyclerViewAdapter<DefaultUserPreferencePresenter, UserPreferenceItemViewHolder> adapter =
                new GenericRecyclerViewAdapter<>(this,
                        userPreferencePresenters,
                        R.layout.preference_item,
                        new UserPreferenceItemViewHolder.Factory(globalTintColor));

        this.recyclerView = findViewById(R.id.recycler_view);
        ViewUtilities.addDividers(this, recyclerView, ViewUtilities.pxFromDp(72));
        recyclerView.setAdapter(adapter);
    }

    public void handleManageInSettingsButtonTapped(View view) {
        final Intent settingsIntent = new Intent();
        settingsIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        settingsIntent.addCategory(Intent.CATEGORY_DEFAULT);
        settingsIntent.setData(Uri.parse("package:" + getPackageName()));
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

        startActivity(settingsIntent);
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

}
