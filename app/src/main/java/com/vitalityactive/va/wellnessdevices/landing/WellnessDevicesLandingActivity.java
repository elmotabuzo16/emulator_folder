package com.vitalityactive.va.wellnessdevices.landing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.menu.MenuBuilder;
import com.vitalityactive.va.menu.MenuContainerViewHolder;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.wellnessdevices.Constants;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;
import com.vitalityactive.va.wellnessdevices.landing.uicontainers.AvailableDevicesContainer;
import com.vitalityactive.va.wellnessdevices.landing.uicontainers.GetLinkingContainer;
import com.vitalityactive.va.wellnessdevices.landing.uicontainers.LinkedDevicesContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.vitalityactive.va.wellnessdevices.landing.WellnessDevicesLandingInteractor.WD_FETCH_DEVICE_LIST;

public class WellnessDevicesLandingActivity
        extends BasePresentedActivity<WellnessDevicesLandingPresenter.UserInterface, WellnessDevicesLandingPresenter>
        implements MenuContainerViewHolder.OnMenuItemClickedListener, WellnessDevicesLandingPresenter.UserInterface,
        EventListener<AlertDialogFragment.DismissedEvent> {
    private static final String TAG = "WD_LANDING";
    private static final String WD_TRY_AGAIN_ERROR_ALERT = "REGISTRATION_TRY_AGAIN_ERROR_ALERT";
    @Inject
    WellnessDevicesLandingPresenter presenter;
    @Inject
    EventDispatcher eventDispatcher;
    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            presenter.updateDeviceList(false);
        }
    };
    private SwipeRefreshLayout swipeRefreshLayout;
    private AvailableDevicesContainer availableDevicesHolder;
    private LinkedDevicesContainer linkedDevicesHolder;
    private ContainersRecyclerViewAdapter containersRecyclerViewAdapter;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_wd_landing);
        setUpActionBarWithTitle(R.string.WDA_title_414)
                .setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeColors(getThemeAccentColor());
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        findViewById(R.id.next_button_bar).setVisibility(isInsideActionRewardsFlow() ? View.VISIBLE : View.GONE);

        GenericRecyclerViewAdapter.OnItemClickListener<PartnerDto> availableForLinkingClickListener =
                new GenericRecyclerViewAdapter.OnItemClickListener<PartnerDto>() {
                    @Override
                    public void onClicked(int position, PartnerDto partner) {
                        Log.d(TAG, String.format("click on unlinked device %s", partner.getDevice()));
                        if (!swipeRefreshLayout.isRefreshing()) {
                            // disable click on item to avoid case when this device will be absent in updated response
                            navigationCoordinator.navigateToAvailableDeviceDetails(WellnessDevicesLandingActivity.this, partner);
                        }
                    }
                };

        GenericRecyclerViewAdapter.OnItemClickListener<PartnerDto> linkedClickListener =
                new GenericRecyclerViewAdapter.OnItemClickListener<PartnerDto>() {
                    @Override
                    public void onClicked(int position, PartnerDto partner) {
                        Log.d(TAG, String.format("click on linked device %s", partner.getDevice()));
                        if (!swipeRefreshLayout.isRefreshing()) {
                            // disable click on item to avoid case when this device will be absent in updated response
                            navigationCoordinator.navigateToAvailableDeviceDetails(WellnessDevicesLandingActivity.this, partner);
                        }
                    }
                };

        availableDevicesHolder = new AvailableDevicesContainer(this, availableForLinkingClickListener);

        linkedDevicesHolder = new LinkedDevicesContainer(this, linkedClickListener);

        setUpRecyclerView((RecyclerView) findViewById(R.id.main_recyclerview));
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
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected WellnessDevicesLandingPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected WellnessDevicesLandingPresenter getPresenter() {
        return presenter;
    }

    private void setUpRecyclerView(final RecyclerView recyclerView) {
        ArrayList<GenericRecyclerViewAdapter> adapters = setupAdapters();
        containersRecyclerViewAdapter = new ContainersRecyclerViewAdapter(adapters);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(containersRecyclerViewAdapter);
        ViewUtilities.scrollToTop(recyclerView);
    }

    private ArrayList<GenericRecyclerViewAdapter> setupAdapters() {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();
        adapters.add(createLinkedDevicesContainer());
        if (!availableDevicesHolder.isEmpty()) {
            adapters.add(createAvailableDevicesContainer());
        }
        adapters.add(createMenuContainerAdapter());
        return adapters;
    }

    private GenericRecyclerViewAdapter createLinkedDevicesContainer() {
        if (linkedDevicesHolder.isEmpty()) {
            return createLinkedDevicesContainerEmpty();
        } else {
            return createLinkedDevicesContainerNotEmpty();
        }
    }

    private GenericRecyclerViewAdapter createLinkedDevicesContainerEmpty() {
        return new GenericRecyclerViewAdapter<>(this,
                Collections.singletonList("one"),
                R.layout.view_wd_landing_getlinking,
                new GetLinkingContainer.Factory());
    }

    private GenericRecyclerViewAdapter createLinkedDevicesContainerNotEmpty() {
        return new GenericRecyclerViewAdapter<>(this,
                linkedDevicesHolder,
                R.layout.view_wd_landing_linked_devices,
                new GenericTitledListContainerWithAdapter.Factory<>());
    }

    private GenericRecyclerViewAdapter createAvailableDevicesContainer() {
        return new GenericRecyclerViewAdapter<>(this,
                availableDevicesHolder,
                R.layout.view_wd_landing_available_devices,
                new GenericTitledListContainerWithAdapter.Factory<>());
    }

    private GenericRecyclerViewAdapter createMenuContainerAdapter() {
        return new MenuBuilder(this)
                .setClickListener(this)
                .setMenuItems(MenuBuilder.MenuItemSet.LearnMore)
                .build();
    }

    @Override
    public void onClicked(MenuItemType menuItemType) {
        if (MenuItemType.Help.equals(menuItemType)) {
            // TODO implement
        } else if (MenuItemType.LearnMore.equals(menuItemType)) {
            navigationCoordinator.navigateToWellnessDevicesLearnMore(this);
        }
    }

    @Override
    public void updateLinkedDevices(final List<PartnerDto> available,
                                    final List<PartnerDto> linked) {
        availableDevicesHolder.setDevices(available);
        linkedDevicesHolder.setDevices(linked);
        setUpRecyclerView((RecyclerView) findViewById(R.id.main_recyclerview));
    }

    @Override
    public void hideLoadingIndicator() {
        super.hideLoadingIndicator();
        hideSwipeLoadingIndicator();
    }

    @Override
    public void showConnectionErrorMessage(@WellnessDevicesLandingInteractor.RequestType String requestType) {
        AlertDialogFragment.create(requestType,
                getString(R.string.connectivity_error_alert_title_44),
                getString(R.string.connectivity_error_alert_message_45),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43))
                .show(getSupportFragmentManager(), requestType);
    }

    @Override
    public void showGenericErrorMessage(@WellnessDevicesLandingInteractor.RequestType String requestType) {
        AlertDialogFragment.create(requestType,
                getString(R.string.generic_unkown_error_title_266),
                getString(R.string.generic_unkown_error_message_267),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43))
                .show(getSupportFragmentManager(), requestType);
    }

    @Override
    public void handleSuccessfullPointsResponse() {
        // NOP
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        if (event.getType().equals(WD_FETCH_DEVICE_LIST)) {
            switch (event.getClickedButtonType()) {
                case Positive:
                    presenter.updateDeviceList(true);
                    break;
                case Negative:
                    finish();
                    break;
                default: // NOP
            }
        } else {
            switch (event.getClickedButtonType()) {
                case Positive:
                    presenter.fetchDeviceActivityMapping();
                    break;
                case Negative:
                    finish();
                    break;
                default: // NOP
            }
        }
    }

    public void onLinkWellnessDevicesNextButtonClicked(View view) {
        navigationCoordinator.navigateAfterDoneLinkingDevicesFromOnboarding(this);
    }

    private void hideSwipeLoadingIndicator() {
        swipeRefreshLayout.setRefreshing(false);
    }

    private boolean isInsideActionRewardsFlow() {
        return getIntent().getBooleanExtra(Constants.IS_INSIDE_ACTIVE_REWARDS_FLOW, false);
    }

    public int getThemeAccentColor() {
        final TypedValue value = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        return value.data;
    }
}
