package com.vitalityactive.va.uicomponents;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.vitalityactive.va.R;

public class OnboardingCirclesAnimator {
    private View circleBig;
    private View circleMedium;
    private View circleSmall;
    private ImageView iconImageView;
    private Animation circleBigAnimation;
    private Animation circleMediumAnimation;
    private Animation circleSmallAnimation;
    private Animation iconAnimation;
    private final Context context;
    private View onboardingCircles;
    private final int size;

    public OnboardingCirclesAnimator(Context context, ViewGroup onboardingCircles, int iconResourceId, int size) {
        this.context = context;
        this.onboardingCircles = onboardingCircles;
        this.size = size;
        findViews();
        resizeCircles();
        setUpAnimations();
        iconImageView.setImageResource(iconResourceId);
    }

    public void animateCirclesAndIcon() {
        circleBig.startAnimation(circleBigAnimation);
        circleMedium.startAnimation(circleMediumAnimation);
        circleSmall.startAnimation(circleSmallAnimation);
        iconImageView.startAnimation(iconAnimation);
    }

    private void findViews() {
        circleBig = onboardingCircles.findViewById(R.id.onboarding_circle_big);
        circleMedium = onboardingCircles.findViewById(R.id.onboarding_circle_medium);
        circleSmall = onboardingCircles.findViewById(R.id.onboarding_circle_small);
        iconImageView = (ImageView) onboardingCircles.findViewById(R.id.onboarding_icon);
    }

    private void resizeCircles() {
        scaleView(onboardingCircles, 1.0f);
        scaleView(circleBig, 1.0f);
        scaleView(circleMedium, 264f / 360f);
        scaleView(circleSmall, 168f / 360f);
    }

    private void setUpAnimations() {
        circleBigAnimation = AnimationUtils.loadAnimation(context, R.anim.pop_in);
        circleMediumAnimation = AnimationUtils.loadAnimation(context, R.anim.pop_in);
        circleMediumAnimation.scaleCurrentDuration(0.85f);
        circleSmallAnimation = AnimationUtils.loadAnimation(context, R.anim.pop_in);
        circleSmallAnimation.scaleCurrentDuration(1.05f);
        iconAnimation = AnimationUtils.loadAnimation(context, R.anim.pop_in);
        iconAnimation.scaleCurrentDuration(0.9f);
    }

    private void scaleView(View view, float scale) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = layoutParams.height = (int) (size * scale);
        view.setLayoutParams(layoutParams);
    }

    public void setIcon(int resourceId) {
        iconImageView.setImageResource(resourceId);
    }
}
