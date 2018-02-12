package com.vitalityactive.va.termsandconditions;

import android.animation.ObjectAnimator;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.Button;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.EmptyStateViewHolder;
import com.vitalityactive.va.R;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.utilities.FileUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.io.IOException;

public abstract class TermsAndConditionsBaseActivity<UserInterface extends TermsAndConditionsPresenter.UserInterface, Presenter extends TermsAndConditionsPresenter<UserInterface>> extends BasePresentedActivity<UserInterface, Presenter> implements EventListener<AlertDialogFragment.DismissedEvent> {
    public static final String TERMS_AND_CONDITIONS_CONTENT_REQUEST_ERROR_ALERT = "TERMS_AND_CONDITIONS_CONTENT_REQUEST_ERROR_ALERT";
    protected static final String TERMS_AND_CONDITIONS_DISAGREE_ALERT = "TERMS_AND_CONDITIONS_DISAGREE_ALERT";
    protected static final String TERMS_AND_CONDITIONS_AGREE_REQUEST_ERROR_ALERT = "TERMS_AND_CONDITIONS_AGREE_REQUEST_ERROR_ALERT";
    protected static final String TERMS_AND_CONDITIONS_DISAGREE_REQUEST_ERROR_ALERT = "TERMS_AND_CONDITIONS_DISAGREE_REQUEST_ERROR_ALERT";
    private static final int SCROLL_DURATION_ON_MORE = 400;
    private static final int SCROLL_CHECK_POSITION_ADJUSTMENT = 10;
    private Button agreeButton;
    private WebView webView;
    private EmptyStateViewHolder emptyStateViewHolder;

