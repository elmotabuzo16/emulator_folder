package com.vitalityactive.va.home.activerewardcard;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.CircleGoalProgressView;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

class ActiveRewardsProgressViewHolder extends GenericRecyclerViewAdapter.ViewHolder<ActiveRewardsCardFragment> {
    private final int ANIMATION_DURATION = 900;
    private TextView scoreProgress;
    private TextView scoreDates;
    private TextView startDate;
    private int currentLayoutId;
    private TextView pointsTextInCircle;

    ActiveRewardsProgressViewHolder(View view) {
        super(view);
        setupFields(view);
    }

    private void setupFields(View view) {
        scoreProgress = view.findViewById(R.id.score_progress);
        scoreDates = view.findViewById(R.id.score_week_date);
        startDate = view.findViewById(R.id.rewards_start_date);
        pointsTextInCircle = view.findViewById(R.id.points_text);
    }

    @Override
    public void layoutFor(final ActiveRewardsCardFragment dataItem) {
        int newLayoutId = getNewLayoutId(dataItem);
        if (currentLayoutId == newLayoutId)
            return;
        currentLayoutId = newLayoutId;

        ViewGroup viewGroup = (ViewGroup) this.itemView;
        viewGroup.removeAllViews();
        View view = LayoutInflater.from(itemView.getContext()).inflate(newLayoutId, viewGroup);
        setupFields(view);
    }

    private int getNewLayoutId(ActiveRewardsCardFragment dataItem) {
        switch (dataItem.getState()) {
            case SHOULD_COMPLETE_VHR:
                return R.layout.active_rewards_home_card_getstarted;
            case NOT_ACTIVATED_GET_STARTED:
                return R.layout.active_rewards_home_card_getstarted;
            case ACTIVATED_WILL_START_SOON:
                return R.layout.active_rewards_home_card_activated;
            case IN_PROGRESS:
                return R.layout.active_rewards_home_card_inprogress;
            case ACHIEVED:
                return R.layout.active_rewards_home_card_achieved;
            default:
                return 0;
        }
    }

    @Override
    public void bindWith(ActiveRewardsCardFragment dataItem) {
        switch (dataItem.getState()) {
            case SHOULD_COMPLETE_VHR:
                // NOP
                break;
            case NOT_ACTIVATED_GET_STARTED:
                // no-op
                break;
            case ACTIVATED_WILL_START_SOON:
                setStartDate(dataItem);
                break;
            case IN_PROGRESS:
                setProgress(dataItem.points, dataItem.requiredPoints);
                setCircleGoalProgress(dataItem);
                animatePointsValue(dataItem.points, shouldHidePointsAfterAnimation(dataItem, dataItem.points));
                setRewardsDateRange(dataItem);
                break;
            case ACHIEVED:
                setProgress(dataItem.points, dataItem.requiredPoints);
                animatePointsValue(dataItem.points, shouldHidePointsAfterAnimation(dataItem, dataItem.points));
                setCircleGoalProgress(dataItem);
                break;
        }
    }

    private boolean shouldHidePointsAfterAnimation(ActiveRewardsCardFragment dataItem, int value) {
        return value == dataItem.requiredPoints && value != 0;
    }

    private void animatePointsValue(final int value, final boolean hidePointsAfterAnimation) {
        pointsTextInCircle.setVisibility(View.VISIBLE);
        ValueAnimator animator = ValueAnimator.ofInt(0, value);
        animator.setDuration(ANIMATION_DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                pointsTextInCircle.setText(animation.getAnimatedValue().toString());
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (hidePointsAfterAnimation) {
                    pointsTextInCircle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    private void setCircleGoalProgress(ActiveRewardsCardFragment dataItem) {
        CircleGoalProgressView circleGoalProgressView = itemView.findViewById(R.id.large_logo);
        circleGoalProgressView.setMaxValue(dataItem.requiredPoints);
        circleGoalProgressView.setValueAnimated(dataItem.points, ANIMATION_DURATION);
    }

    private void setStartDate(ActiveRewardsCardFragment dataItem) {
        Resources resources = this.startDate.getResources();
        String formattedDate = dataItem.getLongFormattedStartDate();
        this.startDate.setText(resources.getString(R.string.AR_home_card_activated_start_date_776, formattedDate));
    }

    private void setProgress(int points, int requiredPoints) {
        Resources resources = scoreProgress.getResources();
        String text = resources.getString(R.string.AR_home_card_in_progress_title_773, points, requiredPoints);
        scoreProgress.setText(text);
    }

    private void setRewardsDateRange(ActiveRewardsCardFragment dataItem) {
        this.scoreDates.setText(dataItem.getLongFormattedRange());
    }
}
