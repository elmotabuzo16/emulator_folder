package com.vitalityactive.va.vitalitystatus.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.vitalitystatus.landing.StatusItem;

public class StatusHeaderWithProgressAndButtonViewHolder extends StatusHeaderViewHolder {
    private final boolean shouldShowMyRewards;
    private ProgressBar progressBar;
    private Button myRewardsButton;

    private StatusHeaderWithProgressAndButtonViewHolder(View itemView, boolean shouldShowMyRewards) {
        super(itemView, 0);
        this.shouldShowMyRewards = shouldShowMyRewards;
    }

    @Override
    public void bindWith(Object data) {
        StatusItem statusItem = (StatusItem) data;

        super.bindWith(data);

        progressBar.setProgress(statusItem.getProgress());

        if (shouldShowMyRewards) {
            ViewUtilities.setViewVisible(myRewardsButton);
        }
    }

    @Override
    protected boolean shouldCreateConfetti() {
        return false;
    }

    @Override
    protected void assignViews(View itemView) {
        super.assignViews(itemView);
        progressBar = itemView.findViewById(R.id.status_progress_bar);
        myRewardsButton = itemView.findViewById(R.id.my_rewards_button);
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<Object,
            StatusHeaderWithProgressAndButtonViewHolder> {

        private boolean shouldShowMyRewards;

        public Factory(boolean shouldShowMyRewards) {
            this.shouldShowMyRewards = shouldShowMyRewards;
        }

        @Override
        public StatusHeaderWithProgressAndButtonViewHolder createViewHolder(View itemView) {
            return new StatusHeaderWithProgressAndButtonViewHolder(itemView, shouldShowMyRewards);
        }
    }
}
