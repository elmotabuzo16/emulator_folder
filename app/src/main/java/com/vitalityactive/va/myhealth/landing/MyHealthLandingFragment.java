package com.vitalityactive.va.myhealth.landing;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vitalityactive.va.BasePresentedFragment;
import com.vitalityactive.va.NavigationCoordinator;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.myhealth.content.MyHealthContent;
import com.vitalityactive.va.myhealth.content.VitalityAgeData;
import com.vitalityactive.va.myhealth.entity.VitalityAge;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;


public class MyHealthLandingFragment extends BasePresentedFragment<MyHealthLandingPresenter.UserInterface, MyHealthLandingPresenter> implements GenericRecyclerViewAdapter.OnItemClickListener<VitalityAgeData>, MyHealthLandingPresenter.UserInterface {

    ContainersRecyclerViewAdapter containersRecyclerViewAdapter;
    @Inject
    NavigationCoordinator navigationCoordinator;
    @Inject
    MyHealthLandingPresenter myHealthPresenter;
    @Inject
    AppConfigRepository appConfigRepository;
    ArrayList<GenericRecyclerViewAdapter> adapters;

    @Inject
    EventDispatcher eventDispatcher;

    public MyHealthLandingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies(getDependencyInjector());
        setUpRecyclerView();
        getPresenter().setUserInterface(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_health_landing, container, false);
    }

    @Override
    public void activityCreated(@Nullable Bundle savedInstanceState) {
        setUpToolbar();
        setUpRecyclerView();
    }

    private void setUpToolbar() {
        setToolbarTransparent();
    }

    private void setToolbarTransparent() {
        getActivity().findViewById(R.id.toolbar).setBackgroundColor(getColour(android.R.color.transparent));
    }

    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected MyHealthLandingPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected MyHealthLandingPresenter getPresenter() {
        return myHealthPresenter;
    }

    private void setUpRecyclerView() {
        if (getView() == null) {
            return;
        }
        RecyclerView recyclerView = getView().findViewById(R.id.myhealth_recyclerview);
        adapters = setUpAdapters(null);
        containersRecyclerViewAdapter = new ContainersRecyclerViewAdapter(adapters);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ViewUtilities.addDividers(getActivity(), recyclerView, getResources().getDimensionPixelSize(R.dimen.item_divider_inset));
        recyclerView.setAdapter(containersRecyclerViewAdapter);
        ViewUtilities.scrollToTop(recyclerView);
    }

    private GenericRecyclerViewAdapter createVitalityAgeAdapter(VitalityAgeData vitalityAgeDetail) {
        List<VitalityAgeData> vitalityAgeDetails = new ArrayList<>();
        vitalityAgeDetails.add(vitalityAgeDetail);
        return new GenericRecyclerViewAdapter<>(getActivity(), vitalityAgeDetails, R.layout.myhealth_vitality_age_card, new MyHealthLandingVitalityAgeViewHolder.Factory(getActivity(), this));
    }

    private ArrayList<GenericRecyclerViewAdapter> setUpAdapters(VitalityAgeData vitalityAgeDetail) {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();
        adapters.add(createVitalityAgeAdapter(vitalityAgeDetail));
        return adapters;
    }

    @Override
    public void onClicked(int position, VitalityAgeData vitalityAgeData) {
        if (!vitalityAgeData.isUnknown) {
            navigationCoordinator.navigateToMyHealthVitalityAgeProfile(getActivity());
        } else {
            navigationCoordinator.navigateAfterVitalityAgeCardLearnMoreButtonClicked(getActivity(), VitalityAgeConstants.VA_UNKNOWN, VitalityAgeConstants.VA_UNKNOWN, null, null);
        }
    }

    private void showVitalityAgeOnScreen(String vitalityAgeValue, String summary, int feedbackType, String variation) {
        final VitalityAgeData vitalyAgeData = MyHealthContent.getVitalityAgeSummary(getActivity(), vitalityAgeValue, summary, feedbackType, variation);
        if (vitalyAgeData != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    adapters.get(0).replaceData(Collections.singletonList(vitalyAgeData));
                    containersRecyclerViewAdapter.notifyDataSetChanged();

                }
            });
        }
    }

    @Override
    public void loadVitalityAge(VitalityAge vitalityAge) {
        if (vitalityAge != null) {
            showVitalityAgeOnScreen(vitalityAge.getAge(), vitalityAge.getFeedbackTitle(), vitalityAge.getEffectiveType(), vitalityAge.getVariance());
        } else {
            showVitalityAgeOnScreen(null, null, 0, null);
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

    private void toggleLoadingIndicator(final int loadingIndicatorVisibility, final int contentVisibility) {
        final View view = getView();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (view != null) {
                    view.findViewById(R.id.loading_indicator).setVisibility(loadingIndicatorVisibility);
                    view.findViewById(R.id.myhealth_landing_contents).setVisibility(contentVisibility);
                }
            }
        });
    }

    private @ColorInt
    int globalTintColor() {
        return Color.parseColor(appConfigRepository.getGlobalTintColorHex());
    }
}
