package com.vitalityactive.va.home.cards;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vitalityactive.va.BaseFragment;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.rewards.content.RewardPartnerContent;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dto.HomeCardDTO;
import com.vitalityactive.va.dto.HomeCardItemDTO;
import com.vitalityactive.va.dto.HomeCardItemMetadataDTO;
import com.vitalityactive.va.dto.HomeCardMetadataDTO;
import com.vitalityactive.va.home.HomeCardItemType;
import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.uicomponents.circularprogressbardrawable.CircularProgressBarDrawable;
import com.vitalityactive.va.utilities.ViewUtilities;

public abstract class HomeScreenCardFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResourceId(), container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupDependencies(getDependencyInjector());
        loadData();

        if (getView() != null) {
            configureView(getView());
            setupClicking(getView());
        }
    }

    protected abstract void setupDependencies(DependencyInjector dependencyInjector);

    protected abstract void loadData();

    protected abstract void onClicked();

    protected abstract void configureView(View view);

    protected void setupTitle(HomeCardDTO card, View view, @StringRes int earnTotalPointsTitleString, @StringRes int completedString) {
        if (card.hasNotStarted() || card.isInProgress()) {
            Log.e("cjc",getResources().getString(earnTotalPointsTitleString).toString());
            setFormattedString(view, R.id.title, earnTotalPointsTitleString, card.potentialPoints);
        } else if (card.isDone()) {
            Log.e("cjc",getResources().getString(completedString).toString());
            setText(view, R.id.title, completedString);
        }
    }

    protected void setupSubTitle(HomeCardDTO card, View rootView,
                                 @StringRes int editAndUpdateString, @StringRes int remainingSectionsMask, @StringRes int getStartedButtonString)
    {

        if (card.isDone()) {
            setText(rootView, R.id.subtitle, editAndUpdateString);
            rootView.findViewById(R.id.subtitle).setVisibility(View.VISIBLE);
        } else if (card.isInProgress()) {
            long sectionsRemaining = card.total - card.amountCompleted;

            setFormattedString(rootView, R.id.subtitle, remainingSectionsMask, sectionsRemaining);
            rootView.findViewById(R.id.subtitle).setVisibility(View.VISIBLE);
        } else {
            setText(rootView, R.id.get_started_button, getStartedButtonString);
        }
    }

    protected abstract int getLayoutResourceId();

    private void setupClicking(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClicked();
            }
        });
    }

    protected Drawable getDrawable(int resourceId) {
        return ResourcesCompat.getDrawable(getResources(), resourceId, getActivity().getTheme());
    }

    protected Drawable getTintedDrawable(int resourceId, int colorId) {
        return getTintedDrawable(getDrawable(resourceId), colorId);
    }

    protected Drawable getTintedDrawable(Drawable drawable, int colorId) {
        int color = ResourcesCompat.getColor(getResources(), colorId, getActivity().getTheme());
        return ViewUtilities.tintDrawable(drawable, color);
    }

    protected void setImage(View rootView, int imageViewId, Drawable drawable) {
        ((ImageView)rootView.findViewById(imageViewId)).setImageDrawable(drawable);
    }

    protected void setFormattedString(View rootView, int textViewId, @StringRes int stringId, Object... formatArgs) {
        String string = getString(stringId, formatArgs);
        ((TextView) rootView.findViewById(textViewId)).setText(string);
    }

    protected void setText(View rootView, int textViewId, int stringId) {
        ((TextView) rootView.findViewById(textViewId)).setText(stringId);
    }

    protected void setupCircularProgressBar(View rootView, HomeCardDTO card,
                                            @ColorRes int completedColor, @ColorRes int incompleteColor) {
        final ProgressBar progressBar = rootView.findViewById(R.id.circular_progress_bar);
        final TextView progressBarTextView = rootView.findViewById(R.id.circular_progress_bar_text);

        Drawable drawable = getProgressBarDrawable(rootView, progressBarTextView, card, completedColor, incompleteColor);

        progressBar.setProgressDrawable(drawable);
        progressBar.setVisibility(View.VISIBLE);
    }

    private Drawable getProgressBarDrawable(View rootView, TextView progressBarTextView, HomeCardDTO card,
                                            @ColorRes int completedColor, @ColorRes int incompleteColor) {
        RelativeLayout logoLayout = rootView.findViewById(R.id.large_logo);
        final ViewGroup.LayoutParams layoutParams = logoLayout.getLayoutParams();

        return new CircularProgressBarDrawable.Builder()
                .setSize(layoutParams.width, layoutParams.height)
                .setFillStrokeWidth(CircularProgressBarDrawable.DEFAULT_STROKE_WIDTH)
                .setSegmentGapWidth(6)
                .setCompletedSegmentCount((int) card.amountCompleted)
                .setTotalSegmentCount(card.total)
                .setCompletedColor(ContextCompat.getColor(rootView.getContext(), completedColor))
                .setIncompleteColor(ContextCompat.getColor(rootView.getContext(), incompleteColor))
                .setExtraText(progressBarTextView)
                .build();
    }

    protected void setupCompletedIconLogo(View rootView, @ColorRes int tintColor) {
        setImage(rootView, R.id.logo, getTintedDrawable(R.drawable.completed_main, tintColor));
        rootView.findViewById(R.id.logo).setVisibility(View.VISIBLE);
    }

    protected void showCompletedIconOrCircularProgressBar(HomeCardDTO card, View rootView, @ColorRes int completedColor, @ColorRes int incompleteColor) {
        showCompletedIconOrCircularProgressBar(card, rootView, completedColor, completedColor, incompleteColor);
    }

    private void showCompletedIconOrCircularProgressBar(HomeCardDTO card, View rootView, @ColorRes int tintColor, @ColorRes int completedColor, @ColorRes int incompleteColor) {
        if (card.isDone()) {
            setupCompletedIconLogo(rootView, tintColor);
        } else {
            setupCircularProgressBar(rootView, card, completedColor, incompleteColor);
        }
    }

    protected String getMetadataValue(HomeCardType.MetadataType metadataType, HomeCardDTO card) {
        for (HomeCardMetadataDTO cardMetadata : card.cardMetadatas) {
            if (cardMetadata.type == metadataType) {
                return cardMetadata.value;
            }
        }
        return "";
    }

    @DrawableRes
    protected int getRewardLogoResourceId(String rewardId) {
        try {
            RewardPartnerContent rewardPartnerContent =
                    RewardPartnerContent.fromRewardId(Integer.parseInt(rewardId));
            return rewardPartnerContent.getLogoResourceId();
        } catch (NumberFormatException ignored) {
        }
        return R.drawable.ic_rewards_trophy_lrg;
    }

    public interface Factory {
        HomeScreenCardFragment buildFragment();
    }
}
