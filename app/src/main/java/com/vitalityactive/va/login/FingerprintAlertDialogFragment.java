package com.vitalityactive.va.login;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ajalt.reprint.core.Reprint;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.R;
import com.vitalityactive.va.ServiceLocator;
import com.vitalityactive.va.VitalityActiveApplication;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.uicomponents.FingerPrintConfirmation;
import com.vitalityactive.va.utilities.ViewUtilities;

import javax.inject.Inject;

/**
 * Created by jayellos on 12/4/17.
 */

public class FingerprintAlertDialogFragment extends DialogFragment implements FingerPrintConfirmation {
    private final String KEY_TITLE = "KEY_TITLE";
    private final String KEY_MESSAGE = "KEY_MESSAGE";
    private final String KEY_POSITIVE_BTN_TEXT = "KEY_POSITIVE_BTN_TEXT";
    private final String KEY_NEGATIVE_BTN_TEXT = "KEY_NEGATIVE_BTN_TEXT";
    private final String KEY_EMAIL = "KEY_EMAIL";

    public enum DialogType {
        FINGERPRINT_RECOGNITION,
        SINGLE_CHECKBOX,
        EMAIL_PASSWORD,
        EMAIL_PASSWORD_WITH_CHECKBOX,
        USE_PASSWORD,
        ENTER_PASSWORD,
        FINGERPRINT_RECOGNITION_LOGIN
    }

    @Inject
    AppConfigRepository appConfigRepository;
    @Inject
    DeviceSpecificPreferences deviceSpecificPreferences;

    private LinearLayout llEmailWrapper;
    private TextInputLayout tilEmail;
    private TextInputEditText tieEmail;
    private TextInputEditText tiePassword;
    private TextView tvFingerPrint;

    private LinearLayout llCheckBoxWrapper;
    private CheckBox checkBox;

    private DialogType dialogType = DialogType.SINGLE_CHECKBOX;
    private int globalTintColor = -1;


    private View parentView;
    private boolean successFingerPrintAuth;
    private int fingerprintFailedCounter;

    public void create(DialogType dialogType, String title, String message,
                       String positiveBtnText, String negativeBtnText,
                       String email) {
        Bundle arguments = new Bundle();
        arguments.putString(KEY_TITLE, title);
        arguments.putString(KEY_MESSAGE, message);
        arguments.putString(KEY_POSITIVE_BTN_TEXT, positiveBtnText);
        arguments.putString(KEY_NEGATIVE_BTN_TEXT, negativeBtnText);

        arguments.putString(KEY_EMAIL, email);

        setArguments(arguments);
        this.dialogType = dialogType;
        this.fingerprintFailedCounter = 0;
    }

    public void create(DialogType dialogType, String title, String message,
                       String positiveBtnText, String negativeBtnText) {

        Bundle arguments = new Bundle();
        arguments.putString(KEY_TITLE, title);
        arguments.putString(KEY_MESSAGE, message);
        arguments.putString(KEY_POSITIVE_BTN_TEXT, positiveBtnText);
        arguments.putString(KEY_NEGATIVE_BTN_TEXT, negativeBtnText);


        this.setArguments(arguments);
        this.dialogType = dialogType;
        this.fingerprintFailedCounter = 0;
    }


