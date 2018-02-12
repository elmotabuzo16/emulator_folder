package com.vitalityactive.va.activerewards.rewards;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.rewards.content.RewardPartnerContent;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.activerewards.rewards.persistence.RewardsRepository;
import com.vitalityactive.va.activerewards.rewards.presenters.RewardVoucherPresenter;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

import javax.inject.Inject;

import static com.vitalityactive.va.constants.RewardId._CINEWORLD;
import static com.vitalityactive.va.constants.RewardId._CINEWORLDORVUE;
import static com.vitalityactive.va.constants.RewardId._NOWIN;
import static com.vitalityactive.va.constants.RewardId._STARBUCKSVOUCHER;
import static com.vitalityactive.va.constants.RewardId._VUE;

/**
 * Created by peter.ian.t.betos on 08/02/2018.
 */

public class BaseRewardVoucherActivity extends BasePresentedActivity<RewardVoucherPresenter.UserInterface, RewardVoucherPresenter>
        implements RewardVoucherPresenter.UserInterface, EventListener<AlertDialogFragment.DismissedEvent> {

    public static final String VOUCHER_UNIQUE_ID = "VOUCHER_UNIQUE_ID";
    private static final String STARBUCKS_SELECT_VOUCHER_GENERIC_ERROR_MESSAGE = "STARBUCKS_SELECT_VOUCHER_GENERIC_ERROR_MESSAGE";
    private static final String STARBUCKS_SELECT_VOUCHER_CONNECTION_ERROR_MESSAGE = "STARBUCKS_SELECT_VOUCHER_CONNECTION_ERROR_MESSAGE";

    @Inject
    RewardVoucherPresenter rewardVoucherPresenter;
    @Inject
    DateFormattingUtilities dateFormattingUtilities;
    @Inject
    EventDispatcher eventDispatcher;

    private TextView rewardName;
    private TextView rewardValueAndType;
    private TextView rewardDescription;
    protected TextView voucherInstructions;
    private TextView installOrOpenApp;
    private ImageView icon;
    private LinearLayout voucherList;
    private RelativeLayout starbucksAppLayout;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_reward_voucher);
        assignViews();

        rewardVoucherPresenter.setUniqueId(getIntent().getLongExtra(VOUCHER_UNIQUE_ID, 0));
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
        navigationCoordinator.getActiveRewardsDependencyInjector().inject(this);
    }

    @Override
    protected RewardVoucherPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected RewardVoucherPresenter getPresenter() {
        return rewardVoucherPresenter;
    }

    private void assignViews() {
        icon = findViewById(R.id.icon);
        rewardName = findViewById(R.id.reward_name);
        rewardValueAndType = findViewById(R.id.reward_value_and_type);
        rewardDescription = findViewById(R.id.reward_description);
        voucherInstructions = findViewById(R.id.voucher_instructions);
        starbucksAppLayout = findViewById(R.id.starbucks_app_layout);
        installOrOpenApp = findViewById(R.id.install_or_open_app);
        voucherList = findViewById(R.id.voucher_list);
    }

    public void showPendingRewardState(RewardVoucherDTO voucher) {
        hideButtonBar();
        setActionBarTitleAndDisplayHomeAsUp(getString(R.string.AR_partners_reward_title_1058));
        setPendingContent(voucher);
        setRewardSpecificInstructionsForPendingVoucher(voucher);
    }

    private void setPendingContent(RewardVoucherDTO voucher) {
        setIcon(voucher);

        ViewUtilities.setTextAndMakeVisibleIfPopulated(rewardName, voucher.name);
        ViewUtilities.setTextAndMakeVisibleIfPopulated(rewardValueAndType, getRewardSubtitle(voucher));
        ViewUtilities.setTextAndMakeVisibleIfPopulated(rewardDescription, String.format(getRewardSpecificRewardDescription(voucher), voucher.rewardType));

        inflateVoucherItem(voucherList, getString(R.string.AR_goal_pending_title_693), 0, true);
    }

    private String getRewardSpecificRewardDescription(RewardVoucherDTO voucher) {
        switch (voucher.rewardId) {
            case _STARBUCKSVOUCHER:
                return getString(R.string.ar_rewards_pending_starbucks_reward_mesage_1078);
            case _CINEWORLDORVUE:
                return getString(R.string.AR_rewards_cinema_way_to_go_message_1113);
        }
        return "This scenario is not part of any reward protocol, but is an unintended side effect of prolonged exposure to this project. Please report to your nearest brain matter emancipation officer.";
    }

    private View inflateVoucherItem(LinearLayout voucherList, String voucherValue, int voucherIndex, boolean attachToRoot) {
        View view = getLayoutInflater().inflate(R.layout.reward_voucher_item, voucherList, attachToRoot);

        TextView title = view.findViewById(R.id.voucher_code_title);
        TextView subTitle = view.findViewById(R.id.voucher_code);

        String voucherTitleText = getString(R.string.AR_voucher_code_title_664);

        if (voucherIndex > 0) {
            voucherTitleText = voucherTitleText.concat(" " + voucherIndex);
        }

        title.setText(voucherTitleText);
        subTitle.setText(voucherValue);

        return view;
    }

    @NonNull
    private String getRewardSubtitle(RewardVoucherDTO voucher) {
        return RewardsRepository.getRewardDescription(voucher.rewardValue, voucher.rewardType);
    }

    @Override
    public void showVoucherNumberState(RewardVoucherDTO voucher) {
        showButtonBar();
        setUpActionBarWithTitle(getString(R.string.AR_partners_reward_title_1058));
        setAvailableContent(voucher);

        createVoucherItems(voucher);
    }

    private void createVoucherItems(RewardVoucherDTO voucher) {
        if (voucher.voucherNumbers != null && voucher.voucherNumbers.size() > 0) {
            voucherList.removeAllViews();

            for (int index = 0; index < voucher.voucherNumbers.size(); index++) {
                voucherList.addView(inflateVoucherItem(voucherList, voucher.voucherNumbers.get(index), index + 1, false));
            }
        }
    }

    private void setIcon(RewardVoucherDTO voucher) {
        icon.setImageResource(RewardPartnerContent.fromRewardId(voucher.rewardId).getLargeLogoResourceId());
    }

    public void showStarbucksAvailableRewardState(RewardVoucherDTO voucher) {
        showButtonBar();
        setUpActionBarWithTitle(getString(R.string.AR_partners_reward_title_1058));
        setAvailableContent(voucher);
        starbucksAppLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNotAwardedScreen(RewardVoucherDTO voucher) {
        setActionBarTitleAndDisplayHomeAsUp(getString(R.string.AR_partners_reward_title_1058));

        setIcon(voucher);

        String rewardValue = String.format(getString(R.string.ar_rewards_reward_could_not_awarded_title_1083), voucher.name);
        String rewardDescriptionMessage = getString(R.string.ar_rewards_reward_could_not_awarded_content_1084);

        ViewUtilities.setTextAndMakeVisibleIfPopulated(rewardValueAndType, rewardValue);
        ViewUtilities.setTextAndMakeVisibleIfPopulated(rewardDescription, rewardDescriptionMessage);
    }

    @Override
    public void launchApplication(String packageName) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            startActivity(launchIntent);
        }
    }

    @Override
    public void displayAppOpen() {
        installOrOpenApp.setText(getString(R.string.ar_rewards_starbucks_open_1088));
    }

    @Override
    public void displayAppInstall() {
        installOrOpenApp.setText(getString(R.string.ar_rewards_starbucks_install_1089));
    }

    @Override
    public void showLoadingIndicator() {
        findViewById(R.id.loadable_content).setVisibility(View.GONE);
        findViewById(R.id.loading_indicator).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        findViewById(R.id.loadable_content).setVisibility(View.VISIBLE);
        findViewById(R.id.loading_indicator).setVisibility(View.GONE);
    }

    @Override
    public void showGenericError() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                STARBUCKS_SELECT_VOUCHER_GENERIC_ERROR_MESSAGE,
                getString(R.string.alert_unknown_title_266),
                getString(R.string.alert_unknown_message_267),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
        alert.show(getSupportFragmentManager(), STARBUCKS_SELECT_VOUCHER_GENERIC_ERROR_MESSAGE);
    }

    @Override
    public void showConnectionError() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                STARBUCKS_SELECT_VOUCHER_CONNECTION_ERROR_MESSAGE,
                getString(R.string.connectivity_error_alert_title_44),
                getString(R.string.connectivity_error_alert_message_45),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
        alert.show(getSupportFragmentManager(), STARBUCKS_SELECT_VOUCHER_CONNECTION_ERROR_MESSAGE);
    }

    private void setAvailableContent(RewardVoucherDTO voucher) {
        setIcon(voucher);

        String rewardDescriptionMessage = String.format(getString(R.string.AR_voucher_expires_658),
                getFormattedDate(voucher));

        ViewUtilities.setTextAndMakeVisibleIfPopulated(rewardName, voucher.name);
        ViewUtilities.setTextAndMakeVisibleIfPopulated(rewardValueAndType, getRewardSubtitle(voucher));
        ViewUtilities.setTextAndMakeVisibleIfPopulated(rewardDescription, rewardDescriptionMessage);

        if (voucher.rewardId != _NOWIN) {
            inflateVoucherItem(voucherList, getString(R.string.ar_rewards_available_title_1086), 0, true);
        }

        setRewardSpecificInstructionsForAvailableVoucher(voucher);
    }

    protected void setRewardSpecificInstructionsForAvailableVoucher(RewardVoucherDTO voucher) {
        CharSequence stringToDisplay = "";

        switch (voucher.rewardId) {
            case _STARBUCKSVOUCHER:
                stringToDisplay = String.format(getText(R.string.Ar_Rewards_starbucks_issued_reward_ready_for_collection_1085).toString(), voucher.rewardType);
                break;
            case _VUE:
            case _CINEWORLD:
                stringToDisplay = getText(R.string.AR_rewards_cinema_voucher_footer_1112);
                break;
        }

        ViewUtilities.setHTMLTextAndMakeVisibleIfPopulated(voucherInstructions, stringToDisplay);
    }

    private String getFormattedDate(RewardVoucherDTO voucher) {
        return dateFormattingUtilities.formatWeekdayDateMonthYear(voucher.expiryDate);
    }

    public void handleDoneButtonTapped(View view) {
        navigationCoordinator.navigateAfterARDoneTapped(this);
    }

    public void handleOnAppLayoutTapped(View view) {
        rewardVoucherPresenter.handleInstallOrOpenApp();
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        if (event.getClickedButtonType() == AlertDialogFragment.DismissedEvent.ClickedButtonType.Positive) {
            rewardVoucherPresenter.fetchRewardVoucher();
        } else {
            onBackPressed();
        }
    }

    protected void setRewardSpecificInstructionsForPendingVoucher(RewardVoucherDTO voucher) {
        CharSequence stringToDisplay = "";

        switch (voucher.rewardId) {
            case _STARBUCKSVOUCHER:
                stringToDisplay = String.format(getText(R.string.ar_rewards_pending_starbucks_reward_footer_1079).toString(),
                        voucher.rewardType, voucher.rewardType);
                break;
            case _CINEWORLDORVUE:
                stringToDisplay = getText(R.string.AR_rewards_cinema_pending_footer_1115);
                break;
            case _VUE:
            case _CINEWORLD:
                stringToDisplay = "This scenario is not part of any reward protocol, but is an unintended side effect of prolonged exposure to this project. Please report to your nearest brain matter emancipation officer.";
                break;
        }

        ViewUtilities.setHTMLTextAndMakeVisibleIfPopulated(voucherInstructions, stringToDisplay);
    }
}
