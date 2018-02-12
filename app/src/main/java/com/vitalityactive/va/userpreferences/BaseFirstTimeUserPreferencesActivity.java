package com.vitalityactive.va.userpreferences;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.R;
import com.vitalityactive.va.ServiceLocator;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.login.FingerprintAlertDialogFragment;
import com.vitalityactive.va.login.FingerprintAlertDialogFragmentEvent;
import com.vitalityactive.va.register.view.SimpleTextViewHolder;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.uicomponents.ButtonBarConfigurator;
import com.vitalityactive.va.uicomponents.adapters.CompositeRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.userpreferences.entities.DefaultUserPreferencePresenter;
import com.vitalityactive.va.userpreferences.entities.UserPreferenceGroup;
import com.vitalityactive.va.userpreferences.viewholder.UserPreferenceViewHolder;
import com.vitalityactive.va.utilities.FingerprintUtility;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.inject.Inject;

public class BaseFirstTimeUserPreferencesActivity extends BaseActivity implements ButtonBarConfigurator.OnClickListener {
    private static final String KEY_NAME = "fingerPrintStorage";

    @Inject
    protected DeviceSpecificPreferences deviceSpecificPreferences;

    @Inject
    PartyInformationRepository partyInformationRepository;

    @Inject
    EmailPreferencePresenter emailPreferencePresenter;

    @Inject
    RememberMePreferencePresenter rememberMePreferencePresenter;

    @Inject
    AppConfigRepository appConfigRepository;

    @Inject
    protected EventDispatcher eventDispatcher;

    @Inject
    protected ShareVitalityStatusPreferenceServiceClient serviceClient;

    @Inject
    MainThreadScheduler scheduler;

    protected ArrayList<UserPreferenceGroup> preferenceGroups;
    protected ArrayList<DefaultUserPreferencePresenter> userPreferencePresenters;
    private GenericRecyclerViewAdapter<UserPreferenceGroup, UserPreferenceViewHolder> preferenceGroupsAdapter;
    private CompositeRecyclerViewAdapter compositeRecyclerViewAdapter;

    private
    @ColorInt
    int globalTintColor, globalTintDarker;

    private FingerprintUtility printUtil;
    private FingerprintAlertDialogFragment fingerprintAlertDialogFragment = new FingerprintAlertDialogFragment();

