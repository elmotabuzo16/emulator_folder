package com.vitalityactive.va.uicomponents;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.ServiceLocator;
import com.vitalityactive.va.utilities.TextUtilities;

import java.util.ArrayList;
import java.util.List;

public class AlertDialogFragment extends DialogFragment {
    private static final String TYPE = "Type";
    private static final String HEADER_TEXT = "HeaderText";
    private static final String MESSAGE_TEXT = "MessageText";
    private static final String NEGATIVE_BUTTON_TEXT = "NegativeButtonText";
    private static final String NEUTRAL_BUTTON_TEXT = "NeutralButtonText";
    private static final String POSITIVE_BUTTON_TEXT = "PositiveButtonText";
    private static final String ALERT_DIALOG_ITEMS = "AlertDialogItems";
    private boolean dismissedByButton;
    private OnItemSelectedListener onItemSelectedListener;
    private int globalTintColor = -1;

    public static AlertDialogFragment create(String type, String headerText, String messageText, String negativeButtonText, String neutralButtonText, String positiveButtonText) {
        return create(type, headerText, null, messageText, negativeButtonText, neutralButtonText, positiveButtonText);
    }

    public static AlertDialogFragment create(String type, String headerText, ArrayList<AlertDialogItem> alertDialogItems) {
        return create(type, headerText, alertDialogItems, null, null, null, null);
    }

