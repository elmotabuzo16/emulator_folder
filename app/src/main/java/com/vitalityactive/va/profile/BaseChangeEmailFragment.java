package com.vitalityactive.va.profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedFragment;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.profile.ChangeEmailPresenter.UI;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.uicomponents.LoadingIndicatorFragment;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;

import javax.inject.Inject;

public abstract class BaseChangeEmailFragment extends BasePresentedFragment<ChangeEmailPresenter.UI, ChangeEmailPresenter>
        implements ChangeEmailPresenter.UI, EventListener<AlertDialogFragment.DismissedEvent> {

    private static final String LOADING_INDICATOR = "LOADING_INDICATOR";
    private static final String CONFIRM_DIALOG_TAG = "ARE_YOU_SURE";
    private static final String ERROR_DIALOG_TAG = "UNABLE_TO_CHANGE_EMAIL";
    private static final String ERROR_PASSWORD_DIALOG_TAG = "INCORRECT_PASSWORD";

    private MenuItem doneItem;

    @Inject
    ChangeEmailPresenter presenter;
    @Inject
    EventDispatcher eventDispatcher;
    @Inject
    AppConfigRepository appConfigRepository;

    private TextView currentEmailView;
    private ImageView newEmailIconView;
    private View menuItemView;
    private int globalTintColor;

    protected EditText newEmailAddView;
    protected TextInputLayout newEmailAddLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_change_email, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.done_menu, menu);
        doneItem = menu.findItem(R.id.action_menu);
        doneItem.setEnabled(false);
        updateMenuButton(false);

        doneItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                presenter.onUserTriesToChangeEmail(newEmailAddView.getText().toString());
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected void activityCreated(@Nullable Bundle savedInstanceState) {
        View parentView = getView();
        if (parentView == null) {
            return;
        }
        setUpChangeEmailView(parentView);
        addEmailTextViewListener();
        addEmailTouchOutsideListener();
        addFocusOutsideListener();
        marketUIUpdate();
    }

    private void setUpChangeEmailView(@NonNull View parentView) {
        globalTintColor = Color.parseColor(appConfigRepository.getGlobalTintColorHex());
        currentEmailView = parentView.findViewById(R.id.current_email);
        newEmailAddView = parentView.findViewById(R.id.new_email_address);
        newEmailAddLayout = parentView.findViewById(R.id.new_email_layout);
        newEmailIconView = parentView.findViewById(R.id.new_email_icon);
        menuItemView = parentView.findViewById(R.id.action_menu);

        newEmailAddLayout.setErrorTextAppearance(R.style.MyTextInputLayoutErrorText);
    }

    private void addEmailTextViewListener() {
        newEmailAddView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // DO NOTHING
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CharSequence text = s == null ? "" : s;
                presenter.onEmailTextChanged(text);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // DO NOTHING
            }
        });
    }

    private void addEmailTouchOutsideListener() {
        if (getView() != null) {
            getView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        int touchX = (int) (event.getX());
                        int touchY = (int) (event.getY());
                        boolean isOutsideEmailView = ViewUtilities.isPointOutsideViewHitRect(touchX, touchY, newEmailAddView);

                        if (isOutsideEmailView) {
                            ViewUtilities.hideKeyboard(getActivity(), getView());
                        }
                        return isOutsideEmailView;
                    }
                    return false;
                }
            });
        }
    }

    private void addFocusOutsideListener() {
        newEmailAddView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CharSequence text = ((TextView) v).getText();

                if (hasFocus || !TextUtilities.isNullOrWhitespace(text)) {
                    newEmailIconView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.email_active));
                } else {
                    newEmailIconView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.email_inactive));
                }

                if (!hasFocus) {
                    presenter.onNewEmailEntered();
                }
            }
        });
    }

    @Override
    public void showChangeEmailExistingError() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                ERROR_DIALOG_TAG,
                getResources().getString( R.string.Settings_alert_unable_to_change_title_927 ),
                getResources().getString( R.string.Settings_alert_unable_to_change_message_928),
                null,
                null,
                getResources().getString( R.string.ok_button_title_40 ));
        alert.show(getFragmentManager(), ERROR_DIALOG_TAG);
    }

    @Override
    public void showChangeEmailFailedError() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                ERROR_DIALOG_TAG,
                getResources().getString( R.string.Settings_alert_unable_to_change_title_927 ),
                getResources().getString( R.string.try_again),
                null,
                null,
                getResources().getString( R.string.profile_change_email_ok ));
        alert.show(getFragmentManager(), ERROR_DIALOG_TAG);
    }

    @Override
    public void showChangeEmailConfirmation() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                CONFIRM_DIALOG_TAG,
                getResources().getString( R.string.Settings_alert_change_confirmation_title_929 ),
                getResources().getString( R.string.profile_edit_mail_prompt_body ),
                getResources().getString( R.string.cancel_button_title_24 ).toUpperCase(),
                null,
                getResources().getString( R.string.continue_button_title_87 ).toUpperCase());
        alert.show(getFragmentManager(), CONFIRM_DIALOG_TAG);
    }

    @Override
    public void showPasswordConfirmation() {
        AlertDialog.Builder confirmPasswordAlert = new AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams") View dialogView = getLayoutInflater().inflate(R.layout.alert_password_dialog, null, false);
        confirmPasswordAlert.setView(dialogView);
        AlertDialog dialog = confirmPasswordAlert.create();

        setupPasswordDialog(dialog, dialogView);

        dialog.show();
    }

    private void setupPasswordDialog(final AlertDialog alertDialog, final View dialogView){
        TextView confirmNewEmail = dialogView.findViewById(R.id.confirm_new_email);
        final TextView confirmPasswordView = dialogView.findViewById(R.id.confirm_password);
        confirmNewEmail.setText(newEmailAddView.getText());

        dialogView.findViewById(R.id.cancel_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.ok_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String confirmPassword = confirmPasswordView.getText().toString();
               String newEmail = newEmailAddView.getText().toString();
               if(!TextUtilities.isNullOrWhitespace(confirmPassword)) {
                   alertDialog.dismiss();
                   presenter.onUserConfirmsChangeEmail(newEmail, confirmPassword);
               }
            }
        });
    }

    @Override
    public void showIncorrectPasswordError() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                ERROR_PASSWORD_DIALOG_TAG,
                getResources().getString( R.string.profile_change_email_incorrect_password_heading ),
                getResources().getString( R.string.profile_change_email_incorrect_password_body ),
                null,
                null,
                getResources().getString( R.string.profile_change_email_ok ));
        alert.show(getFragmentManager(), ERROR_PASSWORD_DIALOG_TAG);
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

    private void updateMenuButton(boolean isEnabled) {
        if (menuItemView != null && menuItemView instanceof TextView) {
            if (isEnabled) {
                ((TextView) menuItemView).setTextColor(Color.WHITE);
            } else {
                ((TextView) menuItemView).setTextColor(Color.GRAY);
            }
        }
        if(doneItem != null){
            doneItem.setEnabled(isEnabled);
        }
    }

    @Override
    public void updateNewEmailView(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((PersonalDetailsActivity)getActivity()).changeActionBarHomeIconToBackArrow();
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void showInvalidEmailAddressMessage() {
        updateErrorColor("RED");
        newEmailAddLayout.setError(getString(R.string.Settings_profile_email_error_message_925));
        newEmailAddLayout.setErrorEnabled(true);
        updateMenuButton(false);
    }

    @Override
    public void hideInvalidEmailAddressMessage() {
        updateErrorColor("BLACK");
        newEmailAddLayout.setErrorEnabled(false);
        if(newEmailAddView.getText().length() > 0){
            updateMenuButton(true);
        }else{
            updateMenuButton(false);
        }
    }

    public void showInvalidLengthEmailAddressMessage() {
        updateErrorColor("RED");String emailError = getString(R.string.Settings_profile_email_error_character_limit_926);
        int max = 100;
        if (emailError.contains("%1$s")) {
            emailError = String.format(emailError, max);
            newEmailAddLayout.setError(emailError);
        } else {
            newEmailAddLayout.setError(emailError);
        }
        newEmailAddLayout.setErrorEnabled(true);
        updateMenuButton(false);
    }

    private void updateErrorColor(String iconColor){
        if(iconColor.equals("RED")){
            Drawable myIcon = ContextCompat.getDrawable(getActivity(), R.drawable.email_active);
            myIcon.setColorFilter(ContextCompat.getColor(getActivity(), R.color.crimson_red), PorterDuff.Mode.SRC_ATOP);
            newEmailIconView.setImageDrawable(myIcon);
            newEmailAddView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        } else {
            Drawable myIcon = ContextCompat.getDrawable(getActivity(), R.drawable.email_active);
            myIcon.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);
            newEmailIconView.setImageDrawable(myIcon);
            newEmailAddView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
        }
    }

    private void updateNewEmail() {
        newEmailAddLayout.setHintAnimationEnabled(false);
        newEmailAddView.setText(presenter.getNewEmailText());
        newEmailAddLayout.setHintAnimationEnabled(true);

        if (!TextUtilities.isNullOrWhitespace(newEmailAddView.getText())) {
            newEmailIconView.setImageResource(R.drawable.email_active);
        }
    }

    @Override
    public void updateDoneEnabled(boolean isEnabled) {
        updateMenuButton(isEnabled);
    }

    @Override
    public void showChangeEmailInfo(String emailAddress) {
        currentEmailView.setText(emailAddress);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected void appear() {
        super.appear();
        updateNewEmail();
        if(newEmailAddView != null){
            newEmailAddView.clearFocus();
        }
        eventDispatcher.addEventListener(AlertDialogFragment.DismissedEvent.class, this);

        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if( supportActionBar != null){
            supportActionBar.setTitle(getResources().getString(R.string.Settings_profile_change_email_title_921));
        }
    }

    @Override
    protected void disappear() {
        super.disappear();
        eventDispatcher.removeEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    @Override
    protected UI getUserInterface() {
        return this;
    }

    @Override
    protected ChangeEmailPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        if (event.getType().equals(CONFIRM_DIALOG_TAG) && event.getClickedButtonType() == AlertDialogFragment.DismissedEvent.ClickedButtonType.Positive) {
            processDismissEvent();
        }
    }


//    protected void processDismissEvent() {
//        getPresenter().onUserConfirmsChangeEmail(newEmailAddView.getText().toString(), presenter.getEncryptedPassword());
//    }

    protected abstract void processDismissEvent();
    protected abstract void marketUIUpdate();
}
