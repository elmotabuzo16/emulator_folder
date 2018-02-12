package com.vitalityactive.va.activerewards.landing.uicontainers;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.ActivityItem;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.date.Date;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

import static org.threeten.bp.temporal.ChronoUnit.DAYS;

public class ActiveRewardsActivityItemViewHolder extends GenericRecyclerViewAdapter.ViewHolder<ActivityItem> {
    private final TextView title;
    private final TextView description;
    private final TextView date;
    private final TextView device;

    private ActiveRewardsActivityItemViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.points_title);
        description = itemView.findViewById(R.id.description);
        date = itemView.findViewById(R.id.date);
        device = itemView.findViewById(R.id.device);
    }

    @Override
    public void bindWith(ActivityItem dataItem) {
        title.setText(dataItem.getTitle());
        description.setText(dataItem.getDescription());

        Date activityDate = new Date(dataItem.getDate());

        LocalDate today = LocalDate.now();

        long daysBetweenTodayAndActivityDate = DAYS.between(activityDate.getValue().toLocalDate().atStartOfDay(), today.getValue().atStartOfDay());

        this.date.setText(getFormattedDate(activityDate, daysBetweenTodayAndActivityDate));
        device.setText(dataItem.getDevice());
        if (!TextUtils.isEmpty(dataItem.getDevice())) {
            device.setVisibility(View.VISIBLE);
        }
    }

    private String getFormattedDate(Date activityDate, long daysBetweenTodayAndActivityDate) {
        String formattedDate;
        if (daysBetweenTodayAndActivityDate == 0) {
            formattedDate = itemView.getContext().getString(R.string.AR_landing_date_indication_today_781);
        } else {
            formattedDate = DateFormattingUtilities.formatDateAbbreviatedWeekdayAbbreviatedMonth(itemView.getContext(),
                    new LocalDate(activityDate.getYear(), activityDate.getMonth(), activityDate.getDayOfMonth()));
        }
        return formattedDate;
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<ActivityItem, ActiveRewardsActivityItemViewHolder> {
        @Override
        public ActiveRewardsActivityItemViewHolder createViewHolder(View itemView) {
            return new ActiveRewardsActivityItemViewHolder(itemView);
        }
    }

}
