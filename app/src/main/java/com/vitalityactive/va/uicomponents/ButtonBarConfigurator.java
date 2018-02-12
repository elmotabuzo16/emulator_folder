package com.vitalityactive.va.uicomponents;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.vitalityactive.va.R;

public class ButtonBarConfigurator {
    private final Button forwardButton;
    private final Button backButton;
    private final Drawable backIcon;
    private final Drawable forwardIcon;
    private final ProgressBar progressBar;

    private ButtonBarConfigurator(Button forwardButton, Button backButton, ProgressBar progressBar, Context context) {
        this.forwardButton = forwardButton;
        this.backButton = backButton;
        this.progressBar = progressBar;

        forwardIcon = ContextCompat.getDrawable(context, R.drawable.ic_navigate_next_white_24dp);
        backIcon = ContextCompat.getDrawable(context, R.drawable.ic_navigate_before_white_24dp);
    }

    public static ButtonBarConfigurator load(View root) {
        return load(root, R.id.button_bar_forward_button, R.id.button_bar_back_button);
    }

    public static ButtonBarConfigurator load(View root, int forwardButtonId, int backButtonId) {
        Button forwardButton = root.findViewById(forwardButtonId);
        Button backButton = root.findViewById(backButtonId);
        ProgressBar progressbar = root.findViewById(R.id.progress_bar);

        return new ButtonBarConfigurator(forwardButton, backButton, progressbar, root.getContext());
    }

    public ButtonBarConfigurator setForwardButtonTextToNext() {
        return setForwardButtonText(R.string.next_button_title_84);
    }

    public ButtonBarConfigurator setForwardButtonTextToConfirm() {
        return setForwardButtonText(R.string.confirm_title_button_182);
    }

    public ButtonBarConfigurator setForwardButtonTextToAgree() {
        return setForwardButtonText(R.string.agree_button_title_50);
    }

    public ButtonBarConfigurator setForwardButtonText(int resourceId) {
        forwardButton.setText(resourceId);
        return this;
    }

    public ButtonBarConfigurator setForwardButtonEnabled(boolean enabled) {
        forwardButton.setEnabled(enabled);

        if (enabled) {
            forwardIcon.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        } else {
            forwardIcon.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }

        forwardButton.setCompoundDrawablesWithIntrinsicBounds(null, null, forwardIcon, null);

        return this;
    }

    public ButtonBarConfigurator setBackButtonEnabled(boolean enabled) {
        backButton.setEnabled(enabled);

        if (enabled) {
            backIcon.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        } else {
            backIcon.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }

        backButton.setCompoundDrawablesWithIntrinsicBounds(backIcon, null, null, null);

        return this;
    }

    public ButtonBarConfigurator displayProgressBar(boolean visibility) {
        if (visibility) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }

        return this;
    }

    public ButtonBarConfigurator setForwardButtonOnClick(final OnClickListener onClickListener) {
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onButtonBarForwardClicked();
            }
        });
        return this;
    }

    public ButtonBarConfigurator setBackButtonOnClick(final OnBackButtonClickListener onClickListener) {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onButtonBarBackClicked();
            }
        });
        return this;
    }

    public ButtonBarConfigurator setBackButtonText(@StringRes int resourceId) {
        backButton.setText(resourceId);
        return this;
    }

    public ButtonBarConfigurator displayBackButton(boolean visibility) {
        if (visibility) {
            backButton.setVisibility(View.VISIBLE);
        } else {
            // invisible, not gone, so that progressbar can be aligned
            backButton.setVisibility(View.INVISIBLE);
        }

        return this;
    }

    public ButtonBarConfigurator displayBackButton(boolean visible, boolean enabled, @StringRes int resourceId) {
        return displayBackButton(visible)
                .setBackButtonEnabled(enabled)
                .setBackButtonText(resourceId);
    }

    public ButtonBarConfigurator displayProgressBar(int progress, int total) {
        progressBar.setProgress(progress);
        progressBar.setMax(total);
        return displayProgressBar(true);
    }

    public interface OnClickListener {
        void onButtonBarForwardClicked();
    }

    public interface OnBackButtonClickListener {
        void onButtonBarBackClicked();
    }
}
