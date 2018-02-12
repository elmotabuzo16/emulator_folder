package com.vitalityactive.va.userpreferences;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.R;
import com.vitalityactive.va.ServiceLocator;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.login.FingerprintAlertDialogFragment;
import com.vitalityactive.va.login.FingerprintAlertDialogFragmentEvent;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.userpreferences.entities.DefaultUserPreferencePresenter;
import com.vitalityactive.va.userpreferences.viewholder.UserPreferenceItemViewHolder;
import com.vitalityactive.va.utilities.FingerprintUtility;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public abstract class BaseSecurityPreferencesActivity extends BaseActivity {

    @Inject
    AppConfigRepository appConfigRepository;

    @Inject
    protected DeviceSpecificPreferences deviceSpecificPreferences;

    @Inject
    RememberMePreferencePresenter rememberMePreferencePresenter;

    protected @ColorInt
    int globalTintColor, globalTintDarker;

    protected List<DefaultUserPreferencePresenter> userPreferencePresenters;

    private FingerprintAlertDialogFragment fingerprintAlertDialogFragment = new FingerprintAlertDialogFragment();

    private GenericRecyclerViewAdapter<DefaultUserPreferencePresenter, UserPreferenceItemViewHolder> adapter;

    private DefaultUserPreferencePresenter noFingerPrintPreference;

    protected ImageView preferenceItemIcon;

    protected FingerprintUtility printUtil;

    //shared variables
    protected DefaultUserPreferencePresenter fingerPrintPreference;

    //abstract methods
    protected abstract void marketUIUpdate();

    protected abstract void showChangePasswordItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_security_preferences);

        navigationCoordinator.getPNSCaptureDependencyInjector().inject(this);

        globalTintColor = Color.parseColor(appConfigRepository.getGlobalTintColorHex());
        globalTintDarker = Color.parseColor(appConfigRepository.getGlobalTintDarkerColorHex());
        setUpActionBarWithTitle(R.string.user_prefs_security_group_header_title_77)
                .setDisplayHomeAsUpEnabled(true);

        setActionBarColor(globalTintColor);
        setStatusBarColor(globalTintDarker);

        printUtil = new FingerprintUtility();

        buildPreferencePresenterList();
        setUpRecyclerView();
        showChangePasswordItem();

    }

    protected void buildPreferencePresenterList() {
        userPreferencePresenters = new ArrayList<>();

        if (printUtil.fingerprintEnabled()) {
            fingerPrintPreference = fingerprintPreferenceMarket(mSettingsToggleChangeListener);

            userPreferencePresenters.add(fingerPrintPreference);
            userPreferencePresenters.add(rememberMePreferencePresenter);
        } else {
            noFingerPrintPreference = new DefaultUserPreferencePresenter(R.string.user_prefs_fingerprint_title_92,
                    R.string.settings_security_finger_print_not_available_1163,
                    false,
                    R.drawable.ic_clear_white_24dp,
                    UserPreferencePresenter.Type.Fingerprint,
                    false,
                    false, null);

            userPreferencePresenters.add(noFingerPrintPreference);
            userPreferencePresenters.add(rememberMePreferencePresenter);
        }
    }

    private void setUpRecyclerView() {
        adapter = new GenericRecyclerViewAdapter<>(this,
                userPreferencePresenters,
                R.layout.preference_item,
                new UserPreferenceItemViewHolder.Factory(globalTintColor));


        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        ViewUtilities.addDividers(this, recyclerView, ViewUtilities.pxFromDp(72));
        recyclerView.setAdapter(adapter);
    }

    public void handleChangePasswordTapped(View view) {
        //no-op
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != userPreferencePresenters) {
            for (DefaultUserPreferencePresenter presenter : userPreferencePresenters) {
                presenter.onUserInterfaceAppeared();
            }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private UserPreferencePresenter.SettingsToggleChangeListener mSettingsToggleChangeListener = new UserPreferencePresenter.SettingsToggleChangeListener() {
        @Override
        public void onToggle(boolean enabled) {
            deviceSpecificPreferences.setFingerprint(enabled);
            fingerprintAlertDialogFragment.setSuccessFingerPrintAuth(false);

            rememberMePreferencePresenter.enableToggle(true);
            if(enabled){
                rememberMePreferencePresenter.enableToggle(false);
            }

            if (printUtil.fingerprintEnabled() && enabled) {
                showFingerPrintLoginDialog();
                rememberMePreferencePresenter.enableToggle(false);

            } else if (!printUtil.fingerprintEnabled() && enabled) {

                fingerPrintPreference.onToggle(false);

            }

            try {
                adapter.notifyDataSetChanged();
            }catch (IllegalStateException e){}

        }
    };

    public EventListener<FingerprintAlertDialogFragmentEvent> onFingerprintAlertDialogFragmentEvent = new EventListener<FingerprintAlertDialogFragmentEvent>() {
        @Override
        public void onEvent(FingerprintAlertDialogFragmentEvent event) {

            if (event.getDialogType() == FingerprintAlertDialogFragment.DialogType.FINGERPRINT_RECOGNITION
                    || event.getDialogType() == FingerprintAlertDialogFragment.DialogType.FINGERPRINT_RECOGNITION_LOGIN) {

                if (!fingerprintAlertDialogFragment.isSuccessFingerPrintAuth()) {
                    fingerPrintPreference.onToggle(false);
                    adapter.notifyDataSetChanged();
                } else {
                    showEnterPassword();
                }
                return;
            }

            if (event.getDialogType() == FingerprintAlertDialogFragment.DialogType.ENTER_PASSWORD) {
                if (event.getClickedButtonType() == FingerprintAlertDialogFragmentEvent.ClickedButtonType.POSITIVE) {
                    deviceSpecificPreferences.setFingerprint(true);
                    deviceSpecificPreferences.setRememberMe(true);

                    rememberMePreferencePresenter.enableToggle(false);
                    adapter.notifyDataSetChanged();
                } else {
                    fingerPrintPreference.onToggle(false);
                    rememberMePreferencePresenter.enableToggle(true);
                    adapter.notifyDataSetChanged();
                }
                return;
            }
        }
    };

    private void showEnterPassword() {
        String message = "\n" + getString(R.string.Settings_alert_password_required_message_942) + "\n";

        this.fingerprintAlertDialogFragment.create(
                this,
                FingerprintAlertDialogFragment.DialogType.ENTER_PASSWORD);
        this.fingerprintAlertDialogFragment.show(getSupportFragmentManager(), "FingerPrintDialog");
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void enablingFingerprint() {

        if (!printUtil.fingerprintEnabled()) {
            return;
        }

        if (printUtil.fingerprintEnabled()) {
            showFingerPrintLoginDialog();
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showFingerPrintLoginDialog() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED) {
            // If your app doesn't have this permission, then display the following text//

            this.fingerprintAlertDialogFragment.create(
                    this,
                    FingerprintAlertDialogFragment.DialogType.FINGERPRINT_RECOGNITION);

            printUtil.startAuthentication(this, this.fingerprintAlertDialogFragment);

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

    protected DefaultUserPreferencePresenter fingerprintPreferenceMarket(UserPreferencePresenter.SettingsToggleChangeListener listener) {
        return new DefaultUserPreferencePresenter(R.string.user_prefs_fingerprint_title_92,
                R.string.user_prefs_fingerprint_message_93,
                deviceSpecificPreferences.isFingerprint(),
                R.drawable.icn_fingerprint,
                UserPreferencePresenter.Type.Fingerprint,
                true,
                false,
                listener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        ServiceLocator.getInstance().eventDispatcher.removeEventListener(FingerprintAlertDialogFragmentEvent.class, onFingerprintAlertDialogFragmentEvent);
    }
}
