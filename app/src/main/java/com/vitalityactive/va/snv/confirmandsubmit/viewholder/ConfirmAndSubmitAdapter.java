package com.vitalityactive.va.snv.confirmandsubmit.viewholder;

import android.app.DatePickerDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.snv.confirmandsubmit.model.ConfirmAndSubmitItemUI;
import com.vitalityactive.va.snv.dto.ConfirmAndSubmitItemDTO;
import com.vitalityactive.va.snv.dto.EventTypeDto;
import com.vitalityactive.va.utilities.date.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ConfirmAndSubmitAdapter extends RecyclerView.Adapter<ConfirmAndSubmitAdapter.ViewHolder> {

    public enum TYPE {
        SCREENINGS,
        VACCINATIONS
    }

    public interface Callback {
        LayoutInflater getLayoutInflaterForAdapter();

        void showDatePickerDialog(DatePickerDialog.OnDateSetListener myDateListener);

        void adjustRecyclerViewHeight(TYPE type, int numItemsChecked);

        String getFormattedDate(LocalDate date);

        void updateButtonBarStatus(boolean status);
    }

    private final String TAG = ConfirmAndSubmitAdapter.class.getName();

    private ConfirmAndSubmitAdapter.Callback adapterCallback;
    private List<ConfirmAndSubmitItemUI> fieldsList;
    private TYPE type;
    private int checkedCounter = 0;


    public ConfirmAndSubmitAdapter(ConfirmAndSubmitAdapter.Callback adapterCallback, List<EventTypeDto> eventTypeDtos, List<ConfirmAndSubmitItemDTO> snvItemsUi, ConfirmAndSubmitAdapter.TYPE type) {
        this.adapterCallback = adapterCallback;
        List<EventTypeDto> noEventsZeroEarnPoints = new ArrayList<>();
        List<EventTypeDto> nonZeroEarnPoints = new ArrayList<>();
        List<EventTypeDto> zeroEarnPoints = new ArrayList<>();
        List<EventTypeDto> finalList = new ArrayList<>();

        for (EventTypeDto eventTypeDto : eventTypeDtos) {
            if (eventTypeDto.getTotalPotentialPoints() > 0) {
                if (eventTypeDto.getEvent().isEmpty() && eventTypeDto.getTotalEarnedPoints() == 0) {
                    noEventsZeroEarnPoints.add(eventTypeDto);
                } else if (eventTypeDto.getTotalEarnedPoints() > 0) {
                    nonZeroEarnPoints.add(eventTypeDto);
                } else if (eventTypeDto.getTotalEarnedPoints() == 0) {
                    zeroEarnPoints.add(eventTypeDto);
                }
            }
        }

        Collections.sort(noEventsZeroEarnPoints);
        Collections.sort(nonZeroEarnPoints);
        Collections.sort(zeroEarnPoints);

        finalList.addAll(noEventsZeroEarnPoints);
        finalList.addAll(nonZeroEarnPoints);
        finalList.addAll(zeroEarnPoints);

        fieldsList = new ArrayList<>();

        for (EventTypeDto eventTypeDto : finalList) {
            ConfirmAndSubmitItemUI item = new ConfirmAndSubmitItemUI(eventTypeDto.getTypeName(), "", false, eventTypeDto.getTypeKey(), 0);
            ConfirmAndSubmitItemDTO itemDTO = findMatchingCheckboxState(item, snvItemsUi);

            if (itemDTO != null) {
                item.setDateTested(itemDTO.getDateTested());
                item.setDateTestedInLong(itemDTO.getDateTestedInLong());
                item.setEnabled(itemDTO.isEnabled());
            }

            fieldsList.add(item);
        }
        this.type = type;
    }

    private ConfirmAndSubmitItemDTO findMatchingCheckboxState(ConfirmAndSubmitItemUI item, List<ConfirmAndSubmitItemDTO> snvItemsUi) {
        for (ConfirmAndSubmitItemDTO confirmAndSubmitItemDTO : snvItemsUi) {
            if (item.getFieldTitle().equalsIgnoreCase(confirmAndSubmitItemDTO.getFieldTitle())) {
                return confirmAndSubmitItemDTO;
            }
        }
        return null;
    }

    @Override
    public ConfirmAndSubmitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = adapterCallback.getLayoutInflaterForAdapter().inflate(R.layout.snv_confirm_submit_item, parent, false);
        return new ConfirmAndSubmitAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConfirmAndSubmitAdapter.ViewHolder holder, int position) {
        Calendar nonUserfacingDate = Calendar.getInstance();
        LocalDate userFacingDate = new LocalDate(nonUserfacingDate.get(Calendar.YEAR), getMonthInOneIndex(nonUserfacingDate.get(Calendar.MONTH)), nonUserfacingDate.get(Calendar.DAY_OF_MONTH));
        String currentDate = adapterCallback.getFormattedDate(userFacingDate);

        final ConfirmAndSubmitItemUI field = fieldsList.get(position);
        final TextView dateTestedView = holder.dateTested;
        field.setDateTested(currentDate);
        field.setDateTestedInLong(nonUserfacingDate.getTime().getTime());

        holder.title.setText(field.getFieldTitle());
        holder.dateTested.setText(field.getDateTested());
        holder.fieldCheckBox.setChecked(field.isEnabled());

        final RelativeLayout dateLayout = holder.dateLayout;

        if (field.isEnabled()) {
            incrementCheckCounter();
            dateLayout.setVisibility(View.VISIBLE);
            adapterCallback.adjustRecyclerViewHeight(type, checkedCounter);
            adapterCallback.updateButtonBarStatus(checkedCounter > 0);
        }


        holder.fieldCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    incrementCheckCounter();
                    dateLayout.setVisibility(View.VISIBLE);
                    field.setEnabled(true);
                } else {
                    decrementCheckCounter();
                    dateLayout.setVisibility(View.GONE);
                    field.setEnabled(false);
                }
                adapterCallback.adjustRecyclerViewHeight(type, checkedCounter);
                adapterCallback.updateButtonBarStatus(checkedCounter > 0);
            }
        });

        final DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int dayOfMonth) {
                Calendar nonUserfacingDate = Calendar.getInstance();
                nonUserfacingDate.set(year, month, dayOfMonth);

                LocalDate userFacingDate = new LocalDate(year, getMonthInOneIndex(month), dayOfMonth);
                String formattedDate = adapterCallback.getFormattedDate(userFacingDate);

                dateTestedView.setText(formattedDate);
                field.setDateTestedInLong(nonUserfacingDate.getTime().getTime());
                field.setDateTested(formattedDate);
            }
        };

        holder.dateTested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterCallback.showDatePickerDialog(myDateListener);
            }
        });
    }

    private int getMonthInOneIndex(int month) {
        return month + 1;
    }

    public List<ConfirmAndSubmitItemUI> getAllConfirmAndSubmitItems() {
        List<ConfirmAndSubmitItemUI> checkedItems = new ArrayList<>();
        for (ConfirmAndSubmitItemUI itemUi : fieldsList) {
            if (itemUi.isEnabled()) {
                checkedItems.add(itemUi);
            }
        }
        return checkedItems;
    }

    private void incrementCheckCounter() {
        checkedCounter++;
        if (checkedCounter > getItemCount()) {
            checkedCounter = getItemCount();
        }
    }

    private void decrementCheckCounter() {
        checkedCounter--;
        if (checkedCounter < 0) {
            checkedCounter = 0;
        }
    }

    @Override
    public int getItemCount() {
        return fieldsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final CheckBox fieldCheckBox;
        private final TextView dateTested;
        private final RelativeLayout dateLayout;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.snv_item_field_txt);
            fieldCheckBox = view.findViewById(R.id.snv_unit_checkbox);
            dateTested = view.findViewById(R.id.snv_date_tested);
            dateLayout = view.findViewById(R.id.snv_date_layout);
            dateLayout.setVisibility(View.GONE);
        }
    }
}
