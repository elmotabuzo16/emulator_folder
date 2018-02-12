package com.vitalityactive.va.register.view;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vitalityactive.va.BaseUkeActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.login.service.RegistrationFailure;
import com.vitalityactive.va.register.ActivationValues;
import com.vitalityactive.va.register.presenter.ActivateBaseCredentialPresenter;
import com.vitalityactive.va.register.presenter.AuthenticationCodePresenter;
import com.vitalityactive.va.register.presenter.DateOfBirthPresenter;
import com.vitalityactive.va.register.presenter.EntityNumberPresenter;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;

public class ActivateActivity extends BaseUkeActivity implements ActivateUserInterface {
    private ActivateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupActionBarButtonBar();
        setupFields();
        handleValidationError(getIntent().getStringExtra("validationError"));
    }

    private void handleValidationError(String validationError) {
        if (validationError == null)
            return;
        handleValidationError(RegistrationFailure.valueOf(validationError));
    }

    private void handleValidationError(RegistrationFailure registrationFailure) {
        int title;
        int message;
        switch (registrationFailure) {
            case INVALID_DATE_OF_BIRTH:
                title = R.string.uke_alert_date_of_birth_title_1052;
                message = R.string.uke_alert_date_of_birth_message_1053;
                break;
            case INVALID_ENTITY_NUMBER:
                title = R.string.uke_alert_incorrect_number_title_372;
                message = R.string.uke_alert_incorrect_number_message_373;
                break;
            case INVALID_REGISTRATION_CODE:
                title = R.string.uke_alert_incorrect_code_title_370;
                message = R.string.uke_alert_incorrect_code_message_371;
                break;
            default:
                return;
        }

        AlertDialogFragment.create(registrationFailure.toString(),
                getString(title), getString(message),
                null, null, getString(R.string.ok_button_title_40))
                .show(getSupportFragmentManager(), registrationFailure.toString());
    }

    private void setupActionBarButtonBar() {
        setActionBarTitleAndDisplayHomeAsUp(getString(R.string.UKE_Activate_activate_title_353));
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.close);
        }
        setupButtonBar(R.id.register_button_bar, R.id.register_button, 0)
                .setForwardButtonText(R.string.done_button_title_53);
    }

    private void setupFields() {
        RecyclerView recyclerView = findViewById(R.id.main_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = buildAdapters();
        adapter.loadValues(getActivationValues());
        recyclerView.setAdapter(adapter);
    }

    private ActivateAdapter buildAdapters() {
        ActivateBaseCredentialPresenter dateOfBirth = new DateOfBirthPresenter();
        ActivateBaseCredentialPresenter authenticationCode = new AuthenticationCodePresenter();
        ActivateBaseCredentialPresenter entityNumber = new EntityNumberPresenter();
        int black = ResourcesCompat.getColor(getResources(), android.R.color.black, getTheme());
        return new ActivateAdapter(this, this, dateOfBirth, authenticationCode, entityNumber, black);
    }

    public void handleRegisterButtonTapped(View view) {
        ActivationValues values = adapter.getActivationValues();
        ukeNavigationCoordinator.navigateAfterActivationValuesCaptured(this, getIntent().getData(), values);
    }

    @Override
    public void allowActivation() {
        enableRegistration(true);
    }

    @Override
    public void disallowActivation() {
        enableRegistration(false);
    }

    private ActivationValues getActivationValues() {
        return getIntent().getParcelableExtra(ActivationValues.class.getSimpleName());
    }

    private void enableRegistration(boolean enabled) {
        setupButtonBar(R.id.register_button_bar, R.id.register_button, 0)
                .setForwardButtonEnabled(enabled);
    }
}
