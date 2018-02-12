package com.vitalityactive.va.activerewards.viewholder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.CircleGoalProgressView;
import com.vitalityactive.va.activerewards.dto.ActivityItem;
import com.vitalityactive.va.activerewards.dto.WeeklyTargetItem;
import com.vitalityactive.va.activerewards.history.detailedscreen.ActivityListContainer;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.util.Collections;
import java.util.List;

public class ActiveRewardsWeeklyTargetViewHolder extends GenericRecyclerViewAdapter.ViewHolder<WeeklyTargetItem> {
    private static final int ANIMATION_DURATION = 900;
    private final TextView willStart;
    private final CircleGoalProgressView circleGoalProgressView;
    private final TextView pointsText;
    private final TextView pointsValue;
    private ActivityListContainer activityListContainer;

    private ActiveRewardsWeeklyTargetViewHolder(View itemView, Builder builder) {
        super(itemView);
        willStart = itemView.findViewById(R.id.txtWillStart);
        circleGoalProgressView = itemView.findViewById(R.id.circleGoalProgressView);
        pointsValue = itemView.findViewById(R.id.points_value);
        pointsText = itemView.findViewById(R.id.points_text);
        RecyclerView recyclerView = itemView.findViewById(R.id.recycler_view);

        activityListContainer = new ActivityListContainer(itemView.getContext(),
                itemView.getContext().getString(R.string.AR_landing_this_weeks_activity_section_header_title_660),
                builder.onItemClickListener);
        activityListContainer.setShouldAddDividers(true);
        activityListContainer.setDividersLeftPaddingPx((int) itemView.getContext().getResources().getDimension(R.dimen.card_horizontal_margin_medium));

        if (builder.data == null || builder.data.isEmpty()) {
            recyclerView.setAdapter(getAdapterEmpty(itemView.getContext()));
        } else {
            recyclerView.setAdapter(getAdapterWithData(itemView.getContext(), builder.data));
        }
    }

    @NonNull
    private GenericRecyclerViewAdapter getAdapterWithData(Context context,
                                                          List<ActivityItem> data) {
        activityListContainer.setActivityList(data);
        return new GenericRecyclerViewAdapter<>(context,
                activityListContainer,
                R.layout.view_ar_activity_list,
                new GenericTitledListContainerWithAdapter.Factory<>());
    }

    @NonNull
    private GenericRecyclerViewAdapter<ActivityItem, ? extends GenericRecyclerViewAdapter.ViewHolder<ActivityItem>> getAdapterEmpty(Context context) {
        return new GenericRecyclerViewAdapter<>(context,
                Collections.singletonList(new ActivityItem(null, null, null, null)),
                R.layout.view_ar_no_activity_this_week,
                new EmptyActivityListContainer.Factory());
    }

    @Override
    @SuppressLint("StringFormatInvalid")
    public void bindWith(WeeklyTargetItem dataItem) {
        willStart.setText(dataItem.getRange());
        circleGoalProgressView.setValueAnimated(dataItem.getInitialPoints(), Float.parseFloat(dataItem.getPointsAchieved()), ANIMATION_DURATION);
        circleGoalProgressView.setMaxValue(dataItem.getPointsTarget());
        int value = Integer.parseInt(dataItem.getPointsAchieved());
        animatePointsValue(value, dataItem.getInitialPoints(), shouldHidePointsAfterAnimation(dataItem, value));
        String pointsMessage = String.format(itemView.getContext().getString(R.string.AR_goal_progress_message_647), dataItem.getPointsTarget());
        pointsText.setText(pointsMessage);
    }

    private boolean shouldHidePointsAfterAnimation(WeeklyTargetItem dataItem, int value) {
        return value == dataItem.getPointsTarget() && value != 0;
    }

    private void animatePointsValue(final int value, final int initialValue, final boolean hidePointsAfterAnimation) {
        pointsValue.setVisibility(View.VISIBLE);
        pointsText.setVisibility(View.VISIBLE);
        ValueAnimator animator = ValueAnimator.ofInt(initialValue, value);
        animator.setDuration(ANIMATION_DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                pointsValue.setText(animation.getAnimatedValue().toString());
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (hidePointsAfterAnimation) {
                    pointsValue.setVisibility(View.GONE);
                    pointsText.setVisibility(View.GONE);
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

    public static class Builder {
        private GenericRecyclerViewAdapter.OnItemClickListener<ActivityItem> onItemClickListener;
        private List<ActivityItem> data;

        public Builder setActivityItemViewHolderFactory() {
            return this;
        }

        public Builder setOnItemClickListener(GenericRecyclerViewAdapter.OnItemClickListener<ActivityItem> onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            return this;
        }

        public Builder setData(List<ActivityItem> data) {
            this.data = data;
            return this;
        }

        public ActiveRewardsWeeklyTargetViewHolder build(View itemView) {
            return new ActiveRewardsWeeklyTargetViewHolder(itemView, this);
        }
    }
}
