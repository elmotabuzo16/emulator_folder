package com.vitalityactive.va.activerewards.history.uicontainers;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.GoalTrackerOutDto;
import com.vitalityactive.va.activerewards.utilities.IconUtilities;
import com.vitalityactive.va.constants.GoalTrackerStatus;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

public class ActivityItemViewHolder extends GenericRecyclerViewAdapter.ViewHolder<GoalTrackerOutDto> {
    protected final ImageView icon;
    protected final TextView status;
    protected final TextView points;
    protected final TextView date;

    public ActivityItemViewHolder(View itemView) {
        super(itemView);
        icon = (ImageView) itemView.findViewById(R.id.iv_ar_icon);
        status = (TextView) itemView.findViewById(R.id.tv_ar_status);
        points = (TextView) itemView.findViewById(R.id.tv_ar_points);
        date = (TextView) itemView.findViewById(R.id.tv_ar_date);
    }

    @Override
    public void bindWith(GoalTrackerOutDto item) {
       // setIcon(item);
        icon.setImageResource(getIconId(item));
        status.setText(getStatus(item));
        points.setText(points.getContext().getString(R.string.AR_home_card_in_progress_title_773, getPointsAchieved(item), getPointsTarget(item)));
        date.setText(createDateRange(itemView, item));
    }

    private String createDateRange(View itemView, GoalTrackerOutDto item) {
            return DateFormattingUtilities.formatRangeDateMonthAbbreviatedYearOptional(
                    itemView.getContext(),
                    new LocalDate(item.getEffectiveFrom()),
                    new LocalDate(item.getEffectiveTo()),
                    false);
    }

    protected void setIcon(GoalTrackerOutDto item){
        int color = ResourcesCompat.getColor(itemView.getContext().getResources(), IconUtilities.getIconColor(getStatusCode(item)), itemView.getContext().getTheme());
        ViewUtilities.tintDrawable(ContextCompat.getDrawable(itemView.getContext(), IconUtilities.getIconId(getStatusCode(item))), color);
        icon.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), IconUtilities.getIconId(getStatusCode(item))));
    }

    protected int getStatusCode(GoalTrackerOutDto item) {
        return item.getGoalTrackerStatusKey();
    }

    protected
    @StringRes
    int getStatus(GoalTrackerOutDto item) {
        final int statusCode = getStatusCode(item);
        switch (statusCode) {
            case GoalTrackerStatus._ACHIEVED:
            case GoalTrackerStatus._MANUALLYACHIEVED:
                return R.string.AR_Goal_History_goal_achieved_title_692;
            case GoalTrackerStatus._INPROGRESS:
                return R.string.AR_goal_in_progress_title_726;
            case GoalTrackerStatus._NOTACHIEVED:
                return R.string.AR_goal_history_not_achieved_title_709;
            case GoalTrackerStatus._PENDING:
                return R.string.AR_goal_pending_title_693;
            case GoalTrackerStatus._CANCELLED:
            case GoalTrackerStatus._MANUALCANCELLATION:
            case GoalTrackerStatus._SYSTEMCANCELLATION:
                return R.string.AR_goal_history_not_achieved_title_709;
            default:
                return R.string.AR_Goal_History_goal_achieved_title_692;
        }
    }

    protected int getPointsAchieved(GoalTrackerOutDto item) {
        return item.getObjectiveTrackers().get(0).getPointsAchieved();
    }

    protected int getPointsTarget(GoalTrackerOutDto item) {
        return item.getObjectiveTrackers().get(0).getPointsTarget();
    }

    protected
    @DrawableRes
    int getIconId(GoalTrackerOutDto item) {
        final int statusCode = getStatusCode(item);
        switch (statusCode) {
            case GoalTrackerStatus._ACHIEVED:
            case GoalTrackerStatus._MANUALLYACHIEVED:
                return R.drawable.learnmore_activate;
            case GoalTrackerStatus._INPROGRESS:
                return R.drawable.ar_goal_in_progress;
            case GoalTrackerStatus._NOTACHIEVED:
                return R.drawable.notachieved;
            case GoalTrackerStatus._PENDING:
                return R.drawable.ar_goal_pending;
            case GoalTrackerStatus._CANCELLED:
            case GoalTrackerStatus._MANUALCANCELLATION:
            case GoalTrackerStatus._SYSTEMCANCELLATION:
                return R.drawable.notachieved;
            default:
                return R.drawable.learnmore_activate;
        }
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<GoalTrackerOutDto, ActivityItemViewHolder> {
        @Override
        public ActivityItemViewHolder createViewHolder(View itemView) {
            return new ActivityItemViewHolder(itemView);
        }
    }

}
