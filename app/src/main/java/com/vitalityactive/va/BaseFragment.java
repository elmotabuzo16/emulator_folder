package com.vitalityactive.va;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.vitalityactive.va.dependencyinjection.DependencyInjector;

import javax.inject.Inject;

public class BaseFragment extends Fragment {
    public static final String GLOBAL_TINT_COLOR = "GLOBAL_TINT_COLOR";
    @Inject
    protected NavigationCoordinator navigationCoordinator;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDependencyInjector().inject(this);
    }

    protected DependencyInjector getDependencyInjector() {
        return getVitalityActiveApplication().getDependencyInjector();
    }

    private ViewTreeObserver getViewTreeObserver() {
        return getActivity().getWindow().getDecorView().getViewTreeObserver();
    }

    protected void setLoadingIndicatorColor(String globalTintColor) {
        View fragmentView = getView();
        if (fragmentView == null) {
            return;
        }

        View loadingIndicator = fragmentView.findViewById(R.id.loading_indicator);
        if (loadingIndicator == null) {
            return;
        }

        ProgressBar progressBar = (ProgressBar) loadingIndicator.findViewById(R.id.progress_bar);
        if (progressBar == null) {
            return;
        }

        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor(globalTintColor), PorterDuff.Mode.MULTIPLY);
    }

    protected void restoreToolbar(String globalTintColor) {
        setToolbarVisible(globalTintColor);
        setToolbarDrawerIconColourToWhite();
    }

    private void setToolbarVisible(String globalTintColor) {
        getActivity().findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor(globalTintColor));
    }

    @NonNull
    private PorterDuffColorFilter getColorFilter(int color) {
        return new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    private Toolbar getToolbar() {
        return (Toolbar) getActivity().findViewById(R.id.toolbar);
    }

    protected int getColour(int colourResourceId) {
        return ResourcesCompat.getColor(getResources(), colourResourceId, getActivity().getTheme());
    }

    protected void setToolbarDrawerIconColourToWhite() {
        setToolbarDrawerIconColor(getColorFilter(getColour(android.R.color.white)));
    }

    private void setToolbarDrawerIconColor(PorterDuffColorFilter colorFilter) {
        Toolbar toolbar = getToolbar();
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            final View v = toolbar.getChildAt(i);

            if (v instanceof ImageButton) {
                ((ImageButton) v).getDrawable().setColorFilter(colorFilter);
            }
        }
    }

    protected AlertDialog alignDialogToTint(AlertDialog dialog, String tint){
        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.parseColor(tint));
        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.parseColor(tint));
        return dialog;
    }

    protected void setToolbarDrawerIconColor(final String globalTintColor) {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setToolbarDrawerIconColor(getColorFilter(Color.parseColor(globalTintColor)));
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    protected void setToolbarColor(String globalTintColor) {
        getActivity().findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor(globalTintColor));
    }

    protected VitalityActiveApplication getVitalityActiveApplication() {
        return (VitalityActiveApplication) getActivity().getApplication();
    }

    @Override
    public final void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            onAppear();
        }
    }

    @Override
    public final void onPause() {
        super.onPause();
        onDisappear();
    }

    protected void onAppear() {

    }

    protected void onDisappear() {

    }

    @Override
    public final void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (!isResumed()) {
            return;
        }

        if (visible) {
            onAppear();
        } else {
            onDisappear();
        }
    }
}
