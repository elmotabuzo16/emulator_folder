package com.vitalityactive.va.eventsfeed;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import com.vitalityactive.va.R;
import com.vitalityactive.va.ServiceLocator;
import com.vitalityactive.va.eventsfeed.views.adapters.AlertCategoryListAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventsFeedAlertDialogFragment extends DialogFragment {
    private static final String TYPE = "Type";
    private static final String HEADER_TEXT = "HeaderText";
    private static final String NEGATIVE_BUTTON_TEXT = "NegativeButtonText";
    private static final String POSITIVE_BUTTON_TEXT = "PositiveButtonText";
    private static final String ALERT_DIALOG_ITEMS = "AlertDialogItems";
    private boolean dismissedByButton;
    private int globalTintColor = -1;


    public static EventsFeedAlertDialogFragment create(String type, String headerText, ArrayList<AlertDialogItem> alertDialogItems, String negativeButtonText, String positiveButtonText) {
        Bundle arguments = new Bundle();
        arguments.putString(TYPE, type);
        arguments.putString(HEADER_TEXT, headerText);
        arguments.putString(NEGATIVE_BUTTON_TEXT, negativeButtonText);
        arguments.putString(POSITIVE_BUTTON_TEXT, positiveButtonText);
        arguments.putParcelableArrayList(ALERT_DIALOG_ITEMS, alertDialogItems);

        EventsFeedAlertDialogFragment fragment = new EventsFeedAlertDialogFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    public EventsFeedAlertDialogFragment setCustomPrimaryColor(String globalTintColor) {
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
        String negativeButtonText = getArguments().getString(NEGATIVE_BUTTON_TEXT);
        final String positiveButtonText = getArguments().getString(POSITIVE_BUTTON_TEXT);
        final List<AlertDialogItem> alertDialogItems = getArguments().getParcelableArrayList(ALERT_DIALOG_ITEMS);

        if (globalTintColor == -1) {
            globalTintColor = getThemeAccentColor();
        }

        if (headerText != null) {
            builder.setTitle(headerText);
        }

        if (negativeButtonText != null) {
            builder.setNegativeButton(negativeButtonText, createOnClickListener(DismissedEvent.ClickedButtonType.Negative));
        }

        if (positiveButtonText != null) {
            builder.setPositiveButton(positiveButtonText, null);
        }

        if (alertDialogItems != null) {
            @SuppressLint("InflateParams") View view = getActivity().getLayoutInflater().inflate(R.layout.alert_dialog_list, null);
            builder.setView(view);
            RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

            recyclerView.setAdapter(new AlertCategoryListAdapter(getActivity().getLayoutInflater(), alertDialogItems, this, globalTintColor));
        }

        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        //this is done in onShow because button color has to be set after .show is called for AlertDialog
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                setAlertDialogButtonColor(dialog);
                Button button = (dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setText(positiveButtonText);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(alertDialogItems != null && hasSelectedCategory()) {
                            dialog.dismiss();
                            dismissedByButton = true;
                            ServiceLocator.getInstance().eventDispatcher.dispatchEvent(new DismissedEvent(getArguments().getString(TYPE), DismissedEvent.ClickedButtonType.Positive, alertDialogItems));
                        }
                    }

                    private boolean hasSelectedCategory(){

                        for (AlertDialogItem item : alertDialogItems) {
                            if (item.isChecked()) {
                                return true;
                            }
                        }

                        return false;
                    }
                });

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

    private DialogInterface.OnClickListener createOnClickListener(final DismissedEvent.ClickedButtonType clickedButtonType, final List<AlertDialogItem> alertDialogItems) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(clickedButtonType == DismissedEvent.ClickedButtonType.Positive){
                    if(hasSelectedCategory()) {
                        ServiceLocator.getInstance().eventDispatcher.dispatchEvent(new DismissedEvent(getArguments().getString(TYPE), clickedButtonType, alertDialogItems));
                    }
                }

                dialog.dismiss();
                dismissedByButton = true;
            }

            private boolean hasSelectedCategory(){

                if(alertDialogItems != null) {
                    for (AlertDialogItem item : alertDialogItems) {
                        if (item.isChecked()) {
                            return true;
                        }
                    }
                }
                return false;
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

    //model
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

        public int getIconResourceId() {
            return iconResourceId;
        }

        public int getIdentifier() {
            return identifier;
        }

        public boolean isChecked() {
            return checked;
        }

        public String getDescription() {
            return description;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }
}
