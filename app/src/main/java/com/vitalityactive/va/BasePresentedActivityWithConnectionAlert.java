package com.vitalityactive.va;

import android.support.annotation.CallSuper;
import android.support.annotation.StringRes;

import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;

import javax.inject.Inject;

public abstract class BasePresentedActivityWithConnectionAlert<UI, P extends Presenter<UI>>
        extends BasePresentedActivity<UI, P>
        implements EventListener<AlertDialogFragment.DismissedEvent>
{
    private static final String CONNECTION_ERROR_MESSAGE = "CONNECTION_SUBMISSION_REQUEST_ERROR_MESSAGE";
    private static final String GENERIC_SUBMISSION_REQUEST_ERROR_MESSAGE = "GENERIC_SUBMISSION_REQUEST_ERROR_MESSAGE";

    @Inject
    protected EventDispatcher eventDispatcher;

    public void showConnectionError() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                CONNECTION_ERROR_MESSAGE,
                getString(R.string.connectivity_error_alert_title_44),
                getString(R.string.connectivity_error_alert_message_45),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
        alert.show(getSupportFragmentManager(), CONNECTION_ERROR_MESSAGE);
    }

    public void showGenericError() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                GENERIC_SUBMISSION_REQUEST_ERROR_MESSAGE,
                getString(getGenericErrorTitle()),
                getString(getGenericErrorMessage()),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
        alert.show(getSupportFragmentManager(), GENERIC_SUBMISSION_REQUEST_ERROR_MESSAGE);
    }

    @StringRes
    protected int getGenericErrorTitle() {
        return R.string.vhr_error_title;
    }

    @StringRes
    protected int getGenericErrorMessage() {
        return R.string.vhr_unknown_error_message;
    }

    @Override
    @CallSuper
    protected void resume() {
        eventDispatcher.addEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    @Override
    @CallSuper
    protected void pause() {
        eventDispatcher.removeEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        if (buttonTypeTapped(event, AlertDialogFragment.DismissedEvent.ClickedButtonType.Positive)) {
            onConnectionAlertPositiveEvent();
        } else {
            onConnectionAlertNegativeEvent();
        }
    }

    protected abstract void onConnectionAlertPositiveEvent();

    protected abstract void onConnectionAlertNegativeEvent();
}
