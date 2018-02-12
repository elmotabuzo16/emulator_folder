package com.vitalityactive.va.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedFragment;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.EmptyStateViewHolder;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.home.cards.HomeScreenCardSection;
import com.vitalityactive.va.home.cards.HomeScreenCardSectionViewHolder;
import com.vitalityactive.va.home.events.GetCardCollectionResponseEvent;
import com.vitalityactive.va.home.events.GetEventStatusByPartyIdResponseEvent;
import com.vitalityactive.va.home.interactor.HomePresenter;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.CMSImageLoader;
import com.vitalityactive.va.vitalitystatus.ProductPointsContent;
import com.vitalityactive.va.vitalitystatus.VitalityStatusDTO;

import java.util.List;

import javax.inject.Inject;

public class HomeFragment extends BasePresentedFragment<HomePresenter.Ui, HomePresenter>
        implements HomePresenter.Ui {
    private static final String TAG = "HomeFragment";
    @Inject
    DeviceSpecificPreferences deviceSpecificPreferences;
    @Inject
    HomePresenter presenter;
    @Inject
    CMSImageLoader cmsImageLoader;
    @Inject
    ProductPointsContent content;
    @Inject
    EventDispatcher eventDispatcher;

    private String globalTintColor;
    private View parentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void activityCreated(@Nullable Bundle savedInstanceState) {
        globalTintColor = getArguments().getString(GLOBAL_TINT_COLOR);

        setLoadingIndicatorColor(globalTintColor);
        setUpToolbar();

        parentView = getView();
    }

    private void setUpToolbar() {
        setToolbarTransparent();
        setToolbarDrawerIconColor(globalTintColor);
    }

    private void setToolbarTransparent() {
        getActivity().findViewById(R.id.toolbar).setBackgroundColor(getColour(android.R.color.transparent));
    }

    private void setUpRecyclerView(RecyclerView view, List<HomeScreenCardSection> sections) {
        view.setAdapter(buildCardAdapter(sections));
    }

    private GenericRecyclerViewAdapter<HomeScreenCardSection, HomeScreenCardSectionViewHolder> buildCardAdapter(List<HomeScreenCardSection> sections) {
        return new GenericRecyclerViewAdapter<>(getContext(),
                sections,
                R.layout.home_card_view_section,
                new HomeScreenCardSectionViewHolder.Factory(getChildFragmentManager()));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        restoreToolbar(globalTintColor);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected HomePresenter.Ui getUserInterface() {
        return this;
    }

    @Override
    protected HomePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onHomeScreenCardSectionsReady(List<HomeScreenCardSection> sections) {
        Log.d(TAG, "loaded");
        setUpRecyclerView((RecyclerView) getActivity().findViewById(R.id.recycler_view), sections);
    }

    @Override
    public void onHomeScreenCardSectionsFailed(GetCardCollectionResponseEvent event) {
        Log.e(TAG, "failed to load home screen card sections " + event.getRequestResult().toString());
        View view = getView();
        if (view != null) {
            EmptyStateViewHolder emptyStateViewHolder = new EmptyStateViewHolder(view.findViewById(R.id.home_fragment_retry));
            emptyStateViewHolder.setup(R.string.error_unable_to_load_title_503,
                    R.string.error_unable_to_load_message_504,
                    R.string.try_again_button_title_43);
            final View content = view.findViewById(R.id.home_fragment_contents);
            emptyStateViewHolder.setButtonClickListener(new EmptyStateViewHolder.EmptyStatusButtonClickedListener() {
                @Override
                public void onEmptyStateButtonClicked(EmptyStateViewHolder emptyStateViewHolder) {
                    emptyStateViewHolder.hideEmptyStateViewAndShowOtherView(content);
                    presenter.retryLoadingHomeScreenCardSections();
                }
            });
            emptyStateViewHolder.showEmptyStateViewAndHideOtherView(content);
            emptyStateViewHolder.setButtonColor(Color.parseColor(globalTintColor));
        }
    }

    @Override
    public void showLoadingIndicator() {
        toggleLoadingIndicator(View.VISIBLE, View.GONE);
    }

    @Override
    public void hideLoadingIndicator() {
        toggleLoadingIndicator(View.GONE, View.VISIBLE);
    }

    @Override
    public void navigateToVhrLandingScreen() {
        navigationCoordinator.navigateAfterActiveRewardsSelectedWithoutVhr(getActivity());
    }

    @Override
    public void navigateToActiveRewardsLanding() {
        navigationCoordinator.navigateAfterActiveRewardsSelected(getActivity());
    }

    public void startCheckingVhrStatus() {
        presenter.startCheckingVhrStatus();
    }

    @Override
    public void showVhrStatusValidationFailedAlert(GetEventStatusByPartyIdResponseEvent event) {
        if (event.getRequestResult() == RequestResult.CONNECTION_ERROR) {
            showSnackBar(R.string.active_rewards_vhr_connection_error);
        } else {
            showSnackBar(R.string.active_rewards_vhr_generic_error);
        }
    }

    public void setStatusInfo(VitalityStatusDTO vitalityStatusDTO) {
        if (parentView == null) {
            return;
        }

        parentView.findViewById(R.id.home_status_layout).setVisibility(View.VISIBLE);

        setViewContent(vitalityStatusDTO, content.getPointsStatusMessage(vitalityStatusDTO.getPointsToMaintainStatus(),
                vitalityStatusDTO.getCurrentStatusLevel().getName(),
                vitalityStatusDTO.getPointsToNextLevel(),
                vitalityStatusDTO.getNextStatusName()));

        cmsImageLoader.loadImage(parentView.<ImageView>findViewById(R.id.home_status_header_logo), getLogoFileName(), -1);
    }

    private String getLogoFileName() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        StringBuilder logoNameBuilder = new StringBuilder("logo_home_header_");
        logoNameBuilder.append(getResources().getString(R.string.current_locale));
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
        return logoNameBuilder.toString();
    }

    @Override
    public void handleStatusLevelIncreased() {
        navigationCoordinator.navigateToVitalityStatusLevelIncreased(getActivity());
    }

    private void setViewContent(VitalityStatusDTO vitalityStatusDTO, String pointsStatus) {
        ((ImageView) parentView.findViewById(R.id.home_status_icon)).setImageResource(vitalityStatusDTO.getCurrentStatusLevel().getSmallIconResourceId());
        ((TextView) parentView.findViewById(R.id.home_status_text)).setText(vitalityStatusDTO.getCurrentStatusLevel().getName());
        ((TextView) parentView.findViewById(R.id.home_status_points_text)).setText(pointsStatus);

        ((ProgressBar) parentView.findViewById(R.id.home_status_progress_bar)).setProgress(getProgressWithMinimumOfOne(vitalityStatusDTO));
    }

    private int getProgressWithMinimumOfOne(VitalityStatusDTO vitalityStatusDTO) {
        return Math.max(1, Math.min(100, vitalityStatusDTO.getProgress()));
    }

    private void toggleLoadingIndicator(int loadingIndicatorVisibility, int contentVisibility) {
        View view = getView();
        if (view != null) {
            view.findViewById(R.id.loading_indicator).setVisibility(loadingIndicatorVisibility);
            view.findViewById(R.id.home_fragment_contents).setVisibility(contentVisibility);
        }
    }

    public void showSnackBar(int stringId) {
        if (parentView != null) {
            Snackbar.make(parentView.findViewById(R.id.home_root), stringId, Snackbar.LENGTH_LONG).show();
        }
    }
}
