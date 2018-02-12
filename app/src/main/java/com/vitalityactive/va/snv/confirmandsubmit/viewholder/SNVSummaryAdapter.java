package com.vitalityactive.va.snv.confirmandsubmit.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.snv.confirmandsubmit.model.PresentableSNVField;
import com.vitalityactive.va.snv.dto.ConfirmAndSubmitItemDTO;

import java.util.ArrayList;
import java.util.List;

public class SNVSummaryAdapter extends RecyclerView.Adapter<SNVSummaryAdapter.ViewHolder> {

    public enum TYPE {
        SCREENINGS,
        VACCINATIONS
    }

    public interface Callback {
        LayoutInflater getLayoutInflaterForAdapter();

        void adjustRecyclerViewHeight(TYPE type, int numItemsChecked);
    }

    private final String TAG = SNVSummaryAdapter.class.getName();

    private SNVSummaryAdapter.Callback adapterCallback;
    private List<PresentableSNVField> fieldsList;
    private TYPE type;


    public SNVSummaryAdapter(SNVSummaryAdapter.Callback adapterCallback, List<ConfirmAndSubmitItemDTO> confirmAndSubmitItemDTOS, SNVSummaryAdapter.TYPE type) {
        this.adapterCallback = adapterCallback;
        fieldsList = new ArrayList<>();

        for (ConfirmAndSubmitItemDTO confirmAndSubmitItemDTO : confirmAndSubmitItemDTOS) {
            PresentableSNVField item = new PresentableSNVField(confirmAndSubmitItemDTO.getFieldTitle(), confirmAndSubmitItemDTO.getDateTested());
            fieldsList.add(item);
        }

        this.type = type;
    }

    @Override
    public SNVSummaryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = adapterCallback.getLayoutInflaterForAdapter().inflate(R.layout.snv_summary_item, parent, false);
        return new SNVSummaryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SNVSummaryAdapter.ViewHolder holder, int position) {
        PresentableSNVField field = fieldsList.get(position);
        holder.title.setText(field.getFieldName());
        holder.dateTested.setText(field.getCaptureDate());
    }

    @Override
    public int getItemCount() {
        return fieldsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView dateTested;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.snv_summary_date_tested);
            dateTested = view.findViewById(R.id.snv_measurement_item_date_title);
        }
    }
}
