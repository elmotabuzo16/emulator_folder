package com.vitalityactive.va.settings;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.register.entity.Password;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.utilities.TextUtilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class SettingChangePasswordActivity extends BasePresentedActivity<SettingChangePasswordPresenter.UserInterface, SettingChangePasswordPresenter<SettingChangePasswordPresenter.UserInterface>> implements SettingChangePasswordPresenter.UserInterface, EventListener<AlertDialogFragment.DismissedEvent> {

    private static final String PASSWORD_PATTERN =
            "/^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{7,100}$/g";
    private static final String ERROR_CURRENT_PASSWORD_DIALOG_TAG = "INCORRECT_EXISTING_PASSWORD";
    private static final String CONFIRM_DIALOG_TAG = "ARE_YOU_SURE";



    private TextView currentPassword;
    private TextView newPassword;
    private TextView confirmPassword;
    private TextView notMatchWarning;
    private TextView warningNewPassword;

    private ImageView currentIcon;
    private ImageView confirmIcon;
    private ImageView newPasswordIcon;

    private Pattern pattern;
    private Matcher matcher;
    private MenuItem doneMenu;

    @Inject
    SettingChangePasswordPresenter settingChangePasswordPresenter;

    @Inject
    EventDispatcher eventDispatcher;

    @Inject
    DeviceSpecificPreferences deviceSpecificPreferences;

    @Inject
    AppConfigRepository appConfigRepository;

    private int globalTintColor, globalTintDarker;
    String currentPasswordValue;


    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);


        setUpActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            setActionBarTitle(getString(R.string.Settings_security_change_password_title_827));
            setCloseAsActionBarIcon();


        }

        pattern = Pattern.compile(PASSWORD_PATTERN);

        setUpView();
        addFocusListener();


        getDependencyInjector().inject(this);

        globalTintColor = Color.parseColor(appConfigRepository.getGlobalTintColorHex());
        globalTintDarker =  Color.parseColor(appConfigRepository.getGlobalTintDarkerColorHex());

        setActionBarColor(globalTintColor);
        setStatusBarColor(globalTintDarker);

    }


    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        navigationCoordinator.getPNSCaptureDependencyInjector().inject(this);
    }

    @Override
    protected SettingChangePasswordPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected SettingChangePasswordPresenter getPresenter() {
        return settingChangePasswordPresenter;
    }

    public void setUpView(){
        currentPassword = findViewById(R.id.current_password);
        newPassword = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.confirm_password);
        notMatchWarning = findViewById(R.id.not_match_warning);
        confirmIcon = findViewById(R.id.confirm_password_icon);
        newPasswordIcon = findViewById(R.id.new_password_icon);
        currentIcon = findViewById(R.id.current_password_icon);
        warningNewPassword = findViewById(R.id.warning_new_password);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_menu, menu);
        doneMenu = menu.findItem(R.id.action_menu);
        doneMenu.setEnabled(false);
        // return true so that the menu pop up is opened
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_menu:
                    submitChangedPassword();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void submitChangedPassword(){

       currentPasswordValue = currentPassword.getText().toString();
        String newPasswordValue = newPassword.getText().toString();
        String confirmPasswordValue = confirmPassword.getText().toString();

        presenter.attemptChangePassword(currentPasswordValue, newPasswordValue, confirmPasswordValue);

//        if(validate(newPasswordValue)) {
//            Drawable myIcon = ContextCompat.getDrawable(getActivity(), R.drawable.confirm_password_24);
//            newPasswordIcon.setImageDrawable(myIcon);
//            warningNewPassword.setText(getString(R.string.invalid_password));
//            warningNewPassword.setTextColor(ContextCompat.getColor(getActivity(), R.color.crimson_red));
//            return;
//        }
//
//        if(validate(confirmPasswordValue)) {
//            Drawable myIcon = ContextCompat.getDrawable(getActivity(), R.drawable.confirm_password_24);
//            confirmIcon.setImageDrawable(myIcon);
//            notMatchWarning.setText(getString(R.string.invalid_password));
//            notMatchWarning.setVisibility(View.VISIBLE);
//            return;
//        }
//
//
//        if(newPasswordValue.contentEquals(confirmPasswordValue)){
//            presenter.attemptChangePassword(currentPasswordValue, newPasswordValue, confirmPasswordValue);
//        } else {
//            Drawable myIcon = ContextCompat.getDrawable(getActivity(), R.drawable.confirm_password_24);
//            confirmIcon.setImageDrawable(myIcon);
//            notMatchWarning.setVisibility(View.VISIBLE);
//        }

    }

    public void addFocusListener(){
        currentPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(v.getId() == R.id.current_password) {
                CharSequence text = ((TextView) v).getText();
                if (hasFocus || !TextUtilities.isNullOrWhitespace(text)) {
                    Password currentPass = new Password("");
                    currentIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.password_active));
                    if(!currentPass.isPatternValid(currentPassword.getText().toString())){
                        showIncorrectNewPasswordValidation();
                    } else {
                        if(currentPassword.getText().length() > 0){
                            doneMenu.setEnabled(true);
                        }
                    }
                } else {
                    currentIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.password_inactive));
                    doneMenu.setEnabled(false);
                }
            }
        });

        newPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(v.getId() == R.id.new_password) {
                CharSequence text = ((TextView) v).getText();
                if (hasFocus || !TextUtilities.isNullOrWhitespace(text)) {
                    newPasswordIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.password_active));
                    warningNewPassword.setText(getString(R.string.Settings_change_password_2_placeholder_998));
                    warningNewPassword.setTextColor(ContextCompat.getColor(SettingChangePasswordActivity.this, R.color.light_disabled_38));
                    if(newPassword.getText().length() > 0){
                        doneMenu.setEnabled(true);
                    }
                } else {
                    newPasswordIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.password_inactive));
                    doneMenu.setEnabled(false);
                }
            }
        });

        confirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(v.getId() == R.id.confirm_password) {
                CharSequence text = ((TextView) v).getText();
                if (hasFocus || !TextUtilities.isNullOrWhitespace(text)) {
                    confirmIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.confirm_active));
                    notMatchWarning.setVisibility(View.GONE);

                    if(confirmPassword.getText().length() > 0){
                        doneMenu.setEnabled(true);
                    }
                } else {
                    confirmIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.confirm_inactive));
                        doneMenu.setEnabled(false);
                }
            }
        });

    }

    public void resetStateNewPassword(){
        Drawable myIcon = ContextCompat.getDrawable(this, R.drawable.password_active);
        newPasswordIcon.setImageDrawable(myIcon);
        warningNewPassword.setText(getString(R.string.Settings_change_password_2_placeholder_998));
    }

    public void resetStateConfirm(){
        Drawable myIcon = ContextCompat.getDrawable(this, R.drawable.password_active);
        confirmIcon.setImageDrawable(myIcon);
        notMatchWarning.setVisibility(View.GONE);
    }

    public boolean validate(final String password){
        matcher = pattern.matcher(password);
        return matcher.matches();

    }

    public void savePassword(String password){
        deviceSpecificPreferences.setRememberedPassword(password);
    }

    public void showChangePasswordConfirmation(){
        navigationCoordinator.navigateToChangePasswordActvity(SettingChangePasswordActivity.this);
    }

    public void showIncorrectCurrentPasswordError() {
        
        boolean hasInvalidcase = !currentPasswordValue.equals(currentPasswordValue.toLowerCase()) &&
                !currentPasswordValue.equals(currentPasswordValue.toUpperCase()) &&
                currentPasswordValue.length() >= 7;
        boolean hasSpecial   = !currentPasswordValue.matches("[A-Za-z0-9]*");

        if (!hasInvalidcase) {
            AlertDialogFragment alert = AlertDialogFragment.create(
                    ERROR_CURRENT_PASSWORD_DIALOG_TAG,
                    getResources().getString(R.string.Settings_alert_unable_to_change_password_title_1162),
                    getResources().getString(R.string.registration_password_field_footnote_29),
                    null,
                    null,
                    getResources().getString(R.string.profile_change_email_ok));
            alert.show(getSupportFragmentManager(), ERROR_CURRENT_PASSWORD_DIALOG_TAG);

        }else if (hasSpecial) {
            AlertDialogFragment alert = AlertDialogFragment.create(
                    ERROR_CURRENT_PASSWORD_DIALOG_TAG,
                    getResources().getString(R.string.Settings_alert_unable_to_change_password_title_1162),
                    getResources().getString(R.string.settings_no_special_characters_validation_message_1166),
                    null,
                    null,
                    getResources().getString(R.string.profile_change_email_ok));
            alert.show(getSupportFragmentManager(), ERROR_CURRENT_PASSWORD_DIALOG_TAG);

        }else{
            AlertDialogFragment alert = AlertDialogFragment.create(
                    ERROR_CURRENT_PASSWORD_DIALOG_TAG,
                    getResources().getString(R.string.Settings_alert_unable_to_change_password_title_1162),
                    getResources().getString(R.string.Settings_alert_change_password_incorrect_message_9999),
                    null,
                    null,
                    getResources().getString(R.string.profile_change_email_ok));
            alert.show(getSupportFragmentManager(), ERROR_CURRENT_PASSWORD_DIALOG_TAG);

        }

    }

    @Override
    public void showIncorrectNewPasswordValidation() {

        Drawable myIcon = ContextCompat.getDrawable(this, R.drawable.confirm_password_24);
        newPasswordIcon.setImageDrawable(myIcon);
        warningNewPassword.setText(getString(R.string.registration_password_field_footnote_29));
        warningNewPassword.setTextColor(ContextCompat.getColor(this, R.color.crimson_red));
    }

    @Override
    public void showIncorrectConfirmValidation() {

        Drawable myIcon = ContextCompat.getDrawable(this, R.drawable.confirm_password_24);
        confirmIcon.setImageDrawable(myIcon);
        notMatchWarning.setText(getString(R.string.registration_password_field_footnote_29));
        notMatchWarning.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSpecialCharacterValidation() {
        Drawable myIcon = ContextCompat.getDrawable(this, R.drawable.confirm_password_24);
        confirmIcon.setImageDrawable(myIcon);
        notMatchWarning.setText(getString(R.string.settings_no_special_characters_validation_message_1166));
        notMatchWarning.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideNotMatchWarning(){
        notMatchWarning.setVisibility(View.INVISIBLE);
    }


    @Override
    public void showPasswordNotEqual() {
        Drawable myIcon = ContextCompat.getDrawable(this, R.drawable.confirm_password_24);
        confirmIcon.setImageDrawable(myIcon);
        notMatchWarning.setText(getString(R.string.registration_mismatched_password_footnote_error_36));
        notMatchWarning.setVisibility(View.VISIBLE);
    }

    @Override
    protected void resume() {
        super.resume();
        eventDispatcher.addEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    @Override
    protected void pause() {
        super.pause();
        eventDispatcher.removeEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    @Override
    public void showLoadingIndicator() {
        super.showLoadingIndicator();
    }

    @Override
    public void hideLoadingIndicator() {
        super.hideLoadingIndicator();
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {

    }
}
