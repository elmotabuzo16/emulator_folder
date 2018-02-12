package com.vitalityactive.va.profile;

public class ChangeEmailFragment extends BaseChangeEmailFragment {
    @Override
    protected void marketUIUpdate() {
        // Do Nothing
    }

    @Override
    protected void processDismissEvent() {
        getPresenter().showPasswordConfirmation();
    }
}
