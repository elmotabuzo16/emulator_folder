package com.vitalityactive.va.profile;

import android.view.View;

import com.vitalityactive.va.R;

public class ChangeEmailFragment extends BaseChangeEmailFragment {
    @Override
    protected void marketUIUpdate() {
        newEmailAddView.setHint(R.string.profile_change_email_new_email_heading);
        newEmailAddLayout.setHint(getString(R.string.profile_change_email_new_email_heading));
        newEmailAddView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    newEmailAddView.setHint("");
                }
            }
        });
    }

    @Override
    protected void processDismissEvent() {
        getPresenter().showPasswordConfirmation();
    }
}
