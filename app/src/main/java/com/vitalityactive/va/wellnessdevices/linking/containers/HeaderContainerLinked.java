package com.vitalityactive.va.wellnessdevices.linking.containers;


import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.date.Date;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

import org.threeten.bp.format.DateTimeParseException;

import static org.threeten.bp.temporal.ChronoUnit.DAYS;

public class HeaderContainerLinked extends GenericRecyclerViewAdapter.ViewHolder<String> {
    private final TextView tvLinkedAt;

    public HeaderContainerLinked(View itemView) {
        super(itemView);
        tvLinkedAt = itemView.findViewById(R.id.tv_wd_linking_synced_at);
    }

    @Override
    public void bindWith(String dateSyncedString) {
        // No way to check for lastSync that is not a date, thus the try catch :(
        try {
            Date dateSynced = new Date(dateSyncedString);

            LocalDate today = LocalDate.now();

            long daysSinceLastSync = DAYS.between(dateSynced.getValue().toLocalDate().atStartOfDay(), today.getValue().atStartOfDay());

            String day = "";
            if (daysSinceLastSync == 0) {
                day = itemView.getResources().getString(R.string.WDA_device_detail_last_synced_today_474);
            }

            if (TextUtilities.isNullOrWhitespace(day)) {
                tvLinkedAt.setText(DateFormattingUtilities.formatDateMonthYearHoursMinutes(itemView.getContext(), dateSynced));
            } else {
                tvLinkedAt.setText(DateFormattingUtilities.formatHoursMinutes(itemView.getContext(), dateSynced));
            }
        } catch (DateTimeParseException ex) {
            tvLinkedAt.setText(R.string.WDA_device_detail_not_synced_493);

        }
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<String, HeaderContainerLinked> {

        @Override
        public HeaderContainerLinked createViewHolder(View itemView) {
            return new HeaderContainerLinked(itemView);
        }
    }
}