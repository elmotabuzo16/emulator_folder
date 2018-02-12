package com.vitalityactive.va.forgotpassword;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.register.entity.EmailAddress;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.utilities.ViewUtilities;

import javax.inject.Inject;

public class ForgotPasswordActivity extends BasePresentedActivity<ForgotPasswordPresenter.Ui, ForgotPasswordPresenter>
        implements EventListener<AlertDialogFragment.DismissedEvent>, ForgotPasswordPresenter.Ui {
    private static final String TAG = "ForgotPasswordActivity";
    private static final String FORGOT_PASSWORD_ALERT_NOT_REGISTERED = "FORGOT_PASSWORD_ALERT_NOT_REGISTERED";
    private static final String FORGOT_PASSWORD_ALERT_GENERIC_ERROR = "FORGOT_PASSWORD_ALERT_GENERIC_ERROR";
    private static final String FORGOT_PASSWORD_ALERT_SUCCESS = "FORGOT_PASSWORD_ALERT_SUCCESS";

    @Inject
    EventDispatcher eventDispatcher;
    @Inject
    ForgotPasswordPresenter presenter;

    private EmailAddress emailAddress;
    private TextInputLayout emailAddressLayout;
    private View view;
    private Button nextButton;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_forgot_password);
        setUpActionBarWithTitle(R.string.forgot_password_screen_title_52);

        emailAddress = new EmailAddress("");

        emailAddressLayout = (TextInputLayout) findViewById(R.id.forgot_password_email_layout);

        nextButton = (Button) findViewById(R.id.forgot_password_next_button);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_right_black_24dp);
        nextButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ViewUtilities.tintDrawable(drawable, ContextCompat.getColorStateList(this, R.color.chevron_tint)), null);
        nextButton.setEnabled(false);

        final ImageView emailIcon = (ImageView) findViewById(R.id.forgot_password_email_icon);

        final EditText editText = (EditText) findViewById(R.id.forgot_password_email);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailAddress.setText(s);
                if (emailAddress.isValid() || emailAddress.getText().length() == 0) {
                    hideInvalidEmailAddressMessage();
                }
                nextButton.setEnabled(emailAddress.getText().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (emailAddress.isValid() || emailAddress.getText().length() == 0) {
                        hideInvalidEmailAddressMessage();
                        emailIcon.setImageResource(R.drawable.email_inactive);
                    } else {
                        showInvalidEmailAddressMessage();
                    }
                } else {
                    emailIcon.setImageResource(R.drawable.email_active);
                }
            }
        });

        view = findViewById(R.id.forgot_password_screen);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int touchX = (int) (event.getX());
                    int touchY = (int) (event.getY());
                    boolean outsideEditTexts = isPointOutsideViewHitRect(touchX, touchY, editText);
                    if (outsideEditTexts) {
                        hideKeyboardAndFocusOnView(ForgotPasswordActivity.this.view);
                    }
                    return outsideEditTexts;
                }
                return false;
            }
        });
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
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected ForgotPasswordPresenter.Ui getUserInterface() {
        return this;
    }

    @Override
    protected ForgotPasswordPresenter getPresenter() {
        return presenter;
    }

    private boolean isPointOutsideViewHitRect(int touchX, int touchY, View view) {
        Rect hitRect = new Rect();
        view.getHitRect(hitRect);
        return !hitRect.contains(touchX, touchY);
    }

    @Override
    public void showInvalidEmailAddressMessage() {
        emailAddressLayout.setError(getString(R.string.registration_invalid_email_footnote_error_35));
    }

    @Override
    public void hideInvalidEmailAddressMessage() {
        emailAddressLayout.setErrorEnabled(false);
    }

    public void handleForwardButtonTapped(View view) {
        if (emailAddress.isValid()) {
            presenter.onForgotPasswordTapped(emailAddress.toString());
        } else {
            showInvalidEmailAddressMessage();
        }
    }

    @Override
    public void onForgotPasswordRequestSuccessful() {
        AlertDialogFragment alert = AlertDialogFragment.create(FORGOT_PASSWORD_ALERT_SUCCESS, getString(R.string.forgot_password_confirmation_screen_email_sent_message_title_58), getString(R.string.forgot_password_confirmation_screen_email_sent_message_59), null, null, getString(R.string.ok_button_title_40));
        alert.show(getSupportFragmentManager(), FORGOT_PASSWORD_ALERT_SUCCESS);
    }

    @Override
    public void onForgotPasswordRequestFailed() {
        Log.w(TAG, "forget password request failed (generic)");
        AlertDialogFragment alert = AlertDialogFragment.create(
                FORGOT_PASSWORD_ALERT_GENERIC_ERROR,
                getString(R.string.connectivity_error_alert_title_44),
                getString(R.string.connectivity_error_alert_message_45),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
        alert.show(getSupportFragmentManager(), FORGOT_PASSWORD_ALERT_GENERIC_ERROR);
    }

    @Override
    public void onEmailNotRegistered() {
        Log.w(TAG, "forget password request failed (user not registered)");
        AlertDialogFragment alert = AlertDialogFragment.create(
                FORGOT_PASSWORD_ALERT_NOT_REGISTERED,
                getString(R.string.forgot_password_email_not_registered_alert_title_56),
                getString(R.string.forgot_password_email_not_registered_alert_message_57),
                null, null,
                getString(R.string.ok_button_title_40));
        alert.show(getSupportFragmentManager(), FORGOT_PASSWORD_ALERT_NOT_REGISTERED);
    }

    @Override
    public void onRequestBusy() {
        this.showLoadingIndicator();
    }

    @Override
    public void onRequestCompleted() {
        this.hideLoadingIndicator();
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        if (eventIsOfType(event, FORGOT_PASSWORD_ALERT_SUCCESS)) {
            navigationCoordinator.navigateAfterForgotPasswordCompleted(this, emailAddress.toString());
        } else if (eventIsOfType(event, FORGOT_PASSWORD_ALERT_GENERIC_ERROR)) {
            if (event.getClickedButtonType() == AlertDialogFragment.DismissedEvent.ClickedButtonType.Positive) {
                presenter.onForgotPasswordTapped(emailAddress.toString());
            }
        }
    }
}