    @Override
    public void onBackPressed() {
        onButtonBarForwardClicked();
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
    public void onButtonBarForwardClicked() {
        if (isAnySettingEnabled(userPreferencePresenters)) {
            saveAllUserPreferences();
        } else {
            showNoSettingsAreEnabledAlert();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigationCoordinator.getPNSCaptureDependencyInjector().inject(this);

        setContentView(R.layout.activity_user_preferences);

        printUtil = new FingerprintUtility();

        globalTintColor = Color.parseColor(appConfigRepository.getGlobalTintColorHex());
        globalTintDarker = Color.parseColor(appConfigRepository.getGlobalTintDarkerColorHex());

        setUpActionBarWithTitle(R.string.user_prefs_setup_tite_374)
                .setBackgroundDrawable(new ColorDrawable(globalTintColor));
        setupButtonBar()
                .setForwardButtonTextToNext()
                .setForwardButtonEnabled(true)
                .setForwardButtonOnClick(this);

        setActionBarColor(globalTintColor);
        setStatusBarColor(globalTintDarker);


        buildPreferencePresenterList();
        setUpRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (DefaultUserPreferencePresenter presenter : userPreferencePresenters) {
            presenter.onUserInterfaceAppeared();
        }

        ServiceLocator.getInstance().eventDispatcher.addEventListener(FingerprintAlertDialogFragmentEvent.class, onFingerprintAlertDialogFragmentEvent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (DefaultUserPreferencePresenter presenter : userPreferencePresenters) {
            presenter.onUserInterfaceDisappeared(isFinishing());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        ServiceLocator.getInstance().eventDispatcher.removeEventListener(FingerprintAlertDialogFragmentEvent.class, onFingerprintAlertDialogFragmentEvent);
    }

    protected void buildPreferencePresenterList() {
        preferenceGroups = new ArrayList<>();
        preferenceGroups.add(UserPreferenceGroup.Builder.communication(emailPreferencePresenter));
        preferenceGroups.add(UserPreferenceGroup.Builder.privacy(deviceSpecificPreferences));
        preferenceGroups.add(UserPreferenceGroup.Builder.security(deviceSpecificPreferences, mSettingsToggleChangeListener, printUtil.fingerprintEnabled(),
                rememberMePreferencePresenter));

        userPreferencePresenters = new ArrayList<>();
        for (UserPreferenceGroup group : preferenceGroups) {
            userPreferencePresenters.addAll(group.userPreferenceItems);
        }
    }

    private void setUpRecyclerView() {
        preferenceGroupsAdapter =
                new GenericRecyclerViewAdapter<>(this,
                        preferenceGroups,
                        R.layout.preference_group,
                        new UserPreferenceViewHolder.Factory(globalTintColor));

        GenericRecyclerViewAdapter<String, SimpleTextViewHolder> footerAdapter =
                new GenericRecyclerViewAdapter<>(this,
                        Collections.singletonList(getString(R.string.user_prefs_user_prefs_screen_footnote_message_83)),
                        R.layout.preferences_footer,
                        new SimpleTextViewHolder.Factory());

        //noinspection AndroidLintUseSparseArrays
        HashMap<Integer, GenericRecyclerViewAdapter> adapters = new HashMap<>();
        adapters.put(R.layout.preference_group, preferenceGroupsAdapter);
        adapters.put(R.layout.preferences_footer, footerAdapter);


        compositeRecyclerViewAdapter =
                new CompositeRecyclerViewAdapter(adapters,
                        new int[]{R.layout.preference_group,
                                R.layout.preference_group,
                                R.layout.preference_group,
                                R.layout.preferences_footer});

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        ViewUtilities.addDividers(this, recyclerView);
        recyclerView.setAdapter(compositeRecyclerViewAdapter);
    }

    private void saveAllUserPreferences() {
        for (DefaultUserPreferencePresenter item : userPreferencePresenters) {
            switch (item.getType()) {
                case RememberMe:
                    saveRememberMePreference(item);
                    break;
                case Analytics:
                    saveAnalyticsPreference(item);
                    break;
                case CrashReports:
                    saveCrashReportPreference(item);
                    break;
            }
        }

        navigationCoordinator.navigateFromFirstTimePreferences(this);
    }

    private void showNoSettingsAreEnabledAlert() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.user_prefs_no_preferences_set_alert_title_85)
                .setMessage(R.string.user_prefs_no_preferences_set_alert_message_86)
                .setPositiveButton(R.string.continue_button_title_87, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        saveAllUserPreferences();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private boolean isAnySettingEnabled(ArrayList<DefaultUserPreferencePresenter> preferenceItems) {
        boolean someSettingIsEnabled = false;

        for (DefaultUserPreferencePresenter item : preferenceItems) {
            if (item.isOptedIn()) {
                someSettingIsEnabled = true;
                break;
            }
        }

        return someSettingIsEnabled;
    }

    private void saveCrashReportPreference(DefaultUserPreferencePresenter item) {
        deviceSpecificPreferences.setEnableCrashReports(item.isOptedIn());
    }

    private void saveAnalyticsPreference(DefaultUserPreferencePresenter item) {
        deviceSpecificPreferences.setAnalytics(item.isOptedIn());
    }

    private void saveRememberMePreference(DefaultUserPreferencePresenter item) {
        deviceSpecificPreferences.setRememberMe(item.isOptedIn());

//        if (item.isOptedIn()) {
//            deviceSpecificPreferences.setRememberedUsername(partyInformationRepository.getUsername());
//        } else {
//            deviceSpecificPreferences.clearRememberedUsername();
//        }
    }

    public void handlePrivacyStatementButtonTapped(View view) {
        navigationCoordinator.AfterPrivacyStatementButtonTapped(this);
    }

    protected UserPreferencePresenter.SettingsToggleChangeListener mSettingsToggleChangeListener = new UserPreferencePresenter.SettingsToggleChangeListener() {
        @Override
        public void onToggle(boolean enabled) {
            fingerprintAlertDialogFragment.setSuccessFingerPrintAuth(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (printUtil.fingerprintEnabled() && enabled) {
                    showFingerPrintLoginDialog();
                } else if (!printUtil.fingerprintEnabled() && enabled) {
                    deviceSpecificPreferences.setFingerprint(false);
                    updatePreferenceGroups();
                }
            }

            if(!enabled) {
                rememberMePreferencePresenter.enableToggle(true);
            }

        }
    };

    public EventListener<FingerprintAlertDialogFragmentEvent> onFingerprintAlertDialogFragmentEvent = new EventListener<FingerprintAlertDialogFragmentEvent>() {
        @Override
        public void onEvent(FingerprintAlertDialogFragmentEvent event) {
            if (event.getDialogType() == FingerprintAlertDialogFragment.DialogType.FINGERPRINT_RECOGNITION
                    || event.getDialogType() == FingerprintAlertDialogFragment.DialogType.FINGERPRINT_RECOGNITION_LOGIN) {
                if (!fingerprintAlertDialogFragment.isSuccessFingerPrintAuth()) {
                    deviceSpecificPreferences.setFingerprint(fingerprintAlertDialogFragment.isSuccessFingerPrintAuth());
                    updatePreferenceGroups();
                    preferenceGroupsAdapter.notifyDataSetChanged();
                    compositeRecyclerViewAdapter.notifyDataSetChanged();
                } else {
                    showEnterPassword();
                }
                return;
            }

            if (event.getDialogType() == FingerprintAlertDialogFragment.DialogType.ENTER_PASSWORD) {
                if (event.getClickedButtonType() == FingerprintAlertDialogFragmentEvent.ClickedButtonType.NEGATIVE) {
                    deviceSpecificPreferences.setFingerprint(false);
                    rememberMePreferencePresenter.enableToggle(true);
                    updatePreferenceGroups();
                } else {
                    deviceSpecificPreferences.setRememberMe(true);

                    rememberMePreferencePresenter.enableToggle(false);
                    deviceSpecificPreferences.setFingerprint(true);
                    preferenceGroupsAdapter.notifyDataSetChanged();
                    compositeRecyclerViewAdapter.notifyDataSetChanged();
                }
                return;
            }
        }
    };

    private void showEnterPassword() {
        this.fingerprintAlertDialogFragment.create(
                this,
                FingerprintAlertDialogFragment.DialogType.ENTER_PASSWORD);
        this.fingerprintAlertDialogFragment.show(getSupportFragmentManager(), "FingerPrintDialog");
    }

    private void updatePreferenceGroups() {
        for (UserPreferenceGroup userPreferenceGroup : preferenceGroups) {
            if (userPreferenceGroup.getUserPreferenceType() == UserPreferenceGroup.UserPreferenceType.SECURITY) {
                updateFingerprint(userPreferenceGroup);
                break;
            }
        }

        preferenceGroupsAdapter.notifyDataSetChanged();
        compositeRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void updateFingerprint(UserPreferenceGroup userPreferenceGroup) {
        for (DefaultUserPreferencePresenter defaultUser : userPreferenceGroup.userPreferenceItems) {
            if (defaultUser.getType() == UserPreferencePresenter.Type.Fingerprint) {
                userPreferenceGroup.userPreferenceItems.get(0).onToggle(false);
                break;
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void showFingerPrintLoginDialog() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED) {
            // If your app doesn't have this permission, then display the following text//
            String message = getString(R.string.login_touchId_fingerprint_prompt_message_9999) + "\n" + getString(R.string.settings_security_finger_print_prompt_confirm_1157) + "\n";

            if (this.fingerprintAlertDialogFragment.isAdded())  this.fingerprintAlertDialogFragment.dismiss();
            this.fingerprintAlertDialogFragment.create(
                    this,
                    FingerprintAlertDialogFragment.DialogType.FINGERPRINT_RECOGNITION
            );

            printUtil.startAuthentication(this, fingerprintAlertDialogFragment);
            this.fingerprintAlertDialogFragment.show(getSupportFragmentManager(), "FingerPrintDialog");

        } else {
            AlertDialogFragment alert = AlertDialogFragment.create("FingerPrint",
                    getString(R.string.login_touchId_fingerprint_prompt_title_9999),
                    getString(R.string.settings_security_finger_print_enable_permission_1161),
                    getString(R.string.profile_change_email_cancel),
                    null,
                    null
            );
            alert.show(getSupportFragmentManager(), "FingerPrint");
        }
    }

}
