package com.vitalityactive.va.wellnessdevices.landing.uicontainers;

import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.date.Date;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;

import org.threeten.bp.format.DateTimeParseException;

import static org.threeten.bp.temporal.ChronoUnit.DAYS;

class LinkedDeviceViewHolder extends GenericRecyclerViewAdapter.ViewHolder<PartnerDto> {
    private final TextView tvTitle;
    private final TextView tvSyncedTime;

    private LinkedDeviceViewHolder(View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvSyncedTime = itemView.findViewById(R.id.tvSyncedTime);
    }

    @Override
    public void bindWith(PartnerDto partner) {
        tvTitle.setText(partner.getDevice());

        // No way to check for lastSync that is not a date, thus the try catch :(
        try {
            Date dateSynced = new Date(partner.getPartnerLastSync());

            LocalDate today = LocalDate.now();

            long daysSinceLastSync = DAYS.between(dateSynced.getValue().toLocalDate().atStartOfDay(), today.getValue().atStartOfDay());

            String day;
            String formattedDate = DateFormattingUtilities.formatHoursMinutes(itemView.getContext(), dateSynced);

            if (daysSinceLastSync == 0) {
                day = itemView.getResources().getString(R.string.WDA_last_synced_today_458);
            } else {
                day = itemView.getResources().getString(R.string.WDA_last_synced_457);
                formattedDate = DateFormattingUtilities.formatDateMonthYearHoursMinutes(itemView.getContext(), dateSynced);
            }

            tvSyncedTime.setText(String.format(day, formattedDate));
        } catch (DateTimeParseException ex) {
            tvSyncedTime.setText(R.string.WDA_device_detail_not_synced_493);
        }
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<PartnerDto, LinkedDeviceViewHolder> {
        @Override
        public LinkedDeviceViewHolder createViewHolder(View itemView) {
            return new LinkedDeviceViewHolder(itemView);
        }
    }

}
