package com.vitalityactive.va.activerewards.rewards;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.EmptyStateViewHolder;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.rewards.dto.RewardSelectionDTO;
import com.vitalityactive.va.activerewards.rewards.presenters.WheelSpinPresenter;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.uicomponents.slotmachine.SlotMachineView;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.List;

import javax.inject.Inject;

public class WheelSpinActivity extends BasePresentedActivity<WheelSpinPresenter.UserInterface, WheelSpinPresenter>
        implements SlotMachineView.FlingListener, SlotMachineView.FinishScrollListener, WheelSpinPresenter.UserInterface {
    private static final String TAG = "WheelSpinActivity";
    public static final String WHEEL_SPIN_UNCLAIMED_REWARD_UNIQUE_ID = "WHEEL_SPIN_UNCLAIMED_REWARD_UNIQUE_ID";
    public static final String WHEEL_SPIN_OUTCOME_REWARD_ID = "WHEEL_SPIN_OUTCOME_REWARD_ID";

    @Inject
    WheelSpinPresenter wheelSpinPresenter;

    private SlotMachineView slotMachineView;
    private EmptyStateViewHolder emptyStateViewHolder;
    private View content;
    private View loadingIndicator;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_wheel_spin);
        setUpActionBarWithTitle(R.string.AR_rewards_spin_title_719)
                .setDisplayHomeAsUpEnabled(true);
        slotMachineView = findViewById(R.id.wheel);
        content = findViewById(R.id.content);
        loadingIndicator = findViewById(R.id.loading_indicator);
        setupSlotMachine();
        wheelSpinPresenter.setRewardUniqueId(getIntent().getLongExtra(WHEEL_SPIN_UNCLAIMED_REWARD_UNIQUE_ID, 0L));
        setUpEmptyState();
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
    protected void pause() {
        if (slotMachineView != null) {
            Log.d(TAG, "cancel spin");
            slotMachineView.cancelSpin();
        }
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        navigationCoordinator.getActiveRewardsDependencyInjector().inject(this);
    }

    @Override
    protected WheelSpinPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected WheelSpinPresenter getPresenter() {
        return wheelSpinPresenter;
    }

    private void setupSlotMachine() {
        slotMachineView.setupFlingListener(3000, this);
        slotMachineView.setSelectorHighlight(getSelectorHighlightDrawable());
        slotMachineView.setOnItemSelectedListener(new SlotMachineView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(SlotMachineView picker, RewardSelectionDTO data, int position) {
                navigationCoordinator.navigateAfterWheelSpin(WheelSpinActivity.this, getPresenter().getWheelSpin());
            }
        });
    }

    @Override
    public void showRewardSelections(List<RewardSelectionDTO> rewardSelections, int outcomeSelectionIndex) {
        slotMachineView.setItems(rewardSelections);
        slotMachineView.setDesiredItemPositionAfterRotation(outcomeSelectionIndex);
    }

    @Override
    public void showGenericErrorMessage() {
        emptyStateViewHolder.showEmptyStateViewAndHideOtherView(content);
    }

    @Override
    public void showConnectionErrorMessage() {
        emptyStateViewHolder.showEmptyStateViewAndHideOtherView(content);
    }

    @Override
    public void showLoadingIndicator() {
        ViewUtilities.setViewVisible(loadingIndicator);
        ViewUtilities.setViewGone(content);
    }

    @Override
    public void hideLoadingIndicator() {
        ViewUtilities.setViewVisible(content);
        ViewUtilities.setViewGone(loadingIndicator);
    }

    private Drawable getSelectorHighlightDrawable() {
        return ResourcesCompat.getDrawable(getResources(), R.drawable.spin_selected_highlight, getTheme());
    }

    @Override
    public void onFling(SlotMachineView view, float velocityY) {
        int predeterminedItemIndex = 2;
        int distance = view.calculateDistanceToCenterItem(predeterminedItemIndex);
        distance += view.calculateDistanceToCompleteLoops(10);
        view.setFinishScrollListener(this);
        view.startScroll(distance, 2500);
    }

    @Override
    public void onScrollFinished(SlotMachineView view, int selectedItemIndex) {
        Log.i(TAG, "you have won the item @ " + selectedItemIndex);
        view.setFinishScrollListener(null);

        navigationCoordinator.navigateAfterWheelSpin(this, getPresenter().getWheelSpin());
    }

    public void onSpinNowClicked(View view){
        slotMachineView.flingProgrammatically();
    }

    public void onParticipatingPartnersClicked(View view){
        navigationCoordinator.navigateOnParticipatingPartnersFromWheelSpin(this);
    }
}