    @CallSuper
    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_terms_and_conditions);
        setUpViews();
    }

    @Override
    protected void resume() {
        super.resume();
        getEventDispatcher().addEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    @Override
    protected void pause() {
        super.pause();
        getEventDispatcher().removeEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    protected abstract EventDispatcher getEventDispatcher();

    private void setUpViews() {
        setUpEmptyState();
        setUpAgreeButton();
        setUpWebView();
    }

    private void setUpEmptyState() {
        emptyStateViewHolder = new EmptyStateViewHolder(findViewById(R.id.empty_state));
        emptyStateViewHolder.setup(R.string.error_unable_to_load_title_503,
                R.string.error_unable_to_load_message_504,
                R.string.try_again_button_title_43);
    }

    private void setUpAgreeButton() {
        agreeButton = findViewById(R.id.terms_and_conditions_agree_button);
        disableAgreeButton();
        agreeButton.setText(getString(R.string.terms_and_conditions_more_button_title_95));
        agreeButton.setCompoundDrawablePadding(16);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_right_black_24dp);
        agreeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ViewUtilities.tintDrawable(drawable, ContextCompat.getColorStateList(this, R.color.chevron_tint)), null);
        agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (agreeButton.getText().equals(getAgreeActionTitle())) {
                    handleAgreeButtonTapped(agreeButton);
                } else {
                    scrollWebView();
                }
            }
        });
    }

    @NonNull
    protected String getAgreeActionTitle() {
        return getString(R.string.agree_button_title_50);
    }

    private void setUpWebView() {
        webView = findViewById(R.id.webview);
        webView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (didScrollToBottomOfWebViewContent()) {
                    agreeButton.setText(getAgreeActionTitle());
                }
            }
        });
    }

    private boolean didScrollToBottomOfWebViewContent() {
        int position = webView.getScrollY() + webView.getMeasuredHeight() + SCROLL_CHECK_POSITION_ADJUSTMENT;
        float totalHeight = webView.getContentHeight() * getResources().getDisplayMetrics().density;
        return position >= totalHeight;
    }

    protected abstract Presenter getPresenter();

    private void scrollWebView() {
        ObjectAnimator anim = ObjectAnimator.ofInt(
                webView,
                "scrollY",
                webView.getScrollY(),
                webView.getScrollY() + getResources().getDisplayMetrics().heightPixels);
        anim.setDuration(SCROLL_DURATION_ON_MORE);
        anim.start();
    }

    public final void handleAgreeButtonTapped(View view) {
        presenter.onUserAgreesToTermsAndConditions();
    }

    public void showTermsAndConditions(@NonNull String termsAndConditions) {
        emptyStateViewHolder.hideEmptyStateViewAndShowOtherView(webView);
        enableAgreeButton();
        webView.loadData(getWebPageWithContent(termsAndConditions), "text/html; charset=utf-8", "utf-8");
        agreeButton.setText(getString(R.string.terms_and_conditions_more_button_title_95));

        webView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int height = webView.getContentHeight();
                if (height != 0) {
                    if (didScrollToBottomOfWebViewContent()) {
                        agreeButton.setText(getAgreeActionTitle());
                    }
                    webView.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return false;
            }
        });
    }

    private String getWebPageWithContent(@NonNull String termsAndConditions) {
        try {
            String template = FileUtilities.readFile(getAssets().open("template.html"));
            return template.replace("CONTENT_CONTENT_CONTENT", termsAndConditions);
        } catch (IOException e) {
            return termsAndConditions;
        }
    }

    public void navigateAfterTermsAndConditionsDeclined() {
        navigationCoordinator.navigateAfterTermsAndConditionsDeclined(this);
    }

    public void disableAgreeButton() {
        agreeButton.setEnabled(false);
    }

    public void enableAgreeButton() {
        agreeButton.setEnabled(true);
    }

    public void showGenericAgreeRequestErrorMessage() {
        showGenericRequestErrorMessage(TERMS_AND_CONDITIONS_AGREE_REQUEST_ERROR_ALERT);
    }

    private void showGenericRequestErrorMessage(String tag) {
        getGenericRequestAlertDialogFragment(tag)
                .show(getSupportFragmentManager(), tag);
    }

    public AlertDialogFragment getGenericRequestAlertDialogFragment(String tag) {
        return AlertDialogFragment.create(
                tag,
                getString(R.string.generic_unkown_error_title_266),
                getString(R.string.generic_unkown_error_message_267),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
    }

    public void showConnectionAgreeRequestErrorMessage() {
        showConnectionRequestErrorMessage(TERMS_AND_CONDITIONS_AGREE_REQUEST_ERROR_ALERT);
    }

    private void showConnectionRequestErrorMessage(String tag) {
        getConnectionAlertDialogFragment(tag)
            .show(getSupportFragmentManager(), tag);
    }

    public AlertDialogFragment getConnectionAlertDialogFragment(String tag) {
        return AlertDialogFragment.create(
                tag,
                getString(R.string.connectivity_error_alert_title_44),
                getString(R.string.connectivity_error_alert_message_45),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
    }

    public void showGenericDisagreeRequestErrorMessage() {
        showGenericRequestErrorMessage(TERMS_AND_CONDITIONS_DISAGREE_REQUEST_ERROR_ALERT);
    }

    public void showConnectionDisagreeRequestErrorMessage() {
        showConnectionRequestErrorMessage(TERMS_AND_CONDITIONS_DISAGREE_REQUEST_ERROR_ALERT);
    }

    public void showGenericContentRequestErrorMessage() {
        showContentRequestErrorMessage();
    }

    private void showContentRequestErrorMessage() {
        emptyStateViewHolder.showEmptyStateViewAndHideOtherView(webView);
    }

    public void showConnectionContentRequestErrorMessage() {
        showContentRequestErrorMessage();
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        if (wasOKButtonTappedOnDisagreeAlert(event) || wasTryAgainButtonTappedOnDisagreeErrorAlert(event)) {
            getPresenter().onUserDisagreesToTermsAndConditions();
        } else if (wasTryAgainButtonTappedOnAgreeErrorAlert(event)) {
            getPresenter().onUserAgreesToTermsAndConditions();
        } else if (wasTryAgainButtonTappedOnContentErrorAlert(event)) {
            getPresenter().fetchTermsAndConditions();
        }
    }

    private boolean wasTryAgainButtonTappedOnContentErrorAlert(AlertDialogFragment.DismissedEvent event) {
        return wasTryAgainButtonTapped(event, TERMS_AND_CONDITIONS_CONTENT_REQUEST_ERROR_ALERT);
    }

    private boolean wasTryAgainButtonTappedOnAgreeErrorAlert(AlertDialogFragment.DismissedEvent event) {
        return wasTryAgainButtonTapped(event, TERMS_AND_CONDITIONS_AGREE_REQUEST_ERROR_ALERT);
    }

    private boolean wasTryAgainButtonTappedOnDisagreeErrorAlert(AlertDialogFragment.DismissedEvent event) {
        return wasTryAgainButtonTapped(event, TERMS_AND_CONDITIONS_DISAGREE_REQUEST_ERROR_ALERT);
    }

    private boolean wasOKButtonTappedOnDisagreeAlert(AlertDialogFragment.DismissedEvent event) {
        return wasTryAgainButtonTapped(event, TERMS_AND_CONDITIONS_DISAGREE_ALERT);
    }

    protected EmptyStateViewHolder getEmptyStateViewHolder() {
        return emptyStateViewHolder;
    }

    public void showDisagreeAlert() {
        getDisagreeAlertDialogFragment()
                .show(getSupportFragmentManager(), TERMS_AND_CONDITIONS_DISAGREE_ALERT);
    }

    public AlertDialogFragment getDisagreeAlertDialogFragment() {
        return AlertDialogFragment.create(
                TERMS_AND_CONDITIONS_DISAGREE_ALERT,
                getString(R.string.disagree_button_title_49),
                getString(R.string.terms_and_conditions_disagree_alert_message_51),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.ok_button_title_40));
    }

    public void onEmptyStateButtonClicked(View view) {
        getPresenter().fetchTermsAndConditions();
    }
}
