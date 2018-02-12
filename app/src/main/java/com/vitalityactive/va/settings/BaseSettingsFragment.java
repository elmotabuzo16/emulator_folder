package com.vitalityactive.va.settings;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pushwoosh.Pushwoosh;
import com.vitalityactive.va.BaseFragment;
import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.R;
import com.vitalityactive.va.pushnotification.InAppPreferences;

import javax.inject.Inject;

public abstract class BaseSettingsFragment extends BaseFragment {

    private String globalTintColor;
    private ImageView communication_preferences_image, privacy_preferences_image, security_preferences_image, terms_conditions_image, logout_image;
    protected View parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarDrawerIconColourToWhite();
        globalTintColor = getArguments().getString(GLOBAL_TINT_COLOR);

        parentView = getView();
        if (parentView == null) {
            return;
        }

        communication_preferences_image = parentView.findViewById(R.id.communication_preferences_image);
        privacy_preferences_image = parentView.findViewById(R.id.privacy_preferences_image);
        security_preferences_image = parentView.findViewById(R.id.security_preferences_image);
        terms_conditions_image = parentView.findViewById(R.id.terms_conditions_image);
        logout_image = parentView.findViewById(R.id.logout_image);

        setUIColor(globalTintColor);

        parentView.findViewById(R.id.communication_preferences).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationCoordinator.navigateToCommunicationPreferences(getActivity());
            }
        });

        marketUiUpdate();

        parentView.findViewById(R.id.privacy_preferences).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationCoordinator.navigateToPrivacyPreferences(getActivity());
            }
        });

        parentView.findViewById(R.id.security_preferences).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationCoordinator.navigateToSecurityPreferences(getActivity());
            }
        });

        parentView.findViewById(R.id.settings_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogout();
            }
        });

        parentView.findViewById(R.id.terms_and_conditions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationCoordinator.navigateFromSettingsToTermsAndConditions(getActivity());
            }
        });
    }


    private void setUIColor(String colorValue) {
        communication_preferences_image.setColorFilter(Color.parseColor(colorValue));
        privacy_preferences_image.setColorFilter(Color.parseColor(colorValue));
        security_preferences_image.setColorFilter(Color.parseColor(colorValue));
        terms_conditions_image.setColorFilter(Color.parseColor(colorValue));
        logout_image.setColorFilter(Color.parseColor(colorValue));
    }

    public void onLogout() {
        AlertDialog logoutDialog = new AlertDialog.Builder(getContext())
                .setMessage(R.string.Settings_alert_logout_message_910)
                .setPositiveButton(R.string.Settings_alert_logout_title_909, (dialog, which) -> {
                    if (BuildConfig.SHOW_PUSH_NOTIFICATIONS) {
                        Pushwoosh.getInstance().unregisterForPushNotifications();
                    }
                    navigationCoordinator.logOut();
                })
                .setNegativeButton(android.R.string.cancel, null).create();
        logoutDialog.show();
        alignDialogToTint(logoutDialog, globalTintColor);
    }

    protected abstract void marketUiUpdate();

}