    public static AlertDialogFragment create(String type, String headerText, ArrayList<AlertDialogItem> alertDialogItems, String messageText, String negativeButtonText, String neutralButtonText, String positiveButtonText) {
        Bundle arguments = new Bundle();
        arguments.putString(TYPE, type);
        arguments.putString(HEADER_TEXT, headerText);
        arguments.putString(MESSAGE_TEXT, messageText);
        arguments.putString(NEGATIVE_BUTTON_TEXT, negativeButtonText);
        arguments.putString(NEUTRAL_BUTTON_TEXT, neutralButtonText);
        arguments.putString(POSITIVE_BUTTON_TEXT, positiveButtonText);
        arguments.putParcelableArrayList(ALERT_DIALOG_ITEMS, alertDialogItems);

        AlertDialogFragment fragment = new AlertDialogFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    public AlertDialogFragment setCustomPrimaryColor(String globalTintColor) {
        this.globalTintColor = Color.parseColor(globalTintColor);
        return this;
    }

    private int getThemeAccentColor() {
        final TypedValue value = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        return value.data;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);

        String headerText = getArguments().getString(HEADER_TEXT);
        String messageText = getArguments().getString(MESSAGE_TEXT);
        String negativeButtonText = getArguments().getString(NEGATIVE_BUTTON_TEXT);
        String neutralButtonText = getArguments().getString(NEUTRAL_BUTTON_TEXT);
        String positiveButtonText = getArguments().getString(POSITIVE_BUTTON_TEXT);
        final List<AlertDialogItem> alertDialogItems = getArguments().getParcelableArrayList(ALERT_DIALOG_ITEMS);

        if (globalTintColor == -1) {
            globalTintColor = getThemeAccentColor();
        }

        if (headerText != null) {
            builder.setTitle(headerText);
        }

        if (messageText != null) {
            builder.setMessage(messageText);
        }

        if (negativeButtonText != null) {
            builder.setNegativeButton(negativeButtonText, createOnClickListener(DismissedEvent.ClickedButtonType.Negative));
        }

        if (neutralButtonText != null) {
            builder.setNeutralButton(neutralButtonText, createOnClickListener(DismissedEvent.ClickedButtonType.Neutral));
        }

        if (positiveButtonText != null) {
            builder.setPositiveButton(positiveButtonText, createOnClickListener(DismissedEvent.ClickedButtonType.Positive, alertDialogItems));
        }

        if (alertDialogItems != null) {
            @SuppressLint("InflateParams") View view = getActivity().getLayoutInflater().inflate(R.layout.alert_dialog_list, null);
            builder.setView(view);
            RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
            recyclerView.setAdapter(new AlertListAdapter(getActivity().getLayoutInflater(), alertDialogItems, this, globalTintColor));
        }

        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        //this is done in onShow because button color has to be set after .show is called for AlertDialog
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                setAlertDialogButtonColor(dialog);
            }
        });

        return dialog;
    }

    private void setAlertDialogButtonColor(AlertDialog dialog) {
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        if (negativeButton != null) {
            negativeButton.setTextColor(globalTintColor);
        }

        Button neutralButton = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        if (neutralButton != null) {
            neutralButton.setTextColor(globalTintColor);
        }

        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(globalTintColor);
        }
    }

    public AlertDialogFragment setItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
        return this;
    }

    private DialogInterface.OnClickListener createOnClickListener(final DismissedEvent.ClickedButtonType clickedButtonType, final List<AlertDialogItem> alertDialogItems) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dismissedByButton = true;
                ServiceLocator.getInstance().eventDispatcher.dispatchEvent(new DismissedEvent(getArguments().getString(TYPE), clickedButtonType, alertDialogItems));
            }
        };
    }

    @NonNull
    private DialogInterface.OnClickListener createOnClickListener(final DismissedEvent.ClickedButtonType clickedButtonType) {
        return createOnClickListener(clickedButtonType, new ArrayList<AlertDialogItem>());
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (!dismissedByButton) {
            ServiceLocator.getInstance().eventDispatcher.dispatchEvent(new DismissedEvent(getArguments().getString(TYPE), DismissedEvent.ClickedButtonType.Anything));
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(AlertDialogItem selected);
    }

    public static class DismissedEvent {

        private final List<AlertDialogItem> alertDialogItems;
        private String type;
        private ClickedButtonType clickedButtonType;

        DismissedEvent(String type, ClickedButtonType clickedButtonType) {
            this(type, clickedButtonType, new ArrayList<AlertDialogItem>());
        }

        DismissedEvent(String type, ClickedButtonType clickedButtonType, List<AlertDialogItem> alertDialogItems) {
            this.type = type;
            this.clickedButtonType = clickedButtonType;
            this.alertDialogItems = alertDialogItems;
        }

        public String getType() {
            return type;
        }

        public ClickedButtonType getClickedButtonType() {
            return clickedButtonType;
        }

        public List<AlertDialogItem> getAlertDialogItems() {
            return alertDialogItems;
        }

        public enum ClickedButtonType {
            Anything,
            Negative,
            Neutral,
            Positive
        }
    }

    private static class AlertListAdapter extends RecyclerView.Adapter<AlertListItemViewHolder> {
        private final LayoutInflater layoutInflater;
        private final List<AlertDialogItem> alertDialogItems;
        private final int globalTintColor;
        private AlertDialogFragment dialog;

        AlertListAdapter(LayoutInflater layoutInflater, List<AlertDialogItem> alertDialogItems, AlertDialogFragment dialog, int globalTintColor) {
            this.layoutInflater = layoutInflater;
            this.alertDialogItems = alertDialogItems;
            this.dialog = dialog;
            this.globalTintColor = globalTintColor;
        }

        @Override
        public AlertListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AlertListItemViewHolder(layoutInflater.inflate(viewType, parent, false), globalTintColor);
        }

        @Override
        public void onBindViewHolder(AlertListItemViewHolder holder, int position) {
            holder.bindWith(alertDialogItems.get(position), dialog, this);
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
            for (AlertDialogItem item : alertDialogItems) {
                item.checked = false;
            }
        }

        AlertDialogItem getCheckedItem() {
            for (AlertDialogItem item : alertDialogItems) {
                if (item.checked) {
                    return item;
                }
            }
            return null;
        }
    }

    private static class AlertListItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final RadioButton radioButton;
        private final TextView description;

        AlertListItemViewHolder(View itemView, int globalTintColor) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            radioButton = itemView.findViewById(R.id.radio_button);

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
        }

        public void bindWith(final AlertDialogItem dataItem, final AlertDialogFragment dialog, final AlertListAdapter adapter) {
            radioButton.setChecked(dataItem.isChecked());
            title.setText(dataItem.getTitle());
            title.setCompoundDrawablesWithIntrinsicBounds(dataItem.getIconResourceId(), 0, 0, 0);
            if (TextUtilities.isNullOrWhitespace(dataItem.description)) {
                description.setVisibility(View.GONE);
            } else {
                description.setText(dataItem.description);
                description.setVisibility(View.VISIBLE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dataItemDiffersFromCheckedItem()) {
                        adapter.clearAllCheckedItems();
                        radioButton.setChecked(!radioButton.isChecked());
                        dataItem.checked = radioButton.isChecked();
                        ServiceLocator.getInstance().eventDispatcher.dispatchEvent(new DismissedEvent(dialog.getArguments().getString(TYPE), DismissedEvent.ClickedButtonType.Anything, adapter.alertDialogItems));
                        if (dialog.onItemSelectedListener != null) {
                            dialog.onItemSelectedListener.onItemSelected(dataItem);
                        }
                    }
                    dialog.dismiss();
                }

                private boolean dataItemDiffersFromCheckedItem() {
                    AlertDialogItem checkedItem = adapter.getCheckedItem();
                    return checkedItem == null || dataItem.identifier != checkedItem.identifier;
                }
            });
        }
    }

    public static class AlertDialogItem implements Parcelable {
        public static final Creator<AlertDialogItem> CREATOR = new Creator<AlertDialogItem>() {
            @Override
            public AlertDialogItem createFromParcel(Parcel in) {
                return new AlertDialogItem(in);
            }

            @Override
            public AlertDialogItem[] newArray(int size) {
                return new AlertDialogItem[size];
            }
        };
        @NonNull
        private final String title;
        private final String description;
        private final int iconResourceId;
        private int identifier;
        private boolean checked;

        public AlertDialogItem(@NonNull String title, String description, @DrawableRes int iconResourceId, int identifier, boolean checked) {
            this.title = title;
            this.description = description;
            this.iconResourceId = iconResourceId;
            this.identifier = identifier;
            this.checked = checked;
        }

        public AlertDialogItem(@NonNull String title, @DrawableRes int iconResourceId, int identifier, boolean checked) {
            this(title, null, iconResourceId, identifier, checked);
        }

        private AlertDialogItem(Parcel in) {
            title = in.readString();
            description = in.readString();
            iconResourceId = in.readInt();
            identifier = in.readInt();
            checked = in.readInt() != 0;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(description);
            dest.writeInt(iconResourceId);
            dest.writeInt(identifier);
            dest.writeInt(checked ? 1 : 0);
        }

        @NonNull
        public String getTitle() {
            return title;
        }

        int getIconResourceId() {
            return iconResourceId;
        }

        public int getIdentifier() {
            return identifier;
        }

        public boolean isChecked() {
            return checked;
        }
    }
}
