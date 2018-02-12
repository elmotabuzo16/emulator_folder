package com.vitalityactive.va.activerewards.rewards;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.EmptyStateViewHolder;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.activerewards.rewards.persistence.RewardsRepository;
import com.vitalityactive.va.activerewards.rewards.presenters.CinemaRewardConfirmationPresenter;
import com.vitalityactive.va.constants.RewardId;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.utilities.ViewUtilities;

import javax.inject.Inject;

public class CinemaRewardConfirmationActivity
        extends BasePresentedActivity<CinemaRewardConfirmationPresenter.UserInterface, CinemaRewardConfirmationPresenter>
        implements CinemaRewardConfirmationPresenter.UserInterface {

    public static final String REWARD_UNIQUE_ID = "WHEEL_SPIN_UNCLAIMED_REWARD_UNIQUE_ID";

    @Inject
    CinemaRewardConfirmationPresenter cinemaPresenter;

    private EmptyStateViewHolder emptyStateViewHolder;
    private View loadingIndicator;
    private View content;
    private View noRewardContent;
    private TextView rewardValueAndType;
    private TextView rewardDescription;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_cinema_reward_confirmation);

        presenter.setRewardUniqueId(getIntent().getLongExtra(REWARD_UNIQUE_ID, 0));

        showButtonBar();
        assignViews();
        setUpEmptyState();
        setUpActionBarWithTitle(R.string.AR_rewards_chosen_reward_title_736);
    }

    private void setUpEmptyState() {
        emptyStateViewHolder = new EmptyStateViewHolder(findViewById(R.id.empty_state));
        emptyStateViewHolder.setup(R.string.error_unable_to_load_title_503,
                R.string.error_unable_to_load_message_504,
                R.string.try_again_button_title_43,
                new EmptyStateViewHolder.EmptyStatusButtonClickedListener() {
                    @Override
                    public void onEmptyStateButtonClicked(EmptyStateViewHolder emptyStateViewHolder) {
                        emptyStateViewHolder.hideEmptyStateViewAndShowOtherView(content);
                        presenter.onTryAgain();
                    }
                });
    }

    @Override
    public void showNoReward() {
        ViewUtilities.setViewGone(content);
        ViewUtilities.setViewVisible(noRewardContent);

        setUpActionBarWithTitle(R.string.AR_partners_reward_title_1058);
    }

    private void showRewardVoucher(RewardVoucherDTO rewardVoucher) {
        ViewUtilities.setViewGone(noRewardContent);
        ViewUtilities.setViewVisible(content);

        rewardValueAndType.setText(getRewardValueAndType(rewardVoucher));
        rewardDescription.setText(getString(R.string.AR_rewards_cinema_way_to_go_message_1113, getRewardValueAndType(rewardVoucher)));
        setUpActionBarWithTitle(R.string.AR_rewards_chosen_reward_title_736);
    }

    @NonNull
    private String getRewardValueAndType(RewardVoucherDTO rewardVoucher) {
        return RewardsRepository.getRewardDescription(rewardVoucher.rewardValue, rewardVoucher.rewardType);
    }

    @Override
    public void showSelectedVoucher(RewardVoucherDTO voucher) {
        emptyStateViewHolder.hideEmptyStateViewAndShowOtherView(content);
        if (voucher.rewardId == RewardId._NOWIN) {
            showNoReward();
        } else {
            showRewardVoucher(voucher);
        }
    }

    @Override
    public void showGenericErrorMessage() {
        showEmptyState();
    }

    @Override
    public void showConnectionErrorMessage() {
        showEmptyState();
    }

    private void showEmptyState() {
        emptyStateViewHolder.showEmptyStateViewAndHideOtherView(content);
    }

    private void assignViews() {
        content = findViewById(R.id.content);
        noRewardContent = findViewById(R.id.no_reward_content);
        loadingIndicator = findViewById(R.id.loading_indicator);
        rewardValueAndType = findViewById(R.id.reward_value_and_type);
        rewardDescription = findViewById(R.id.reward_description);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        navigationCoordinator.getActiveRewardsDependencyInjector().inject(this);
    }

    @Override
    protected CinemaRewardConfirmationPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected CinemaRewardConfirmationPresenter getPresenter() {
        return cinemaPresenter;
    }

    public void handleDoneButtonTapped(View view) {
        finish();
    }

    @Override
    public void showLoadingIndicator() {
        ViewUtilities.setViewGone(content);
        ViewUtilities.setViewGone(noRewardContent);
        ViewUtilities.setViewVisible(loadingIndicator);
    }

    @Override
    public void hideLoadingIndicator() {
        ViewUtilities.setViewGone(loadingIndicator);
        ViewUtilities.setViewVisible(content);
    }
}
