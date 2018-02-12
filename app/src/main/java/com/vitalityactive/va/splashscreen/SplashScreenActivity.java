package com.vitalityactive.va.splashscreen;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.vitalityactive.va.BasePresentedActivityWithConnectionAlert;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.utilities.ImageLoader;

import javax.inject.Inject;

public class SplashScreenActivity
        extends BasePresentedActivityWithConnectionAlert<SplashScreenPresenter.UserInterface, SplashScreenPresenter>
        implements SplashScreenPresenter.UserInterface {
    public static final String SKIP_APP_CONFIG = "SKIP_APP_CONFIG";
    private static final String CONNECTION_ERROR_MESSAGE = "CONNECTION_CONFIG_REQUEST_ERROR_MESSAGE";
    private static final String GENERIC_ERROR_MESSAGE = "GENERIC_CONFIG_REQUEST_ERROR_MESSAGE";
    private final Handler handler = new Handler();
    @Inject
    SplashScreenPresenter splashScreenPresenter;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            splashScreenPresenter.onDismissTimerCompleted();
        }
    };
    @Inject
    EventDispatcher eventDispatcher;
    private ImageView logo;
    private String globalTintColor;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo = findViewById(R.id.splash_screen_logo);

        setupLogoSize();

        splashScreenPresenter.skipAppConfig(getIntent().getBooleanExtra(SKIP_APP_CONFIG, false));
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected SplashScreenPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected SplashScreenPresenter getPresenter() {
        return splashScreenPresenter;
    }

    @Override
    public void showLogo(String path) {
        ImageLoader.loadImageFromUri(Uri.parse("file:" + path), logo);
    }

    @Override
    public void dismiss() {
        navigationCoordinator.navigateFromSplashScreen(this);
    }

    @Override
    public void pauseDismissTimer() {
        handler.removeCallbacks(runnable);
    }

    @Override
    public void setSplashGradient(String[] gradientColorStrings) {
        GradientDrawable splashGradient = new GradientDrawable(GradientDrawable.Orientation.BL_TR, getGradientColors(gradientColorStrings));
        final FrameLayout layout = findViewById(R.id.activity_splash_screen);
        layout.setBackground(splashGradient);
    }

    private int[] getGradientColors(String[] gradientColorStrings) {
        int gradientColor1 = Color.parseColor('#' + gradientColorStrings[0]);
        int gradientColor2 = Color.parseColor('#' + gradientColorStrings[1]);

        return new int[]{gradientColor1,
                gradientColor2};
    }

    @Override
    public void startDismissTimer() {
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void navigateOnFailedAppConfigUpdate() {
        navigationCoordinator.logOut();
    }

    @Override
    public void setStatusBarColor() {
        setStatusBarColor(Color.parseColor(globalTintColor));
    }

    @Override
    public void setLoadingIndicatorColor() {
        ProgressBar progressBar = findViewById(R.id.content_loading_indicator);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor(globalTintColor), PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public void setCustomPrimaryColor(String globalTintColorHex) {
        this.globalTintColor = globalTintColorHex;
    }

    @Override
    public void hideLoadingIndicator() {
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    @Override
    public void showLoadingIndicator() {
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onConnectionAlertPositiveEvent() {
        splashScreenPresenter.updateAppConfig();
    }

    @Override
    protected void onConnectionAlertNegativeEvent() {
        navigationCoordinator.logOut();
    }

    public void showConnectionError() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                CONNECTION_ERROR_MESSAGE,
                getString(R.string.connectivity_error_alert_title_44),
                getString(R.string.connectivity_error_alert_message_45),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43))
                .setCustomPrimaryColor(globalTintColor);
        alert.show(getSupportFragmentManager(), CONNECTION_ERROR_MESSAGE);
    }

    public void showGenericError() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                GENERIC_ERROR_MESSAGE,
                getString(R.string.error_unable_to_load_title_503),
                getString(R.string.error_unable_to_load_message_504),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43))
                .setCustomPrimaryColor(globalTintColor);
        alert.show(getSupportFragmentManager(), GENERIC_ERROR_MESSAGE);
    }

    private void setupLogoSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        StringBuilder logoNameBuilder = new StringBuilder();
        logoNameBuilder.append("logo");
        if (metrics.densityDpi >= DisplayMetrics.DENSITY_XXXHIGH) {
            logoNameBuilder.append("_xxxhdpi.png");
        } else if (metrics.densityDpi >= DisplayMetrics.DENSITY_XXHIGH) {
            logoNameBuilder.append("_xxhdpi.png");
        } else if (metrics.densityDpi >= DisplayMetrics.DENSITY_XHIGH) {
            logoNameBuilder.append("_xhdpi.png");
        } else if (metrics.densityDpi >= DisplayMetrics.DENSITY_HIGH) {
            logoNameBuilder.append("_hdpi.png");
        } else {
            logoNameBuilder.append("_mdpi.png");
        }
        splashScreenPresenter.setSizedLogoFilename(logoNameBuilder.toString());
    }
}
