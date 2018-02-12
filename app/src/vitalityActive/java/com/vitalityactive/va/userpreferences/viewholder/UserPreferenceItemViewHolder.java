package com.vitalityactive.va.userpreferences.viewholder;

import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.userpreferences.EmailPreferencePresenter;
import com.vitalityactive.va.userpreferences.RememberMePreferencePresenter;
import com.vitalityactive.va.userpreferences.entities.DefaultUserPreferencePresenter;

public class UserPreferenceItemViewHolder extends BaseUserPreferenceItemViewHolder{

    protected UserPreferenceItemViewHolder(View itemView, int globalTintColor) {
        super(itemView, globalTintColor);
    }

    public void emailCommunication(){
        if (presenter.getDescription() != 0) {
            if (presenter instanceof EmailPreferencePresenter) {
                String desc = description.getContext().getResources().getString(presenter.getDescription());

                if (!TextUtils.isEmpty(presenter.getEmail())) {
                    if (desc.contains("%1$s")) {
                        desc = String.format(desc, presenter.getEmail());
                    } else {
                        desc = description.getContext().getResources().getString(R.string.user_prefs_email_toggle_message_66);
                        desc += "\n";
                        desc += presenter.getEmail();
                    }
                }

                description.setText(desc);

            } else {
                description.setText(presenter.getDescription());
            }
        }
    }

    public void bindWithMarketSpecific(){

        emailCommunication();

        if (presenter.getDescription() != 0) {
            if (presenter instanceof RememberMePreferencePresenter) {
                if(((RememberMePreferencePresenter) presenter).isFingerprintOn()){
                    disableSwitch(true);
                }
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
