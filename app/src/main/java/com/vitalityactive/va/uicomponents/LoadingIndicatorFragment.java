package com.vitalityactive.va.uicomponents;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vitalityactive.va.BaseDialogFragment;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.utilities.ViewUtilities;

import javax.inject.Inject;

public class LoadingIndicatorFragment extends BaseDialogFragment {

    private static final String PROGRESS_BAR_COLOUR = "PROGRESS_BAR_COLOUR";
    private static final String PROGRESS_BAR_TEXT = "PROGRESS_BAR_TEXT";

    @Inject
    AppConfigRepository appConfigRepository;

    public static LoadingIndicatorFragment newInstance(@ColorInt int progressBarColour){
        return newInstance(progressBarColour, null);
    }

    public static LoadingIndicatorFragment newInstance(@ColorInt int progressBarColour, String text) {
        LoadingIndicatorFragment loadingIndicatorFragment = new LoadingIndicatorFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PROGRESS_BAR_COLOUR, progressBarColour);
        bundle.putString(PROGRESS_BAR_TEXT, text);
        loadingIndicatorFragment.setArguments(bundle);
        return loadingIndicatorFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        getDependencyInjector().inject(this);
        setCancelable(false);
        Dialog dialog = new Dialog(getActivity(), R.style.ProgressDialogTheme);
        @SuppressLint("InflateParams") View view = getActivity().getLayoutInflater().inflate(R.layout.loading_indicator, null, false);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        tintProgressBar(view);
        setText(view);
        return dialog;
    }

    private void tintProgressBar(View view) {
        // fixes pre-Lollipop progressBar indeterminateDrawable tinting
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable wrapDrawable = ViewUtilities.tintDrawable(progressBar.getIndeterminateDrawable(), getColor());
            progressBar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            progressBar.getIndeterminateDrawable().setColorFilter(getColor(), PorterDuff.Mode.SRC_IN);
        }
    }

    private int getColor() {
        if (getArguments() == null) {
            return getGobalTintColour();
        }
        int colour = getArguments().getInt(PROGRESS_BAR_COLOUR, -1);
        return colour == -1 ? getGobalTintColour() : colour;
    }

    private int getGobalTintColour() {
        return Color.parseColor(appConfigRepository.getGlobalTintColorHex());
    }

    private void setText(View view){
        if(getArguments() != null) {
            String text = getArguments().getString(PROGRESS_BAR_TEXT);
            TextView textView = (TextView) view.findViewById(R.id.tv_progress);
            if (TextUtils.isEmpty(text)) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setText(text);
                textView.setVisibility(View.VISIBLE);
            }
        }

    }
}
