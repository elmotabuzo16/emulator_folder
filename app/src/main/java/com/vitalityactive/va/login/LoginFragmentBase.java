package com.vitalityactive.va.login;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedFragment;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.NavigationCoordinator;
import com.vitalityactive.va.R;
import com.vitalityactive.va.ServiceLocator;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.uicomponents.LoadingIndicatorFragment;
import com.vitalityactive.va.utilities.FingerprintUtility;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.VAFingerprintManager;
import com.vitalityactive.va.utilities.ViewUtilities;

import javax.inject.Inject;

public abstract class LoginFragmentBase extends BasePresentedFragment<LoginPresenter.UserInterface, LoginPresenter> implements LoginPresenter.UserInterface, EventListener<AlertDialogFragment.DismissedEvent> {
    private static final int EMAIL_ADDRESS = 0;
    private static final int PASSWORD = 1;
    private static final String GENERIC_LOGIN_ERROR_ALERT = "GENERIC_LOGIN_ERROR_ALERT";
    private static final String INVALID_CREDENTIALS_LOGIN_ERROR_ALERT = "INVALID_CREDENTIALS_LOGIN_ERROR_ALERT";
    private static final String INVALID_FINGERPRINT_LOGIN_ERROR = "INVALID_FINGERPRINT_LOGIN_ERROR_ALERT";
    private static final String LOADING_INDICATOR = "LOADING_INDICATOR";
    private static final String LOCKED_ACCOUNT_LOGIN_ERROR_ALERT = "LOCKED_ACCOUNT_LOGIN_ERROR_ALERT";

    @Inject
    NavigationCoordinator navigationCoordinator;
    @Inject
    LoginPresenter loginPresenter;
    @Inject
    DeviceSpecificPreferences deviceSpecificPreferences;

    protected FingerprintUtility printUtil;

    private RelativeLayout rlLoginContainer;
    private EditText emailAddress;
    private EditText password;
    private Button loginButton;
    private Button btnRegister;
    private TextInputLayout emailAddressLayout;
    private ImageView emailIcon;
    private ImageView imgFingerprint;

    private FingerprintAlertDialogFragment fingerprintAlertDialogFragment = new FingerprintAlertDialogFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    protected void processLoginClick() {
        if (fingerprintEnabled()) {
            enablingFingerprint();
        } else {
            getPresenter().onUserTriesToLogIn();
        }
    }

