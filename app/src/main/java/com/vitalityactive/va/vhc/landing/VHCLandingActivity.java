package com.vitalityactive.va.vhc.landing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.TitleAndSubtitle;
import com.vitalityactive.va.activerewards.viewholder.TitleAndSubtitleViewHolder;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.menu.MenuBuilder;
import com.vitalityactive.va.menu.MenuContainerViewHolder;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.uicomponents.adapters.CompositeRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.vhc.content.VHCHealthAttributeContent;
import com.vitalityactive.va.vhc.dto.HealthAttributeGroupDTO;
import com.vitalityactive.va.vhc.viewholder.CompletedMeasurementItemViewHolder;
import com.vitalityactive.va.vhc.viewholder.InCompletedMeasurementItemViewHolder;
import com.vitalityactive.va.myhealth.vitalityage.VitalityAgeActivity;
import com.vitalityactive.va.myhealth.vitalityage.VitalityAgePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class VHCLandingActivity
        extends BasePresentedActivity<VHCHealthMeasurementsPresenter.UserInterface,
        VHCHealthMeasurementsPresenter>
        implements MenuContainerViewHolder.OnMenuItemClickedListener,
        VHCHealthMeasurementsPresenter.UserInterface, EventListener<AlertDialogFragment.DismissedEvent> {

    public static final String REFRESH = "REFRESH";
    private static final String VHC_RESPONSE_GENERIC_ERROR = "VHC_RESPONSE_GENERIC_ERROR";
    private static final String VHC_RESPONSE_CONNECTION_ERROR = "VHC_RESPONSE_CONNECTION_ERROR";
    @Inject
    VHCHealthMeasurementsPresenter presenter;
    @Inject
    VitalityAgePresenter vitalityAgePresenter;
    @Inject
    EventDispatcher eventDispatcher;
    @Inject
    VHCHealthAttributeContent vhcContent;
    @Inject
    InsurerConfigurationRepository insurerConfigurationRepository;
    private List<Integer> viewTypes = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    public static final String MODE_FORM_SUBMITTED = "MODE_FORM_SUBMITTED";

    public void onVHCCaptureResultsTapped(View view) {
        navigationCoordinator.navigateAfterVHCCaptureResultsTapped(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (shouldLoadAttributes(intent)) {
            getPresenter().loadAttributes();
        }
        getPresenter().setReturnfromFormSubmission(isFromFormSubmit(intent));
    }

    private boolean shouldLoadAttributes(Intent intent) {
        return intent.getBooleanExtra(REFRESH, false);
    }

    private boolean isFromFormSubmit(Intent intent) {
        return intent.getBooleanExtra(MODE_FORM_SUBMITTED, false);
    }


    @Override
    public void onClicked(MenuItemType menuItemType) {
        navigationCoordinator.navigateOnMenuItemFromVHCHealthMeasurements(this, menuItemType);
    }

    @Override
    public void onHealthAttributesReady(List<HealthAttributeGroupDTO> incomplete,
                                        List<HealthAttributeGroupDTO> captured) {
        viewTypes = new ArrayList<>();

        setUpRecyclerView((RecyclerView) findViewById(R.id.main_recyclerview), incomplete, captured);
    }

    @Override
    public void showHealthAttributeGroupDetails(int healthAttributeGroupFeatureType) {
        navigationCoordinator.navigateAfterHealthAttributeGroupTapped(this, healthAttributeGroupFeatureType);
    }

    @Override
    public void showGenericError() {
//        if (swipeRefreshLayout.isRefreshing()) {
//            getGenericErrorSnackbar().show();
//        }
        getGenericErrorAlertDialog().show(getSupportFragmentManager(), VHC_RESPONSE_GENERIC_ERROR);

    }

//    @NonNull
//    private Snackbar getGenericErrorSnackbar() {
//        return Snackbar.make(findViewById(R.id.activity_vhc_health_measurements), R.string.wd_info_error_title, Snackbar.LENGTH_LONG)
//                .setAction(R.string.email_preference_snackbar_retry_button_text, getSnackBarRetryListener());
//    }

    @NonNull
    private View.OnClickListener getSnackBarRetryListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.setRefreshing(true);
                presenter.fetchAttributesFromService();
            }
        };
    }

    private AlertDialogFragment getGenericErrorAlertDialog() {
        return AlertDialogFragment.create(VHC_RESPONSE_GENERIC_ERROR,
                getString(R.string.error_unable_to_load_title_503),
                getString(R.string.error_unable_to_load_message_504),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
    }

    @Override
    public void showConnectionError() {
        if (swipeRefreshLayout.isRefreshing()) {
            Snackbar.make(findViewById(R.id.activity_vhc_health_measurements), R.string.connectivity_error_alert_title_44, Snackbar.LENGTH_LONG)
                    .setAction(R.string.AR_connection_error_button_retry_789, getSnackBarRetryListener()).show();
        } else {
            getConnectionErrorAlertDialog().show(getSupportFragmentManager(), VHC_RESPONSE_GENERIC_ERROR);
        }
    }

    private AlertDialogFragment getConnectionErrorAlertDialog() {
        return AlertDialogFragment.create(VHC_RESPONSE_CONNECTION_ERROR,
                getString(R.string.connectivity_error_alert_title_44),
                getString(R.string.connectivity_error_alert_message_45),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
    }

    @Override
    public void setUpPullToRefresh() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.points_monitor_swipe_refresh_layout);

        swipeRefreshLayout.setColorSchemeColors(ViewUtilities.getColorPrimaryFromTheme(this));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.fetchAttributesFromService();
            }
        });
    }

    @Override
    public void navigateToVitalityAge() {
        navigationCoordinator.navigateToVitalityAge(VHCLandingActivity.this, VitalityAgeActivity.VHC__DONE_VHR_PENDING_MODE);
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        if (buttonTypeTapped(event, AlertDialogFragment.DismissedEvent.ClickedButtonType.Positive)) {
            presenter.loadAttributes();
        }
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_vhc_health_measurements);

        setUpActionBarWithTitle(R.string.home_card_card_title_125)
                .setDisplayHomeAsUpEnabled(true);
        if (getIntent().getExtras() != null) {
            presenter.setReturnfromFormSubmission(getIntent().getExtras().getBoolean(MODE_FORM_SUBMITTED, false));
        }
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected VHCHealthMeasurementsPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected void resume() {
        eventDispatcher.addEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    @Override
    protected void pause() {
        eventDispatcher.removeEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    @Override
    protected VHCHealthMeasurementsPresenter getPresenter() {
        return presenter;
    }

    private void setUpRecyclerView(final RecyclerView recyclerView,
                                   List<HealthAttributeGroupDTO> incomplete,
                                   List<HealthAttributeGroupDTO> captured) {
        HashMap<Integer, GenericRecyclerViewAdapter> adapters = createAdapters(incomplete, captured);

        CompositeRecyclerViewAdapter containersRecyclerViewAdapter =
                new CompositeRecyclerViewAdapter(adapters, getViewTypesIntArray());

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(containersRecyclerViewAdapter);

        ViewUtilities.scrollToTop(recyclerView);
    }

    @NonNull
    private HashMap<Integer, GenericRecyclerViewAdapter> createAdapters(List<HealthAttributeGroupDTO> incomplete,
                                                                        List<HealthAttributeGroupDTO> captured) {
        @SuppressLint("UseSparseArrays") HashMap<Integer, GenericRecyclerViewAdapter> adapters = new HashMap<>();

        adapters.put(R.layout.vhc_landing_header_layout, createHeaderAdapter());

        if (incomplete != null)
            adapters.put(R.layout.vhc_measurement_item_incomplete,
                    createInCompletedHealthMeasurementAdapter(incomplete));

        if (captured != null)
            adapters.put(R.layout.vhc_measurement_item_completed,
                    createCompletedHealthMeasurementAdapter(captured));

        adapters.put(R.layout.menu_container, createMenuContainerAdapter());
        return adapters;
    }

    private GenericRecyclerViewAdapter createHeaderAdapter() {
        TitleAndSubtitle titleAndSubtitle = new TitleAndSubtitle(vhcContent.getLandingTitle(),
                vhcContent.getLandingSubtitle());

        viewTypes.add(R.layout.vhc_landing_header_layout);

        return new GenericRecyclerViewAdapter<>(this,
                titleAndSubtitle,
                R.layout.vhc_landing_header_layout,
                new TitleAndSubtitleViewHolder.Factory());
    }

    private int[] getViewTypesIntArray() {
        int[] array = new int[viewTypes.size()];

        for (int i = 0; i < viewTypes.size(); i++)
            array[i] = viewTypes.get(i);

        return array;
    }

    private GenericRecyclerViewAdapter createInCompletedHealthMeasurementAdapter(List<HealthAttributeGroupDTO> groups) {
        for (int i = 0; i < groups.size(); i++)
            viewTypes.add(R.layout.vhc_measurement_item_incomplete);

        return new GenericRecyclerViewAdapter<>(this,
                groups,
                R.layout.vhc_measurement_item_incomplete,
                new InCompletedMeasurementItemViewHolder.Factory());
    }

    private GenericRecyclerViewAdapter createCompletedHealthMeasurementAdapter(List<HealthAttributeGroupDTO> groups) {
        for (int i = 0; i < groups.size(); i++)
            viewTypes.add(R.layout.vhc_measurement_item_completed);

        return new GenericRecyclerViewAdapter<>(this,
                groups,
                R.layout.vhc_measurement_item_completed,
                new CompletedMeasurementItemViewHolder.Factory(
                        insurerConfigurationRepository.getCurrentMembershipPeriodStart(),
                        insurerConfigurationRepository.getCurrentMembershipPeriodEnd()),
                new GenericRecyclerViewAdapter.OnItemClickListener<HealthAttributeGroupDTO>() {
                    @Override
                    public void onClicked(int position, HealthAttributeGroupDTO item) {
                        presenter.onCompletedHealthAttributeGroupTapped(item);
                    }
                });
    }

    private GenericRecyclerViewAdapter createMenuContainerAdapter() {
        viewTypes.add(R.layout.menu_container);

        MenuBuilder.MenuItemSet menuItemSet =
                insurerConfigurationRepository.hasFeatureTypeVHCTemplate()
                        ? MenuBuilder.MenuItemSet.HealthCareLearnMore
                        : MenuBuilder.MenuItemSet.LearnMore;

        return new MenuBuilder(this)
                .setClickListener(this)
                .setMenuItems(menuItemSet)
                .build();
    }

    @Override
    public void hideLoadingIndicator() {
        super.hideLoadingIndicator();

        swipeRefreshLayout.setRefreshing(false);
    }
}