    public void create(Context context, DialogType dialogType) {
        if (isAdded()) dismiss();
        switch (dialogType) {
            case SINGLE_CHECKBOX:
                create(dialogType,
                        context.getString(R.string.login_touchId_fingerprint_change_title_1140),
                        context.getString(R.string.login_touchId_fingerprint_change_message_1141),
                        context.getString(R.string.profile_change_email_continue),
                        null);
                break;
            case FINGERPRINT_RECOGNITION_LOGIN:
                create(dialogType,
                        context.getString(R.string.login_login_alert_title_9999),
                        "\n" + context.getString(R.string.settings_security_finger_print_prompt_confirm_1157) + "\n",
                        context.getString(R.string.profile_change_email_cancel),
                        null
                );
                break;
            case FINGERPRINT_RECOGNITION:
                create(dialogType,
                        context.getString(R.string.login_touchId_fingerprint_prompt_title_9999),
                        context.getString(R.string.login_touchId_fingerprint_prompt_message_9999) + "\n\n" + context.getString(R.string.settings_security_finger_print_prompt_confirm_1157) + "\n",
                        context.getString(R.string.profile_change_email_cancel),
                        null
                );
                break;
            case USE_PASSWORD:
                create(dialogType,
                        context.getString(R.string.login_touchId_fingerprint_prompt_title_9999),
                        "\n" + context.getString(R.string.login_touchId_fingerprint_prompt_message_9999) + "\n\n" + context.getString(R.string.settings_security_finger_print_prompt_confirm_1157) + "\n",
                        context.getString(R.string.login_use_password_text_9999).toUpperCase(),
                        null
                );
                break;
            case ENTER_PASSWORD:
                create(dialogType,
                        context.getString(R.string.Settings_alert_password_required_title_931),
                        "\n" + context.getString(R.string.Settings_alert_password_required_message_942) + "\n",
                        context.getString(R.string.profile_change_email_ok),
                        context.getString(R.string.profile_change_email_cancel)
                );
                break;
            default:
                break;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        this.fingerprintFailedCounter = 0;
        getDependencyInjector().inject(this);

        globalTintColor = Color.parseColor(appConfigRepository.getGlobalTintColorHex());

        parentView = getActivity().getLayoutInflater().inflate(R.layout.alert_dialog_fingerprint, null);

        //views
        llEmailWrapper = parentView.findViewById(R.id.llEmailWrapper);
        tilEmail = parentView.findViewById(R.id.tilEmail);
        tieEmail = parentView.findViewById(R.id.tieEmail);
        tiePassword = parentView.findViewById(R.id.tiePassword);

        llCheckBoxWrapper = parentView.findViewById(R.id.llCheckBoxWrapper);
        checkBox = parentView.findViewById(R.id.checkBox);
        llCheckBoxWrapper.setVisibility(View.GONE);

        tvFingerPrint = parentView.findViewById(R.id.tvFingerPrint);
        tvFingerPrint.setVisibility(View.GONE);


        tieEmail.setText(getArguments().getString(KEY_EMAIL));
        switch (dialogType) {
            case FINGERPRINT_RECOGNITION:
            case FINGERPRINT_RECOGNITION_LOGIN:
                tvFingerPrint.setVisibility(View.VISIBLE);
                llEmailWrapper.setVisibility(View.GONE);
                llCheckBoxWrapper.setVisibility(View.GONE);
                break;
            case USE_PASSWORD:
                tvFingerPrint.setVisibility(View.VISIBLE);
                llEmailWrapper.setVisibility(View.GONE);
                llCheckBoxWrapper.setVisibility(View.GONE);
                break;
            case SINGLE_CHECKBOX:
                llEmailWrapper.setVisibility(View.GONE);
                llCheckBoxWrapper.setVisibility(View.VISIBLE);
                break;
            case EMAIL_PASSWORD:
                llEmailWrapper.setVisibility(View.VISIBLE);
                break;
            case EMAIL_PASSWORD_WITH_CHECKBOX:
                llEmailWrapper.setVisibility(View.VISIBLE);
                llCheckBoxWrapper.setVisibility(View.VISIBLE);
                break;
            case ENTER_PASSWORD:
                llEmailWrapper.setVisibility(View.VISIBLE);
                tilEmail.setVisibility(View.GONE);
                llCheckBoxWrapper.setVisibility(View.GONE);
                tvFingerPrint.setVisibility(View.GONE);
                break;
        }

        Drawable photoDrawable = ViewUtilities.tintDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.finger_print), globalTintColor);
        tvFingerPrint.setCompoundDrawablesWithIntrinsicBounds(photoDrawable, null, null, null);


        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        builder.setView(parentView);
        builder.setCancelable(false);


        String message = getArguments().getString(KEY_MESSAGE);
        final String positiveBtnText = getArguments().getString(KEY_POSITIVE_BTN_TEXT);
        String negativeBtnText = getArguments().getString(KEY_NEGATIVE_BTN_TEXT);

        if (globalTintColor == -1) {
            globalTintColor = getThemeAccentColor();
        }

        builder.setTitle(getArguments().getString(KEY_TITLE));

