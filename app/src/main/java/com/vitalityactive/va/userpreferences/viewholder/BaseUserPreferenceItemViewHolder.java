package com.vitalityactive.va.userpreferences.viewholder;

import android.content.res.ColorStateList;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.userpreferences.UserPreferencePresenter;
import com.vitalityactive.va.userpreferences.entities.DefaultUserPreferencePresenter;
import com.vitalityactive.va.utilities.ViewUtilities;

public abstract class BaseUserPreferenceItemViewHolder extends GenericRecyclerViewAdapter.ViewHolder<DefaultUserPreferencePresenter>
        implements UserPreferencePresenter.UserInterface {

    protected final
    @ColorInt
    int globalTintColor;
    protected TextView title;
    protected TextView description;
    protected Switch preferenceEnabledSwitch;
    protected ImageView icon;
    protected Button manageInSettingsButton;
    protected DefaultUserPreferencePresenter presenter;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

    protected abstract void bindWithMarketSpecific();

    protected BaseUserPreferenceItemViewHolder(View itemView, @ColorInt int globalTintColor) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.preference_item_title);
        description = (TextView) itemView.findViewById(R.id.preference_item_description);
        preferenceEnabledSwitch = (Switch) itemView.findViewById(R.id.preference_item_switch);
        icon = (ImageView) itemView.findViewById(R.id.preference_item_icon);
        manageInSettingsButton = (Button) itemView.findViewById(R.id.manage_in_settings_button);
        this.globalTintColor = globalTintColor;
    }

    @Override
    public void bindWith(final DefaultUserPreferencePresenter presenter) {
        this.presenter = presenter;
        if (presenter.getTitle() != 0) {
            title.setText(presenter.getTitle());
        } else {
            title.setVisibility(View.GONE);
        }

        bindWithMarketSpecific();

        if (presenter.getIconID() != 0) {
            icon.setImageDrawable(ViewUtilities.tintDrawable(ResourcesCompat.getDrawable(itemView.getResources(),
                    presenter.getIconID(),
                    itemView.getContext().getTheme()),
                    globalTintColor));
        } else {
            description.setPadding(45, 0, 0, 0);
        }

        if (presenter.hasSettingsButton()) {
            manageInSettingsButton.setTextColor(globalTintColor);
            manageInSettingsButton.setVisibility(View.VISIBLE);
            ViewUtilities.moveButtonLeftByAmountOfPadding(manageInSettingsButton);
            ViewUtilities.removeBottomMargin(description);

        }

        setSwitchIfPresent();
        setMarginIfNotFirstElement();
        presenter.setUserInterface(this);
    }

    private void setSwitchIfPresent() {
        if (presenter.hasToggleSwitch()) {
            preferenceEnabledSwitch.setVisibility(View.VISIBLE);

            setSwitchColorsToGlobal();

            boolean optedIn = presenter.isOptedIn();
            preferenceEnabledSwitch.setChecked(optedIn);
            onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    presenter.onToggle(isChecked);
                }
            };
            preferenceEnabledSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
        } else {
            preferenceEnabledSwitch.setVisibility(View.INVISIBLE);
        }
    }

    private void setSwitchColorsToGlobal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            RippleDrawable background = (RippleDrawable) preferenceEnabledSwitch.getBackground();

            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{-android.R.attr.state_pressed},
                            new int[]{android.R.attr.state_pressed},
                            new int[]{android.R.attr.state_checked}
                    },
                    new int[]{ContextCompat.getColor(itemView.getContext(), R.color.switch_button_grey),
                            globalTintColor,
                            globalTintColor,
                            globalTintColor}
            );

            background.setColor(colorStateList.withAlpha(50));
            preferenceEnabledSwitch.setBackground(background);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                preferenceEnabledSwitch.setThumbTintList(colorStateList);
            }

            ColorStateList colorStateList2 = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked}
                    },
                    new int[]{ContextCompat.getColor(itemView.getContext(), R.color.switch_track_grey),
                            globalTintColor}
            );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                preferenceEnabledSwitch.setTrackTintList(colorStateList2);
            }
        }
    }

    private void setMarginIfNotFirstElement() {
        if (getAdapterPosition() > 0) {
            final RelativeLayout titleWithSwitchLayout =
                    (RelativeLayout) itemView.findViewById(R.id.title_with_switch_layout);

            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) titleWithSwitchLayout
                    .getLayoutParams();

            int px = ViewUtilities.pxFromDp(16);
            mlp.setMargins(0, px, 0, 0);

            titleWithSwitchLayout.setLayoutParams(mlp);
        }
    }

    @Override
    public void showErrorMessage() {
        String snackBarMessage;
        if (presenter.getTitle() == R.string.uke_communication_pref_title_status_375) {
            snackBarMessage = preferenceEnabledSwitch.getResources().getString(R.string.uke_communication_pref_update_error_9999);
        } else {
            snackBarMessage = preferenceEnabledSwitch.getResources().getString(R.string.Settings_email_preference_error_9999);
        }

        Snackbar.make(itemView, snackBarMessage, Snackbar.LENGTH_LONG)
                .setActionTextColor(globalTintColor)
                .setAction(R.string.AR_connection_error_button_retry_789, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preferenceEnabledSwitch.toggle();
                    }
                })
                .show();
    }

    @Override
    public void disableSwitch() {
        disableSwitch(false);
    }


    protected void disableSwitch(boolean toggleSwitchOn) {
        if (toggleSwitchOn) {
            preferenceEnabledSwitch.setChecked(true);
        }
        preferenceEnabledSwitch.setEnabled(false);
    }


    @Override
    public void enableSwitch() {
        preferenceEnabledSwitch.setEnabled(true);
    }

    @Override
    public void synchroniseOptedInState() {
        preferenceEnabledSwitch.setOnCheckedChangeListener(null);
        preferenceEnabledSwitch.setChecked(presenter.isOptedIn());
        preferenceEnabledSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
    }
}
