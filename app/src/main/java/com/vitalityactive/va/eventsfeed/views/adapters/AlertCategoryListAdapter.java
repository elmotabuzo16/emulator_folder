package com.vitalityactive.va.eventsfeed.views.adapters;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.eventsfeed.EventsFeedAlertDialogFragment;
import com.vitalityactive.va.utilities.TextUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jayellos on 12/6/17.
 */

public class AlertCategoryListAdapter extends RecyclerView.Adapter<AlertCategoryListAdapter.AlertListItemViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<EventsFeedAlertDialogFragment.AlertDialogItem> alertDialogItems;
    private final int globalTintColor;

    public AlertCategoryListAdapter(LayoutInflater layoutInflater, List<EventsFeedAlertDialogFragment.AlertDialogItem> alertDialogItems, EventsFeedAlertDialogFragment dialog, int globalTintColor) {
        this.layoutInflater = layoutInflater;
        this.alertDialogItems = alertDialogItems;
        this.globalTintColor = globalTintColor;
    }

    @Override
    public AlertListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlertListItemViewHolder(layoutInflater.inflate(viewType, parent, false), globalTintColor);
    }

    @Override
    public void onBindViewHolder(AlertListItemViewHolder holder, int position) {
        holder.bindWith(alertDialogItems.get(position), this);
    }

    @Override
    public int getItemCount() {
        return alertDialogItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.alert_dialog_list_item;
    }

    void clearAllCheckedItems() {
        for (EventsFeedAlertDialogFragment.AlertDialogItem item : alertDialogItems) {
            item.setChecked(false);
        }
    }

    List<EventsFeedAlertDialogFragment.AlertDialogItem> getCheckedItems(){
        List<EventsFeedAlertDialogFragment.AlertDialogItem> checkedItems = new ArrayList<>();

        for (EventsFeedAlertDialogFragment.AlertDialogItem item : alertDialogItems) {
            if (item.isChecked()) {
                checkedItems.add(item);
            }
        }

        return checkedItems;
    }

    EventsFeedAlertDialogFragment.AlertDialogItem getCheckedItem() {
        for (EventsFeedAlertDialogFragment.AlertDialogItem item : alertDialogItems) {
            if (item.isChecked()) {
                return item;
            }
        }
        return null;
    }

    public void unCheckAll() {
        for (EventsFeedAlertDialogFragment.AlertDialogItem item : alertDialogItems) {
            if(item.getIdentifier() != 999) {
                item.setChecked(false);
            }
        }

        notifyDataSetChanged();
    }

    public void checkAll() {
        for (EventsFeedAlertDialogFragment.AlertDialogItem item : alertDialogItems) {
            item.setChecked(true);
        }

        notifyDataSetChanged();
    }

    public void uncheck(int identifier){
        for (EventsFeedAlertDialogFragment.AlertDialogItem item : alertDialogItems) {
            if(item.getIdentifier() == identifier) {
                item.setChecked(false);
                break;
            }
        }
        notifyDataSetChanged();
    }

    protected class AlertListItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final RadioButton radioButton;
        private final CheckBox checkBox;
        private final TextView description;

        AlertListItemViewHolder(View itemView, int globalTintColor) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            radioButton = itemView.findViewById(R.id.radio_button);
            checkBox = itemView.findViewById(R.id.check_box);
            radioButton.setVisibility(View.GONE);
            checkBox.setVisibility(View.VISIBLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setRadioButtonColors(globalTintColor);
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private void setRadioButtonColors(int globalTintColor) {
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked}
                    },
                    new int[]{ContextCompat.getColor(itemView.getContext(), R.color.light_secondary_54), globalTintColor}
            );

            radioButton.setButtonTintList(colorStateList);
            checkBox.setButtonTintList(colorStateList);
        }

        public void bindWith(final EventsFeedAlertDialogFragment.AlertDialogItem dataItem, final AlertCategoryListAdapter adapter) {
            radioButton.setChecked(dataItem.isChecked());
            checkBox.setChecked(dataItem.isChecked());

            title.setText(dataItem.getTitle());
            title.setCompoundDrawablesWithIntrinsicBounds(dataItem.getIconResourceId(), 0, 0, 0);
            if (TextUtilities.isNullOrWhitespace(dataItem.getDescription())) {
                description.setVisibility(View.GONE);
            } else {
                description.setText(dataItem.getDescription());
                description.setVisibility(View.VISIBLE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    checkBox.setChecked(!checkBox.isChecked());
                    dataItem.setChecked(checkBox.isChecked());

                    if(dataItem.getIdentifier() == 999) {
                        if (!checkBox.isChecked()) {
//                                adapter.unCheckAll();
                        }else{
//                                adapter.checkAll();
                            adapter.unCheckAll();
                        }
                    }else if(checkBox.isChecked()){
                        adapter.uncheck(999);
                    }
                }
            });
        }
    }
}