    @Override
    protected void activityCreated(@Nullable Bundle savedInstanceState) {
        if (getView() == null) {
            return;
        }

        ServiceLocator.getInstance().eventDispatcher.addEventListener(FingerprintAlertDialogFragmentEvent.class, onFingerprintAlertDialogFragmentEvent);


        printUtil = new FingerprintUtility();

        rlLoginContainer = getView().findViewById(R.id.login_container);
        emailAddress = getView().findViewById(R.id.login_email_address);
        emailAddressLayout = getView().findViewById(R.id.login_email_layout);
        password = getView().findViewById(R.id.login_password);
        TextInputLayout passwordLayout = getView().findViewById(R.id.login_password_layout);
        loginButton = getView().findViewById(R.id.login_button);
        btnRegister = getView().findViewById(R.id.login_register_button);
        emailIcon = getView().findViewById(R.id.login_email_icon);
        imgFingerprint = getView().findViewById(R.id.imgFingerprint);

        loginButton.setEnabled(getPresenter().getViewModel().isLoginEnabled());
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLoginClick();
            }
        });

        imgFingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFingerprintLogin();
            }
        });

        passwordLayout.setPasswordVisibilityToggleDrawable(R.drawable.password_visible_toggle_not_focused);

        setFocusChangeListener(emailAddressLayout, R.drawable.email_active, R.drawable.email_inactive, emailIcon, EMAIL_ADDRESS);
        setFocusChangeListener(passwordLayout, R.drawable.password_active, R.drawable.password_inactive, (ImageView) getView().findViewById(R.id.login_password_icon), PASSWORD);

        getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int touchX = (int) (event.getX());
                    int touchY = (int) (event.getY());
                    boolean outsideEditTexts = isPointOutsideViewHitRect(touchX, touchY, emailAddress) && isPointOutsideViewHitRect(touchX, touchY, password);
                    if (outsideEditTexts) {
                        ViewUtilities.hideKeyboard(getActivity(), getView());
                    }
                    return outsideEditTexts;
                }
                return false;
            }
        });

        addTextChangedListener(emailAddress, EMAIL_ADDRESS);
        addTextChangedListener(password, PASSWORD);

        //noinspection ConstantConditions
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button btn = (Button) v;

                if (btn.getText().toString().equals(getString(R.string.login_remember_me_not_you_button_title_60).toUpperCase())) {

                    clearUsername();
                    btnRegister.setText(getString(R.string.login_register_button_title_23));
                    imgFingerprint.setVisibility(View.GONE);
                } else {
                    String username = emailAddress.getText().toString();
                    navigationCoordinator.navigateFromLoginAfterRegisterTapped(getActivity(), username);
                }
            }
        });

        checkFingerPrintHasChanges();
    }

    public void checkFingerPrintHasChanges(){
        if(deviceSpecificPreferences.isRememberMeOn() && !deviceSpecificPreferences.getRememberedUsername().equals("")) {
            checkForFingerPrint();
        }
    }

    protected abstract void checkForFingerPrint();

    protected void showFingerprintLogin() {
        if (fingerprintEnabled()) {
            showEnrollFingerPrintDialog();
        }
    }

    protected boolean fingerprintEnabled() {
        return printUtil.fingerprintEnabled();
    }

    protected boolean hasFingerprintChanges() {
        return printUtil.isFingerprintHasChanged(deviceSpecificPreferences);
    }


    protected void showAuthenticationUponLogin() {

        this.fingerprintAlertDialogFragment.create(
                getActivity(),
                FingerprintAlertDialogFragment.DialogType.SINGLE_CHECKBOX);

       if (isVisible() && !isStateSaved()) {
            this.fingerprintAlertDialogFragment.show(getActivity().getSupportFragmentManager(), "FingerprintAlertDialogFragment");
        }
    }


    protected void enablingFingerprint() {

        if (!fingerprintEnabled()) {
            return;
        }

        if (!deviceSpecificPreferences.isCurrentUserHasSeenLoginFingerprintEnrollment()) {
            showEnrollFingerPrintDialog();
        } else {
            getPresenter().onUserTriesToLogIn();
        }

    }

    @Override
    public void showEnrollFingerPrintDialog() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED) {

            FingerprintAlertDialogFragment.DialogType dialogType = FingerprintAlertDialogFragment.DialogType.FINGERPRINT_RECOGNITION_LOGIN;
            if (!deviceSpecificPreferences.isFingerprint()) {
                dialogType = FingerprintAlertDialogFragment.DialogType.USE_PASSWORD;
            }

            this.fingerprintAlertDialogFragment.create(
                    getActivity(),
                    dialogType
            );

            printUtil.startAuthentication(getActivity(), this.fingerprintAlertDialogFragment);
        }

        this.fingerprintAlertDialogFragment.show(getActivity().getSupportFragmentManager(), "FingerPrintDialog");
    }


    private void addTextChangedListener(EditText editText, final int viewId) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CharSequence text = s == null ? "" : s;
                if (viewId == EMAIL_ADDRESS) {
                    getPresenter().onUsernameChanged(text);
                } else {
                    getPresenter().onPasswordChanged(text);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setFocusChangeListener(final TextInputLayout layout, final int activeResourceId, final int inactiveResourceId, final ImageView imageView, final int viewId) {
        View view = viewId == PASSWORD ? layout.findViewById(R.id.login_password) : layout.findViewById(R.id.login_email_address);

        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CharSequence text = ((TextView) v).getText();
                int iconResourceId;

                int passwordToggleIconResourceId;


                if (!hasFocus && viewId == EMAIL_ADDRESS) {

                    emailAddressLayout.setHint(getString(R.string.registration_email_field_title_26));
                    if (!TextUtilities.isNullOrWhitespace(text)) {
                        emailAddressLayout.setHint(getString(R.string.login_remember_me_enter_password_title_61));
                    }
                }


                if (hasFocus || !TextUtilities.isNullOrWhitespace(text)) {
                    iconResourceId = activeResourceId;
                    passwordToggleIconResourceId = R.drawable.password_visible_toggle_focused;
                } else {
                    iconResourceId = inactiveResourceId;
                    passwordToggleIconResourceId = R.drawable.password_visible_toggle_not_focused;
                }

                imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), iconResourceId));
                layout.setPasswordVisibilityToggleDrawable(passwordToggleIconResourceId);

                if (!hasFocus && viewId == EMAIL_ADDRESS) {
                    getPresenter().onUsernameEntered();
                }
            }
        });
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected LoginPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected LoginPresenter getPresenter() {
        return loginPresenter;
    }

    @Override
    protected void appear() {
        ServiceLocator.getInstance().eventDispatcher.addEventListener(AlertDialogFragment.DismissedEvent.class, this);
        setUsernameAfterForgotPassword();
        updateUsername();
        updatePassword();
    }

    @Override
    protected void disappear() {
        ServiceLocator.getInstance().eventDispatcher.removeEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    private void setUsernameAfterForgotPassword() {
        String usernameFromIntent = getActivity().getIntent().getStringExtra(LoginActivity.EXTRA_LOGIN_AFTER_FORGOT_PASSWORD_USERNAME);
        if (usernameFromIntent != null && !usernameFromIntent.isEmpty()) {
            getPresenter().setUsernameFromIntent(usernameFromIntent);
        }
    }

    private void updatePassword() {
        password.setText(getPresenter().getViewModel().getPassword());
    }

    private void updateUsername() {
        emailAddressLayout.setHintAnimationEnabled(false);
        emailAddress.setText(getPresenter().getViewModel().getUsername());

        if (!emailAddress.getText().toString().isEmpty()) {
            emailAddressLayout.setHint(getString(R.string.login_remember_me_enter_password_title_61));
        }

        emailAddressLayout.setHintAnimationEnabled(true);

        if (!TextUtilities.isNullOrWhitespace(emailAddress.getText())) {
            emailIcon.setImageResource(R.drawable.email_active);
        }
    }

    private void clearUsername() {
        emailAddress.setText("");
        emailAddressLayout.setHint(getString(R.string.registration_email_field_title_26));
    }

    private boolean isPointOutsideViewHitRect(int touchX, int touchY, View view) {
        Rect hitRect = new Rect();
        view.getHitRect(hitRect);
        return !hitRect.contains(touchX, touchY);
    }

    @Override
    public void updateLoginEnabled(boolean isLoginEnabled) {
        loginButton.setEnabled(isLoginEnabled);
    }

    @Override
    public void showInvalidEmailAddressMessage() {
        emailAddressLayout.setError(getString(R.string.registration_invalid_email_footnote_error_35));
    }

    @Override
    public void hideInvalidUsernameMessage() {
        emailAddressLayout.setErrorEnabled(false);
    }

    @Override
    public void showLoadingIndicator() {
        if (getView() != null) {
            if (getFragmentManager().findFragmentByTag(LOADING_INDICATOR) != null) {
                return;
            }
            ViewUtilities.hideKeyboard(getActivity(), getView());
            new LoadingIndicatorFragment().show(getFragmentManager(), LOADING_INDICATOR);
        }
    }

    @Override
    public void hideLoadingIndicator() {
        if (getView() != null) {
            LoadingIndicatorFragment loadingIndicatorFragment = (LoadingIndicatorFragment) getFragmentManager().findFragmentByTag(LOADING_INDICATOR);
            if (loadingIndicatorFragment != null) {
                loadingIndicatorFragment.dismiss();
            }
        }
    }

    @Override
    public void showInvalidCredentialsLoginErrorMessage() {
        showInvalidCredentialsLoginErrorMessage(null);
    }

    @Override
    public void showGenericLoginErrorMessage() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                GENERIC_LOGIN_ERROR_ALERT,
                getString(R.string.login_incorrect_email_password_error_title_47),
                getString(R.string.login_error_generic_message_9999),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
        alert.show(getFragmentManager(), GENERIC_LOGIN_ERROR_ALERT);
    }

    @Override
    public void showGenericFingerPrintErrorMessage() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                INVALID_FINGERPRINT_LOGIN_ERROR,
                getString(R.string.Settings_alert_fingerprint_not_recognised_title_940),
                getString(R.string.Settings_alert_fingerprint_not_recognised_message_941),
                getString(R.string.ok_button_title_40),
                null,
                null);
        alert.show(getFragmentManager(), INVALID_FINGERPRINT_LOGIN_ERROR);
    }


    @Override
    public void navigateAfterSuccessfulLogin() {
        navigationCoordinator.navigateAfterSuccessfulLogin(getActivity());
    }

    @Override
    public void navigatetoscreening() {
        navigationCoordinator.navigateToSnvLearnMore(getActivity());
    }

    @Override
    public void showConnectionErrorMessage() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                GENERIC_LOGIN_ERROR_ALERT,
                getString(R.string.connectivity_error_alert_title_44),
                getString(R.string.connectivity_error_alert_message_45),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
        alert.show(getFragmentManager(), GENERIC_LOGIN_ERROR_ALERT);
    }

    @Override
    public void showInvalidCredentialsLoginErrorMessageWithForgotPassword() {
        String forgotPasswordText = getString(R.string.login_forgot_password_button_title_22);
        showInvalidCredentialsLoginErrorMessage(forgotPasswordText);
    }

    @Override
    public void showLockedAccountLoginErrorMessage() {
        AlertDialogFragment.create(
                LOCKED_ACCOUNT_LOGIN_ERROR_ALERT,
                getString(R.string.login_account_locked_alert_title_737),
                getString(R.string.login_account_locked_error_alert_message_738),
                null,
                null,
                getString(R.string.ok_button_title_40))
                .show(getFragmentManager(), LOCKED_ACCOUNT_LOGIN_ERROR_ALERT);
    }

    private void showInvalidCredentialsLoginErrorMessage(String forgotPasswordText) {
        AlertDialogFragment alert = AlertDialogFragment.create(
                INVALID_CREDENTIALS_LOGIN_ERROR_ALERT,
                getString(R.string.login_incorrect_email_password_error_title_47),
                getString(R.string.login_incorrect_email_password_error_message_48),
                forgotPasswordText,
                null,
                getString(R.string.ok_button_title_40));
        alert.show(getFragmentManager(), INVALID_CREDENTIALS_LOGIN_ERROR_ALERT);
    }

    protected void regenerateFingerprintKeys(){
        printUtil.regenerateFingerprintKeys(deviceSpecificPreferences);
    }

    public EventListener<FingerprintAlertDialogFragmentEvent> onFingerprintAlertDialogFragmentEvent = new EventListener<FingerprintAlertDialogFragmentEvent>() {
        @Override
        public void onEvent(FingerprintAlertDialogFragmentEvent event) {

            if (event.getDialogType() == FingerprintAlertDialogFragment.DialogType.SINGLE_CHECKBOX &&
                    event.getClickedButtonType() == FingerprintAlertDialogFragmentEvent.ClickedButtonType.POSITIVE) {

                btnRegister.setText(getString(R.string.login_email_not_registered_error_alert_button_340).toUpperCase());

                regenerateFingerprintKeys();

                if (!event.isCheck()) {
                    showFingerprintDisabled();
                } else {
                    deviceSpecificPreferences.setFingerprint(true);
                }
                return;
            }

            if (event.getDialogType() == FingerprintAlertDialogFragment.DialogType.USE_PASSWORD) {
                deviceSpecificPreferences.setCurrentUserHasSeenLoginFingerprintEnrollment();
                if (fingerprintAlertDialogFragment.isSuccessFingerPrintAuth()) {
                    deviceSpecificPreferences.setFingerprint(true);
                    getPresenter().onUserTriesToLogIn();

                } else {
                    getPresenter().onUserTriesToLogIn();
                }
                return;
            }

            if (event.getDialogType() == FingerprintAlertDialogFragment.DialogType.FINGERPRINT_RECOGNITION_LOGIN) {

                if (fingerprintAlertDialogFragment.isSuccessFingerPrintAuth()) {

                    StringBuilder emailWithPrefix = new StringBuilder();
                    String enviPrefix = deviceSpecificPreferences.getCurrentEnvironmentPrefix();

                    if (!TextUtils.isEmpty(enviPrefix)) {
                        emailWithPrefix.append(enviPrefix);
                    }
                    emailWithPrefix.append(deviceSpecificPreferences.getRememberedUsername());

                    if (!TextUtils.isEmpty(deviceSpecificPreferences.getRememberedPassword())) {
                        password.setText(deviceSpecificPreferences.getRememberedPassword());
                    }

                    if (!TextUtils.isEmpty(deviceSpecificPreferences.getRememberedUsername())) {
                        emailAddress.setText(emailWithPrefix);
                    }

                    if (!TextUtils.isEmpty(password.getText()) && !TextUtils.isEmpty(emailAddress.getText())) {
                        getPresenter().onUserTriesToLogIn();
                        deviceSpecificPreferences.setFingerprint(true);
                        deviceSpecificPreferences.setRememberMe(true);
                    }
                }

                if (!fingerprintAlertDialogFragment.isSuccessFingerPrintAuth() && fingerprintAlertDialogFragment.hasFingerprintFailed()) {
                    btnRegister.setText(getString(R.string.login_remember_me_not_you_button_title_60).toUpperCase());

                    if (deviceSpecificPreferences.isFingerprint()) {
                        imgFingerprint.setVisibility(View.VISIBLE);
                    }

                    showGenericFingerPrintErrorMessage();
                }
                return;

            }

            if (event.getDialogType() == FingerprintAlertDialogFragment.DialogType.ENTER_PASSWORD &&
                    event.getClickedButtonType() == FingerprintAlertDialogFragmentEvent.ClickedButtonType.POSITIVE) {

                deviceSpecificPreferences.setFingerprint(true);
                getPresenter().onUserTriesToLogIn();

                return;
            }
        }
    };

    private void showFingerprintDisabled() {
        Snackbar snackbar = Snackbar
                .make(rlLoginContainer, getString(R.string.Settings_fingerprint_disabled_message_9999), Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    @Override
    public void deleteProfilePhoto() {
        getVitalityActiveApplication().removeProfileImage();
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {

        if (event.getType().equals(GENERIC_LOGIN_ERROR_ALERT) && event.getClickedButtonType() == AlertDialogFragment.DismissedEvent.ClickedButtonType.Positive) {
            getPresenter().onUserTriesToLogIn();
        } else if (event.getType().equals(INVALID_CREDENTIALS_LOGIN_ERROR_ALERT) && event.getClickedButtonType() == AlertDialogFragment.DismissedEvent.ClickedButtonType.Negative) {
            navigationCoordinator.navigateFromLoginAfterForgotPasswordTapped(getActivity());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ServiceLocator.getInstance().eventDispatcher.removeEventListener(FingerprintAlertDialogFragmentEvent.class, onFingerprintAlertDialogFragmentEvent);
    }
}