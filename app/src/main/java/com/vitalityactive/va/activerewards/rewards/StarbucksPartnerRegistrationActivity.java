package com.vitalityactive.va.activerewards.rewards;

import android.content.ComponentName;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.TextInputLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.rewards.dto.UnclaimedRewardDTO;
import com.vitalityactive.va.activerewards.rewards.presenters.StarbucksPartnerRegistrationPresenter;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.register.entity.EmailAddress;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.uicomponents.ButtonBarConfigurator;
import com.vitalityactive.va.utilities.CMSImageLoader;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.implemented.TextWatcherWithDefaultBeforeAndAfter;

import javax.inject.Inject;

public class StarbucksPartnerRegistrationActivity
        extends BasePresentedActivity<StarbucksPartnerRegistrationPresenter.UserInterface, StarbucksPartnerRegistrationPresenter>
        implements StarbucksPartnerRegistrationPresenter.UserInterface {
    public static final String REWARD_UNIQUE_ID = "WHEEL_SPIN_UNCLAIMED_REWARD_UNIQUE_ID";
    public static final String PARTNER_REGISTRATION_EMAIL = "PARTNER_REGISTRATION_EMAIL";
    public static final String OUTGOING_BUNDLE_KEY = "OUTGOING_BUNDLE_KEY";
    public static final String STARBUCKS_CONFIRM_EMAIL_ALERT = "STARBUCKS_CONFIRM_EMAIL_ALERT";
    private static final String CONNECTION_STARBUCKS_EMAIL_SELECT_VOUCHER_ERROR_MESSAGE = "CONNECTION_STARBUCKS_EMAIL_SELECT_VOUCHER_ERROR_MESSAGE";
    private static final String GENERIC_STARBUCKS_EMAIL_SELECT_VOUCHER_ERROR_MESSAGE = "GENERIC_STARBUCKS_EMAIL_SELECT_VOUCHER_ERROR_MESSAGE";

    @Inject
    CMSImageLoader cmsImageLoader;
    @Inject
    EventDispatcher eventDispatcher;
    @Inject
    StarbucksPartnerRegistrationPresenter starbucksPresenter;

    private TextView rewardTitle;
    private TextView rewardDescription;
    private TextView emailAddressView;
    private TextView registerAccountMessage;
    private ImageView starbucksIcon;
    private EmailAddress partnerEmailAddress = new EmailAddress("");
    private TextInputLayout emailLayout;
    private ButtonBarConfigurator buttonBarConfigurator;
    private String partnerURL;
    private CustomTabsClient mCustomTabsClient = null;
    CustomTabsServiceConnection connection = new CustomTabsServiceConnection() {
        @Override
        public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
            mCustomTabsClient = client;
            prepareBrowserPage(partnerURL);
            unbindService(connection);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mCustomTabsClient = null;
        }
    };
    private CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
    private TextWatcherWithDefaultBeforeAndAfter textWatcher = new TextWatcherWithDefaultBeforeAndAfter() {
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int beforeCount, int i2) {
            partnerEmailAddress.setText(charSequence);
            checkEmailValidation();
        }
    };

    private EventListener<AlertDialogFragment.DismissedEvent> alertDialogDismissedEventListener = new EventListener<AlertDialogFragment.DismissedEvent>() {
        @Override
        public void onEvent(AlertDialogFragment.DismissedEvent event) {
            if (positiveButtonTapped(event)) {
                getPresenter().onUserConfirmsEmailAddress(partnerEmailAddress.toString());
            }
        }
    };

    private void prepareBrowserPage(String url) {
        mCustomTabsClient.warmup(0);

        CustomTabsSession session = mCustomTabsClient.newSession(new CustomTabsCallback());
        session.mayLaunchUrl(Uri.parse(url), null, null);
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        setupDisplay();

        starbucksPresenter.setRewardUniqueId(getIntent().getLongExtra(REWARD_UNIQUE_ID, 0));

        assignViews();
        setRegisterAccountMessage();

        CustomTabsClient.bindCustomTabsService(this, "com.android.chrome", connection);
        partnerURL = "https://www.starbucks.com/account/create";
    }

    private void setupDisplay() {
        setContentView(R.layout.activity_starbucks_partner_registration);

        setUpActionBarWithTitle(R.string.AR_partners_reward_title_1058)
                .setDisplayHomeAsUpEnabled(true);
        buttonBarConfigurator = setupButtonBar().setForwardButtonEnabled(false);
    }

    private void setRegisterAccountMessage() {
        String registerAccount = getString(R.string.AR_partners_starbucks_reward_register_account_1060);
        String message = String.format(getString(R.string.AR_partners_starbucks_reward_email_message_1059), registerAccount);

        int indexOf = message.indexOf(registerAccount);

        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                if (mCustomTabsClient != null &&
                        builder != null) {
                    builder.build().launchUrl(StarbucksPartnerRegistrationActivity.this, Uri.parse(partnerURL));
                }
            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setColor(textPaint.linkColor);
                textPaint.setUnderlineText(false);
            }
        };

        registerAccountMessage.setMovementMethod(LinkMovementMethod.getInstance());
        registerAccountMessage.setText(getSpannableString(registerAccount, message, indexOf, span));
    }

    @NonNull
    private SpannableString getSpannableString(String registerAccount, String message, int indexOf, ClickableSpan span) {
        SpannableString spannableString = new SpannableString(message);
        spannableString.setSpan(span, indexOf, indexOf + registerAccount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private void checkEmailValidation() {
        if (partnerEmailAddress.isValid()) {
            handleValidEmailAddress();
        } else {
            handleInvalidEmailAddress();
        }
    }

    private void handleInvalidEmailAddress() {
        buttonBarConfigurator.setForwardButtonEnabled(false);
        emailLayout.setErrorEnabled(true);
        emailLayout.setError(getString(R.string.registration_invalid_email_footnote_error_35));
    }

    private void handleValidEmailAddress() {
        buttonBarConfigurator.setForwardButtonEnabled(true);
        emailLayout.setErrorEnabled(false);
    }

    @Override
    protected void resume() {
        emailAddressView.addTextChangedListener(textWatcher);
        eventDispatcher.addEventListener(AlertDialogFragment.DismissedEvent.class, alertDialogDismissedEventListener);
    }

    @Override
    protected void pause() {
        emailAddressView.removeTextChangedListener(textWatcher);
        eventDispatcher.removeEventListener(AlertDialogFragment.DismissedEvent.class, alertDialogDismissedEventListener);
    }

    private void assignViews() {
        rewardTitle = findViewById(R.id.reward_name);
        rewardDescription = findViewById(R.id.reward_value_and_type);
        emailAddressView = findViewById(R.id.email_address);
        emailLayout = findViewById(R.id.email_layout);
        registerAccountMessage = findViewById(R.id.register_account_message);
        starbucksIcon = findViewById(R.id.icon);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        navigationCoordinator.getActiveRewardsDependencyInjector().inject(this);
    }

    @Override
    protected StarbucksPartnerRegistrationPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected StarbucksPartnerRegistrationPresenter getPresenter() {
        return starbucksPresenter;
    }

    @Override
    public void setRewardData(UnclaimedRewardDTO reward) {
        starbucksIcon.setImageResource(R.drawable.starbucks_200_x_150);
        ViewUtilities.setTextAndMakeVisibleIfPopulated(rewardTitle, reward.outcomeRewardName);
        ViewUtilities.setTextAndMakeVisibleIfPopulated(rewardDescription, reward.outcomeRewardValue + " " + reward.outcomeRewardType);
    }

    @Override
    public void showPartnerRegisteredEmail(@Nullable String partnerRegisteredEmail) {
        if (!TextUtilities.isNullOrWhitespace(partnerRegisteredEmail)) {
            populateEmailAddress(partnerRegisteredEmail);
        }
    }

    @Override
    public void navigateAfterStarbucksRewardConfirmed(long uniqueID) {
        navigationCoordinator.navigateAfterStarbucksRewardConfirmed(StarbucksPartnerRegistrationActivity.this,
                uniqueID);
    }

    @Override
    public void showConnectionErrorMessage() {
        hideLoadingIndicator();

        AlertDialogFragment alert = AlertDialogFragment.create(
                CONNECTION_STARBUCKS_EMAIL_SELECT_VOUCHER_ERROR_MESSAGE,
                getString(R.string.connectivity_error_alert_title_44),
                getString(R.string.connectivity_error_alert_message_45),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
        alert.show(getSupportFragmentManager(), CONNECTION_STARBUCKS_EMAIL_SELECT_VOUCHER_ERROR_MESSAGE);
    }

    @Override
    public void showDataSharingConsent() {
        navigationCoordinator.navigateToStarbucksDataSharingConsent(this,
                presenter.getUniqueRewardId(),
                String.valueOf(partnerEmailAddress.getText()));
    }

    @Override
    public void showGenericErrorMessage() {
        hideLoadingIndicator();

        AlertDialogFragment alert = AlertDialogFragment.create(
                GENERIC_STARBUCKS_EMAIL_SELECT_VOUCHER_ERROR_MESSAGE,
                getString(R.string.alert_unknown_title_266),
                getString(R.string.alert_unknown_message_267),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
        alert.show(getSupportFragmentManager(), GENERIC_STARBUCKS_EMAIL_SELECT_VOUCHER_ERROR_MESSAGE);
    }

    private void populateEmailAddress(String partnerRegisteredEmail) {
        emailAddressView.setText(partnerRegisteredEmail);
        partnerEmailAddress = new EmailAddress(partnerRegisteredEmail);
        checkEmailValidation();
    }

    public void handleConfirmButtonTapped(View view) {
        AlertDialogFragment.create(
                STARBUCKS_CONFIRM_EMAIL_ALERT,
                getString(R.string.AR_partners_starbucks_reward_alert_title_1063),
                getAlertMessage(),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.confirm_title_button_182))
                .show(getSupportFragmentManager(), STARBUCKS_CONFIRM_EMAIL_ALERT);
    }


    public String getAlertMessage() {
        return String.format(getString(R.string.AR_partners_starbucks_reward_alert_message_1064), partnerEmailAddress.getText());
    }
}
