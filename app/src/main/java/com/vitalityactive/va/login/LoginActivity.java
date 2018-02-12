package com.vitalityactive.va.login;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.onboarding.OnboardingPresenter;
import com.vitalityactive.va.pushnotification.InAppPreferences;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.uicomponents.OnboardingCirclesAnimator;

import javax.inject.Inject;

import me.relex.circleindicator.CircleIndicator;

public class LoginActivity extends BasePresentedActivity<OnboardingPresenter.UserInterface, OnboardingPresenter> implements OnboardingPresenter.UserInterface {
    public static final String EXTRA_LOGIN_AFTER_REGISTRATION = "EXTRA_LOGIN_AFTER_REGISTRATION";
    public static final String EXTRA_LOGIN_AFTER_FORGOT_PASSWORD_USERNAME = "EXTRA_LOGIN_AFTER_FORGOT_PASSWORD_USERNAME";
    private static final String ERROR_SESSION_EXPIRED_TAG = "SESSION_EXPIRED_TAG";
    public static final String SESSION_EXPIRED_KEY = "SESSION_EXPIRED_KEY";
    private ViewPager viewPager;
    private Button skipButton;
    private View background;
    private OnboardingCirclesAnimator circlesAnimator;

    @Inject
    OnboardingPresenter presenter;
    @Inject
    InAppPreferences inAppPreferences;

    private CircleIndicator pagingIndicator;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_login);
        getVitalityActiveApplication().reset();

        findViews();

        setUpViewPager();
        setUpPagingIndicator(viewPager);
        setUpCirclesAndIcon();

        initialiseAppearance();

        boolean isShowSessionExpiredMessage = getIntent().getBooleanExtra(SESSION_EXPIRED_KEY, false);
        if(isShowSessionExpiredMessage){
            showSessionExpiredMessage();
        }
    }

    private void initialiseAppearance() {
        for (int position = presenter.getViewModel().getNumberOfPages() - 1; position >= 0; --position) {
            viewPager.setCurrentItem(position);
        }
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected OnboardingPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected OnboardingPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        String usernameUsedInForgetPassword = intent.getStringExtra(EXTRA_LOGIN_AFTER_FORGOT_PASSWORD_USERNAME);

        if (intent.getBooleanExtra(EXTRA_LOGIN_AFTER_REGISTRATION, false)) {
            viewPager.setCurrentItem(presenter.getViewModel().getNumberOfPages() - 1);
        } else if (usernameUsedInForgetPassword != null && !usernameUsedInForgetPassword.isEmpty()) {
            skipOnboarding();
        }
    }

    private void updateAppearanceForPage(int position) {
        updateSkipButtonVisibilityOnPage(position);
        updateBackgroundAndIcon(position);
        updatePagingIndicator(position);
    }

    private void setUpCirclesAndIcon() {
        final ViewGroup onboardingCircles = (ViewGroup) findViewById(R.id.onboarding_circles);
        onboardingCircles.setVisibility(View.INVISIBLE);
        circlesAnimator = new OnboardingCirclesAnimator(this, onboardingCircles, R.drawable.onboard_01_ic, getResources().getDisplayMetrics().widthPixels);
        animateCirclesAndIcon(onboardingCircles);
    }

    private void animateCirclesAndIcon(final ViewGroup onboardingCircles) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                circlesAnimator.animateCirclesAndIcon();
                onboardingCircles.setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    private void findViews() {
        skipButton = (Button) findViewById(R.id.login_onboarding_skip_button);
        background = findViewById(R.id.activity_login);
    }

    private void setUpViewPager() {
        viewPager = (ViewPager) findViewById(R.id.login_viewpager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new LoginPagerAdapter(getSupportFragmentManager(), presenter.getViewModel()));
        viewPager.addOnPageChangeListener(getOnPageChangeListener());
    }

    @NonNull
    private ViewPager.OnPageChangeListener getOnPageChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateAppearanceForPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    private void updateBackgroundAndIcon(int position) {
        if (presenter.getViewModel().isOnboardingPageAtPosition(position)) {
            setIcon(position);
            circlesAnimator.animateCirclesAndIcon();
            setBackground(position);
        } else {
            setBackgroundColorToWhite();
        }
    }

    private void setBackground(int position) {
        if (presenter.getViewModel().isOnboardingPageAtPosition(position)) {
            swapOnboardingBackground(position);
        } else {
            setBackgroundColorToWhite();
        }
    }

    private void setBackgroundColorToWhite() {
        background.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, android.R.color.white));
    }

    private void updateSkipButtonVisibilityOnPage(int position) {
        skipButton.setVisibility(presenter.getViewModel().isOnboardingPageAtPosition(position) ? View.VISIBLE : View.INVISIBLE);
    }

    private void swapOnboardingBackground(int position) {
        TypedArray backgrounds = getResources().obtainTypedArray(R.array.onboarding_backgrounds);
        background.setBackground(backgrounds.getDrawable(position));
        backgrounds.recycle();
    }

    private void setIcon(int position) {
        TypedArray iconResources = getResources().obtainTypedArray(R.array.onboarding_icons);
        circlesAnimator.setIcon(iconResources.getResourceId(position, 0));
        iconResources.recycle();
    }

    private void setUpPagingIndicator(ViewPager viewPager) {
        pagingIndicator = (CircleIndicator) findViewById(R.id.login_onboarding_indicator);
        pagingIndicator.setViewPager(viewPager);
    }

    private void updatePagingIndicator(int position) {
        if (presenter.getViewModel().isOnboardingPageAtPosition(position)) {
            configurePagingIndicator(R.drawable.onboarding_paging_indicator);
        } else {
            configurePagingIndicator(R.drawable.login_paging_indicator);
        }
    }

    private void configurePagingIndicator(int indicatorBackgroundId) {
        pagingIndicator.configureIndicator(-1, -1, -1, R.animator.paging_indicator_in, R.animator.paging_indicator_out, indicatorBackgroundId, R.drawable.login_paging_indicator_disabled);
    }

    public void handleSkipButtonTapped(View view) {
        presenter.onSkipOnboarding();
    }

    @Override
    public void skipOnboarding() {
        viewPager.setCurrentItem(presenter.getViewModel().getNumberOfPages() - 1, false);
    }

    public void handleForgotPasswordButtonTapped(View view) {
        navigationCoordinator.navigateFromLoginAfterForgotPasswordTapped(this);
    }

    private void showSessionExpiredMessage() {
        inAppPreferences.clearAll();
        AlertDialogFragment alert = AlertDialogFragment.create(
                ERROR_SESSION_EXPIRED_TAG,
                getResources().getString( R.string.login_session_expiration_title_9999 ),
                getResources().getString( R.string.login_session_expiration_message_9999),
                null,
                null,
                getResources().getString( R.string.ok_button_title_40 ));
        alert.show( getSupportFragmentManager(), ERROR_SESSION_EXPIRED_TAG);
    }
}
