package com.vitalityactive.va.register.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.register.RegistrationPresenter;
import com.vitalityactive.va.register.presenter.CredentialPresenter;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.uicomponents.LoadingIndicatorFragment;
import com.vitalityactive.va.uicomponents.adapters.CompositeRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class RegisterActivity extends BasePresentedActivity<RegistrationUserInterface, RegistrationPresenter> implements GenericRecyclerViewAdapter.IViewHolderFactory<CredentialPresenter, RegistrationCellViewHolder>, RegistrationUserInterface, EventListener<AlertDialogFragment.DismissedEvent> {
    public static final String LOADING_INDICATOR = "LOADING_INDICATOR";
    private static final String REGISTRATION_TRY_AGAIN_ERROR_ALERT = "REGISTRATION_TRY_AGAIN_ERROR_ALERT";
    private static final String REGISTRATION_OK_ERROR_ALERT = "REGISTRATION_OK_ERROR_ALERT";
    public static final String EXTRA_USERNAME = "EXTRA_USERNAME_REGISTER";
    private static final int USERNAME_FIELD_INDEX = 0;
    @Inject
    RegistrationPresenter presenter;
    @Inject
    List<CredentialPresenter> credentialPresenters;
    @Inject
    EventDispatcher eventDispatcher;
    private Button registerButton;
    private View rootView;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);

        setUpActionBarWithTitle(R.string.registration_screen_title_25);

        setUpRecyclerView();

        rootView = findViewById(R.id.activity_register);

        registerButton = (Button) findViewById(R.id.register_button);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_right_black_24dp);
        registerButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ViewUtilities.tintDrawable(drawable, ContextCompat.getColorStateList(this, R.color.chevron_tint)), null);
        registerButton.setEnabled(false);

        loadFieldsFromValuesEnteredInLogin();
    }

    private void loadFieldsFromValuesEnteredInLogin() {
        String username = getIntent().getStringExtra(EXTRA_USERNAME);
        credentialPresenters.get(USERNAME_FIELD_INDEX).onValueChanged(username);
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        for (CredentialPresenter cp : credentialPresenters) {
            cp.setRegistrationUserInterface(this);
        }

        GenericRecyclerViewAdapter<CredentialPresenter, RegistrationCellViewHolder> fieldsAdapter =
                new GenericRecyclerViewAdapter<>(this, credentialPresenters, R.layout.registration_list_item, this);

        //noinspection AndroidLintUseSparseArrays
        HashMap<Integer, GenericRecyclerViewAdapter> adapters = new HashMap<>();
        adapters.put(R.layout.registration_list_item, fieldsAdapter);
        GenericRecyclerViewAdapter<String, SimpleTextViewHolder> footerAdapter = new GenericRecyclerViewAdapter<>(this, Collections.singletonList(getString(R.string.registration_registration_code_field_footnote_34)), R.layout.registration_footer, new SimpleTextViewHolder.Factory());
        adapters.put(R.layout.registration_footer, footerAdapter);

        CompositeRecyclerViewAdapter adapter = new CompositeRecyclerViewAdapter(adapters, new int[]{R.layout.registration_list_item, R.layout.registration_list_item, R.layout.registration_list_item, R.layout.registration_list_item, R.layout.registration_footer});

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected RegistrationUserInterface getUserInterface() {
        return this;
    }

    @Override
    protected RegistrationPresenter getPresenter() {
        return presenter;
    }

    @Override
    public RegistrationCellViewHolder createViewHolder(View itemView) {
        return new RegistrationCellViewHolder(itemView);
    }

    public void handleRegisterButtonTapped(View view) {
        presenter.onUserTriesToRegister();
    }

    @Override
    public void allowRegistration() {
        registerButton.setEnabled(true);
    }

    @Override
    public void disallowRegistration() {
        registerButton.setEnabled(false);
    }

    @Override
    public void showLoadingIndicator() {
        if (getSupportFragmentManager().findFragmentByTag(LOADING_INDICATOR) != null) {
            return;
        }
        ViewUtilities.hideKeyboard(this, rootView);
        new LoadingIndicatorFragment().show(getSupportFragmentManager(), LOADING_INDICATOR);
    }

    @Override
    public void hideLoadingIndicator() {
        LoadingIndicatorFragment loadingIndicatorFragment = (LoadingIndicatorFragment) getSupportFragmentManager().findFragmentByTag(LOADING_INDICATOR);
        if (loadingIndicatorFragment != null) {
            loadingIndicatorFragment.dismiss();
        }
    }

    @Override
    public void navigateAfterSuccessfulRegistration() {
        navigationCoordinator.navigateAfterSuccessfulRegistration(this);
    }

    @Override
    public void showConnectionErrorMessage() {
        if (isShowingErrorAlert()) {
            return;
        }

        AlertDialogFragment.create(REGISTRATION_TRY_AGAIN_ERROR_ALERT,
                getString(R.string.connectivity_error_alert_title_44),
                getString(R.string.connectivity_error_alert_message_45),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43))
                .show(getSupportFragmentManager(), REGISTRATION_TRY_AGAIN_ERROR_ALERT);
    }

    @Override
    public void showGenericErrorMessage() {
        if (isShowingErrorAlert()) {
            return;
        }

        AlertDialogFragment.create(REGISTRATION_TRY_AGAIN_ERROR_ALERT,
                getString(R.string.registration_unable_to_register_alert_title_41),
                getString(R.string.registration_unable_to_register_alert_message_42),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43))
                .show(getSupportFragmentManager(), REGISTRATION_TRY_AGAIN_ERROR_ALERT);
    }

    private boolean isShowingErrorAlert() {
        return isShowingErrorAlert(REGISTRATION_TRY_AGAIN_ERROR_ALERT) || isShowingErrorAlert(REGISTRATION_OK_ERROR_ALERT);
    }

    private boolean isShowingErrorAlert(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag) != null;
    }

    @Override
    public void showInvalidEmailOrInsurerCodeErrorMessage() {
        if (isShowingErrorAlert()) {
            return;
        }

        AlertDialogFragment.create(REGISTRATION_OK_ERROR_ALERT,
                getString(R.string.registration_invalid_email_or_registration_code_alert_title_38),
                getString(R.string.registration_invalid_email_or_registration_code_alert_message_39),
                null,
                null,
                getString(R.string.ok_button_title_40))
                .show(getSupportFragmentManager(), REGISTRATION_OK_ERROR_ALERT);
    }

    @Override
    public void showAlreadyRegisteredErrorMessage() {
        if (isShowingErrorAlert()) {
            return;
        }

        AlertDialogFragment.create(REGISTRATION_OK_ERROR_ALERT,
                getString(R.string.registration_unable_to_register_alert_title_41),
                getString(R.string.registration_unable_to_register_alert_message_46),
                null,
                null,
                getString(R.string.ok_button_title_40))
                .show(getSupportFragmentManager(), REGISTRATION_OK_ERROR_ALERT);
    }

    @Override
    public void setUsername(CharSequence username) {

    }

    @Override
    protected void resume() {
        eventDispatcher.addEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    @Override
    protected void pause() {
        eventDispatcher.removeEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        if (eventIsOfType(event, REGISTRATION_TRY_AGAIN_ERROR_ALERT) && buttonTypeTapped(event, AlertDialogFragment.DismissedEvent.ClickedButtonType.Positive)) {
            presenter.onUserTriesToRegister();
        } else {
            presenter.onUserDismissesErrorMessage();
        }
    }
}