        if (message != null) {
            builder.setMessage(message);
        }
        if (negativeBtnText != null) {
            builder.setNegativeButton(negativeBtnText, onCancelClick);
        }
        if (positiveBtnText != null) {
            builder.setPositiveButton(positiveBtnText, null);
        }

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        //this is done in onShow because button color has to be set after .show is called for AlertDialog
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                setAlertDialogButtonColor(dialog);

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setText(positiveBtnText);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (dialogType == DialogType.ENTER_PASSWORD ||
                                dialogType == DialogType.EMAIL_PASSWORD ||
                                dialogType == DialogType.EMAIL_PASSWORD_WITH_CHECKBOX) {
                            if (tiePassword.getText().toString().equals(deviceSpecificPreferences.getRememberedPassword())) {
                                dismissWithSuccess(dialog);
                            } else {
                                tiePassword.setError("Invalid Password");
                            }
                        } else {
                            dismissWithSuccess(dialog);
                        }
                    }
                });
            }
        });

        return dialog;
    }

    private DialogInterface.OnClickListener onCancelClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            successFingerPrintAuth = false;
            dismiss();
            ServiceLocator.getInstance().eventDispatcher.dispatchEvent(new FingerprintAlertDialogFragmentEvent(
                    dialogType,
                    FingerprintAlertDialogFragmentEvent.ClickedButtonType.NEGATIVE,
                    checkBox.isChecked()));
        }
    };

    private void dismissWithSuccess(AlertDialog dialog) {
        dialog.dismiss();
        ServiceLocator.getInstance().eventDispatcher.dispatchEvent(new FingerprintAlertDialogFragmentEvent(
                dialogType,
                FingerprintAlertDialogFragmentEvent.ClickedButtonType.POSITIVE,
                checkBox.isChecked()));
    }

    private int getThemeAccentColor() {
        final TypedValue value = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        return value.data;
    }

    private void setAlertDialogButtonColor(AlertDialog dialog) {
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        if (negativeButton != null) {
            negativeButton.setTextColor(globalTintColor);
        }

        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(globalTintColor);
        }
    }


    //Fingerprint Recognizer
    @Override
    public void fingerPrintRecognized() {
        this.successFingerPrintAuth = true;
        if (getActivity() != null) {
            this.fingerprintFailedCounter = 0;
            tvFingerPrint.setText(getString(R.string.settings_security_finger_print_recognized_1158));
//            bottomText.setCompoundDrawables(ContextCompat.getDrawable(getActivity(), R.drawable.fingerprint_recognized_40, getContext().getTheme()), null, null, null);

            Drawable photoDrawable = ViewUtilities.tintDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.fingerprint_recognized_40), globalTintColor);

            tvFingerPrint.setCompoundDrawablesWithIntrinsicBounds(photoDrawable, null, null, null);
            tvFingerPrint.setTextColor(globalTintColor);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ServiceLocator.getInstance().eventDispatcher.dispatchEvent(new FingerprintAlertDialogFragmentEvent(
                            FingerprintAlertDialogFragment.DialogType.FINGERPRINT_RECOGNITION_LOGIN));
                    if (fingerprintDialogType() && isAdded()) dismiss();
                }
            }, 500);
        }
    }

    private boolean fingerprintDialogType(){
        return (this.dialogType == FingerprintAlertDialogFragment.DialogType.FINGERPRINT_RECOGNITION_LOGIN ||
                this.dialogType == FingerprintAlertDialogFragment.DialogType.FINGERPRINT_RECOGNITION);
    }


    @Override
    public void fingerPrintNotRecognized() {

        this.successFingerPrintAuth = false;
        if (getActivity() != null) {
            tvFingerPrint.setText(getString(R.string.settings_security_finger_print_not_recognized_1159));
            tvFingerPrint.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fingeprint_fail_40, 0, 0, 0);
            tvFingerPrint.setTextColor(ContextCompat.getColor(getActivity(), R.color.crimson_red));

            this.fingerprintFailedCounter++;
            //check attempt limit
            if (hasFingerprintFailed()) {
                ServiceLocator.getInstance().eventDispatcher.dispatchEvent(new FingerprintAlertDialogFragmentEvent(
                        FingerprintAlertDialogFragment.DialogType.FINGERPRINT_RECOGNITION_LOGIN));
                if (fingerprintDialogType() && isAdded()) dismiss();
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Reprint.cancelAuthentication();
        super.onDismiss(dialog);
    }

    public boolean isSuccessFingerPrintAuth() {
        return successFingerPrintAuth;
    }

    public void setSuccessFingerPrintAuth(Boolean successFingerPrintAuth) {
        this.successFingerPrintAuth =successFingerPrintAuth;
    }

    public boolean hasFingerprintFailed() {
        return this.fingerprintFailedCounter > 2;
    }

    protected final DependencyInjector getDependencyInjector() {
        return getVitalityActiveApplication().getDependencyInjector();
    }

    protected VitalityActiveApplication getVitalityActiveApplication() {
        return (VitalityActiveApplication) getContext().getApplicationContext();
    }
}
