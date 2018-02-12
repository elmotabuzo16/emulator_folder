package com.vitalityactive.va.userpreferences.viewholder;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.view.View;

import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.userpreferences.EmailPreferencePresenter;
import com.vitalityactive.va.userpreferences.RememberMePreferencePresenter;
import com.vitalityactive.va.userpreferences.entities.DefaultUserPreferencePresenter;

public class UserPreferenceItemViewHolder extends BaseUserPreferenceItemViewHolder{

    protected UserPreferenceItemViewHolder(View itemView, int globalTintColor) {
        super(itemView, globalTintColor);
    }

    public void bindWithMarketSpecific() {
        emailCommunication();

        if (presenter.getDescription() == R.string.user_prefs_manage_in_settings_button_title_91) {
            description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent settingsIntent = new Intent();
                    settingsIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    settingsIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    settingsIntent.setData(Uri.parse("package:" + itemView.getContext().getPackageName()));
                    settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    settingsIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                    itemView.getContext().startActivity(settingsIntent);

                }
            });
        }

        if (presenter.getDescription() != 0) {
            if (presenter instanceof RememberMePreferencePresenter) {
                    if(((RememberMePreferencePresenter) presenter).isFingerprintOn()){
                        disableSwitch(true);
                    }
            }
        }
    }

    public void emailCommunication(){
        if (presenter.getDescription() != 0) {
            if (presenter instanceof EmailPreferencePresenter) {
                String desc = description.getContext().getResources().getString(presenter.getDescription());
                if (!TextUtils.isEmpty(presenter.getEmail())) {
                    desc = description.getContext().getResources().getString(R.string.user_prefs_email_toggle_message_66);
                    desc += "\n";
                    desc += presenter.getEmail();
                }
                description.setText(desc);

            } else {
                description.setText(presenter.getDescription());
            }
        }
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<DefaultUserPreferencePresenter,
            UserPreferenceItemViewHolder> {

        private final
        @ColorInt
        int globalTintColor;

        public Factory(@ColorInt int globalTintColor) {

            this.globalTintColor = globalTintColor;
        }

        @Override
        public UserPreferenceItemViewHolder createViewHolder(View itemView) {
            return new  UserPreferenceItemViewHolder(itemView, globalTintColor);
        }
    }
}
