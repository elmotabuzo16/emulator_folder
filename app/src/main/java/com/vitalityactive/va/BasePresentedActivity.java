package com.vitalityactive.va;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.uicomponents.LoadingIndicatorFragment;
import com.vitalityactive.va.utilities.ViewUtilities;

public abstract class BasePresentedActivity<UI, P extends Presenter<UI>> extends BaseActivity {
    protected static final String LOADING_INDICATOR = "LOADING_INDICATOR";
    protected P presenter;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectDependencies(getDependencyInjector());
        presenter = getPresenter();
        presenter.setUserInterface(getUserInterface());

        create(savedInstanceState);

        getPresenter().onUserInterfaceCreated(savedInstanceState == null);
    }

    protected void create(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected final void onResume() {
        super.onResume();
        resume();
    }

    protected void resume() {
    }

    @Override
    protected final void onResumeFragments() {
        super.onResumeFragments();
        resumeFragments();
        getPresenter().onUserInterfaceAppeared();
    }

    protected void resumeFragments() {
    }

    @Override
    protected final void onPause() {
        super.onPause();
        pause();
        getPresenter().onUserInterfaceDisappeared(isFinishing());
    }

    protected void pause() {
    }

    @Override
    protected final void onDestroy() {
        super.onDestroy();
        destroy();
        getPresenter().onUserInterfaceDestroyed();
        getPresenter().setUserInterface(null);
    }

    protected void destroy() {
    }

    protected abstract void injectDependencies(DependencyInjector dependencyInjector);

    protected abstract UI getUserInterface();

    protected abstract P getPresenter();

    public void showLoadingIndicator() {
        showLoadingIndicator(getThemeAccentColor());
    }

    public void showLoadingIndicator(@ColorInt int colour) {
        showLoadingIndicator(colour, null);
    }

    public void showLoadingIndicator(String text) {
        showLoadingIndicator(getThemeAccentColor(), text);
    }

    public void showLoadingIndicator(@ColorInt int colour, String text) {
        if (getSupportFragmentManager().findFragmentByTag(LOADING_INDICATOR) != null) {
            return;
        }
        LoadingIndicatorFragment.newInstance(colour, text).show(getSupportFragmentManager(), LOADING_INDICATOR);
    }

    public void hideLoadingIndicator() {
        LoadingIndicatorFragment loadingIndicatorFragment = (LoadingIndicatorFragment) getSupportFragmentManager().findFragmentByTag(LOADING_INDICATOR);
        if (loadingIndicatorFragment != null) {
            loadingIndicatorFragment.dismiss();
        }
    }

    protected boolean buttonTypeTapped(AlertDialogFragment.DismissedEvent event, AlertDialogFragment.DismissedEvent.ClickedButtonType buttonType) {
        return event.getClickedButtonType() == buttonType;
    }

    protected boolean eventIsOfType(AlertDialogFragment.DismissedEvent event, String eventType) {
        return event.getType().equals(eventType);
    }

    protected boolean positiveButtonTapped(AlertDialogFragment.DismissedEvent event) {
        return buttonTypeTapped(event, AlertDialogFragment.DismissedEvent.ClickedButtonType.Positive);
    }

    protected boolean wasTryAgainButtonTapped(AlertDialogFragment.DismissedEvent event, String eventType) {
        return eventIsOfType(event, eventType) && positiveButtonTapped(event);
    }

    public int getThemeAccentColor() {
        final TypedValue value = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        return value.data;
    }


    protected void setUIColor(String colorValue){
        //TODO: Make the method abstract once all devs are comfortable to use this method
        //TODO: Interim: This will be the placeholder to set the colors for each widget since they seem to be getting it on globalTintColor
        //TODO: Long term: Decide on which theme to use depending of the user's typeCode for market.
        // Make the layout widgets use android:tint="?attr/colorPrimary"
        // Note: Styles prepare first, layout with their attr next, and java code last.
        // So we need to use the styles for its potential
    }

    /**
     * call this from within dispatchTouchEvent
     */
    protected void clearEditTextFocusWhenTappingOutsideIt(MotionEvent event) {
        // based on https://stackoverflow.com/a/28939113/1016377
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return;
        }

        View view = getCurrentFocus();
        if (!(view instanceof EditText)) {
            return;
        }

        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        if (!rect.contains((int) event.getRawX(), (int) event.getRawY())) {
            view.clearFocus();
            ViewUtilities.hideKeyboardWithoutRequestingFocus(BasePresentedActivity.this, view);
        }
    }
}
