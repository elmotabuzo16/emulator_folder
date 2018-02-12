package com.vitalityactive.va.activerewards.rewards;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.EmptyStateViewHolder;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.rewards.dto.RewardSelectionDTO;
import com.vitalityactive.va.activerewards.rewards.presenters.ChooseRewardPresenter;
import com.vitalityactive.va.activerewards.rewards.viewholder.RewardSelectionViewHolder;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.uicomponents.ButtonBarConfigurator;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.List;

import javax.inject.Inject;

public class ChooseRewardActivity
        extends BasePresentedActivity<ChooseRewardPresenter.UserInterface, ChooseRewardPresenter>
        implements ChooseRewardPresenter.UserInterface, RewardSelectionViewHolder.OnRewardSelectionToggledListener {

    public static final String CHOOSE_REWARD_UNCLAIMED_REWARD_UNIQUE_ID = "CHOOSE_REWARD_UNCLAIMED_REWARD_UNIQUE_ID";
    public static final String CHOOSE_REWARDS_OUTCOME_REWARD_ID = "CHOOSE_REWARDS_OUTCOME_REWARD_ID";

    @Inject
    ChooseRewardPresenter chooseRewardPresenter;

    private View errorStateView;
    private View voucherContent;
    private ButtonBarConfigurator buttonBarConfigurator;
    private RecyclerView recyclerView;
    private GenericRecyclerViewAdapter<RewardSelectionDTO, RewardSelectionViewHolder> adapter;
    private RewardSelectionDTO selectedReward;
    private EmptyStateViewHolder emptyStateViewHolder;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_choose_reward);

        setUpActionBarWithTitle(R.string.AR_rewards_choose_reward_title_724)
                .setDisplayHomeAsUpEnabled(true);

        voucherContent = findViewById(R.id.voucher_selection_content);
        errorStateView = findViewById(R.id.voucher_selection_retry);

        buttonBarConfigurator = setupButtonBar().setForwardButtonEnabled(false);

        getPresenter().setRewardUniqueId(getIntent().getLongExtra(CHOOSE_REWARD_UNCLAIMED_REWARD_UNIQUE_ID, 0L));

        recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void retryVoucherRequest() {
        if (selectedReward != null) {
            getPresenter().onConfirmRewardChoice(selectedReward);
        } else {
            getPresenter().fetchRewardVoucher();
        }
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        navigationCoordinator.getActiveRewardsDependencyInjector().inject(this);
    }

    @Override
    protected ChooseRewardPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected ChooseRewardPresenter getPresenter() {
        return chooseRewardPresenter;
    }

    @Override
    public void showRewardSelections(List<RewardSelectionDTO> selections) {
        adapter = new GenericRecyclerViewAdapter<>(
                this,
                selections,
                R.layout.ar_reward_selection_option, new RewardSelectionViewHolder.Factory(this));
        recyclerView.setAdapter(adapter);
        ViewUtilities.addDividers(this, recyclerView);
        if (emptyStateViewHolder != null) {
            emptyStateViewHolder.hideEmptyStateAndShowOtherIfHasData(!selections.isEmpty(), voucherContent);
        }
    }

    @Override
    public void onRewardSelectionToggled(RewardSelectionDTO reward, boolean isChecked) {
        if (isChecked && !reward.selected) {
            clearAllSelections();
            reward.selected = true;
            adapter.notifyDataSetChanged();
            selectedReward = reward;
            buttonBarConfigurator.setForwardButtonEnabled(true);
        }
    }

    void clearAllSelections() {
        for (RewardSelectionDTO item : adapter.getData()) {
            item.selected = false;
        }
    }

    public void handleChooseButtonTapped(View view) {
        getPresenter().onConfirmRewardChoice(selectedReward);
    }

    @Override
    public void showLoadingIndicator() {
        errorStateView.setVisibility(View.GONE);
        toggleLoadingIndicator(View.VISIBLE, View.GONE);
    }

    @Override
    public void hideLoadingIndicator() {
        toggleLoadingIndicator(View.GONE, View.VISIBLE);
    }

    private void toggleLoadingIndicator(int loadingIndicatorVisibility, int contentVisibility) {
        findViewById(R.id.loading_indicator).setVisibility(loadingIndicatorVisibility);
        voucherContent.setVisibility(contentVisibility);
        findViewById(R.id.button_bar).setVisibility(contentVisibility);
    }

    @Override
    public void showGenericErrorMessage() {
        emptyStateViewHolder = setupAndShowEmptyState(
                R.string.error_unable_to_load_title_503,
                R.string.error_unable_to_load_message_504);
        hideLoadingIndicator();
    }

    @Override
    public void showConnectionErrorMessage() {
        emptyStateViewHolder = setupAndShowEmptyState(
                R.string.connectivity_error_alert_title_44,
                R.string.connectivity_error_alert_message_45);
        hideLoadingIndicator();
    }

    @NonNull
    private EmptyStateViewHolder setupAndShowEmptyState(int titleId, int messageId) {
        EmptyStateViewHolder viewHolder = new EmptyStateViewHolder(errorStateView);
        viewHolder.setup(titleId, messageId, R.string.try_again_button_title_43)
                .setButtonClickListener(new EmptyStateViewHolder.EmptyStatusButtonClickedListener() {
                    @Override
                    public void onEmptyStateButtonClicked(EmptyStateViewHolder emptyStateViewHolder) {
                        retryVoucherRequest();
                    }
                }).showEmptyStateViewAndHideOtherView(voucherContent);

        return viewHolder;
    }

    @Override
    public void showDataSharingConsent() {
        navigationCoordinator.navigateToActiveRewardsDataSharingConsent(this, selectedReward.rewardUniqueId, selectedReward.rewardId);
    }

    @Override
    public void navigateAfterRewardVoucherSelected(long uniqueId) {
        navigationCoordinator.navigateAfterRewardChoiceConfirmed(this, uniqueId);
    }

}
