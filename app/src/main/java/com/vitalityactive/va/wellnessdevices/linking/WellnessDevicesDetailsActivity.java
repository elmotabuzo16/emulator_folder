package com.vitalityactive.va.wellnessdevices.linking;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.wellnessdevices.WdEventType;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;
import com.vitalityactive.va.wellnessdevices.landing.uicontainers.GetLinkingContainer;
import com.vitalityactive.va.wellnessdevices.linking.containers.FooterContainer;
import com.vitalityactive.va.wellnessdevices.linking.containers.HeaderContainerLinked;
import com.vitalityactive.va.wellnessdevices.linking.containers.MeasurementsContainer;
import com.vitalityactive.va.wellnessdevices.linking.containers.PointsEarningMetricsContainer;
import com.vitalityactive.va.wellnessdevices.linking.web.WellnessDevicesWebActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import static com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesLinkingInteractor.WD_DELINK_DEVICE;
import static com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesLinkingInteractor.WD_LINK_DEVICE;

public class WellnessDevicesDetailsActivity extends BasePresentedActivity<WellnessDevicesLinkingPresenter.UserInterface, WellnessDevicesLinkingPresenter>
        implements View.OnClickListener,
        GenericRecyclerViewAdapter.OnItemClickListener<MeasurementsContainer.MeasurementItem>,
        WellnessDevicesLinkingPresenter.UserInterface,
        EventListener<AlertDialogFragment.DismissedEvent> {
    public final static String KEY_DEVICE = "KEY_DEVICE";
    private static final String WD_DELINK_DIALOG = "WD_DELINK_DIALOG";
    private static final int WEB_ACTIVITY_REQUEST_CODE = 101;
    @Inject
    WellnessDevicesLinkingPresenter presenter;
    @Inject
    EventDispatcher eventDispatcher;

    private ContainersRecyclerViewAdapter containersRecyclerViewAdapter;
    private MeasurementsContainer physicalActivityContainer;
    private MeasurementsContainer measurementsContainer;
    private MeasurementsContainer otherContainer;

    private CustomTabsClient mCustomTabsClient = null;
    CustomTabsServiceConnection connection = new CustomTabsServiceConnection() {
        @Override
        public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
            mCustomTabsClient = client;
            prepareBrowserPage(presenter.getAssets().getPartnerWebsiteUrl());
            unbindService(connection);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mCustomTabsClient = null;
        }
    };
    private CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_wd_available_device_detail);

        presenter.setPartner((PartnerDto) getIntent().getParcelableExtra(KEY_DEVICE));
        setUpActionBarWithTitle(presenter.getPartner().getDevice()).setDisplayHomeAsUpEnabled(true);

        physicalActivityContainer = new MeasurementsContainer(this, getString(R.string.WDA_physical_activity_section_header_438), this);
        measurementsContainer = new MeasurementsContainer(this, getString(R.string.WDA_health_measurements_section_header_445), this);
        otherContainer = new MeasurementsContainer(this, getString(R.string.WDA_device_detail_other_section_header_448), this);
        setUpContainers();

        updateUi();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!TextUtils.isEmpty(presenter.getAssets().getPartnerWebsiteUrl())) {
            CustomTabsClient.bindCustomTabsService(this, "com.android.chrome", connection);
        }
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
        navigationCoordinator.getWDALinkingDependencyInjector().inject(this);
    }

    @Override
    protected WellnessDevicesLinkingPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected WellnessDevicesLinkingPresenter getPresenter() {
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
        adapters.add(createHeaderContainer(presenter.isLinked()));
        adapters.add(createPointsEarningContainer());
        if (!physicalActivityContainer.isEmpty()) {
            adapters.add(createPhysicalActivityContainer());
        }
        if (!measurementsContainer.isEmpty()) {
            adapters.add(createMeasurementsContainer());
        }
        if (!otherContainer.isEmpty()) {
            adapters.add(createOtherContainer());
        }
        adapters.add(createAboutContainer());
        return adapters;
    }

    private GenericRecyclerViewAdapter createHeaderContainer(boolean isLinked) {
        return isLinked ? createLinkedHeaderContainer() : createNotLinkedHeaderContainer();
    }

    private GenericRecyclerViewAdapter createNotLinkedHeaderContainer() {
        return new GenericRecyclerViewAdapter<>(this,
                Collections.singletonList("one"),
                R.layout.view_wd_linking_header,
                new GetLinkingContainer.Factory());
    }

    private GenericRecyclerViewAdapter createLinkedHeaderContainer() {
        return new GenericRecyclerViewAdapter<>(this,
                Collections.singletonList(presenter.getPartner().getPartnerLastSync()),
                R.layout.view_wd_linking_header_linked,
                new HeaderContainerLinked.Factory());
    }

    private GenericRecyclerViewAdapter createPointsEarningContainer() {
        boolean partnerHasURL = !TextUtils.isEmpty(presenter.getAssets().getPartnerWebsiteUrl());

        return new GenericRecyclerViewAdapter<>(this,
                Collections.singletonList("one"),
                R.layout.view_wd_linking_points_earning_metrics,
                new PointsEarningMetricsContainer.Factory(this, this, partnerHasURL));
    }

    private GenericRecyclerViewAdapter createPhysicalActivityContainer() {
        return new GenericRecyclerViewAdapter<>(this,
                physicalActivityContainer,
                R.layout.view_wd_linking_measurement_container,
                new GenericTitledListContainerWithAdapter.Factory<>());
    }

    private GenericRecyclerViewAdapter createMeasurementsContainer() {
        return new GenericRecyclerViewAdapter<>(this,
                measurementsContainer,
                R.layout.view_wd_linking_measurement_container,
                new GenericTitledListContainerWithAdapter.Factory<>());
    }

    private GenericRecyclerViewAdapter createOtherContainer() {
        return new GenericRecyclerViewAdapter<>(this,
                otherContainer,
                R.layout.view_wd_linking_measurement_container,
                new GenericTitledListContainerWithAdapter.Factory<>());
    }

    private GenericRecyclerViewAdapter createAboutContainer() {
        return new GenericRecyclerViewAdapter<>(this,
                Collections.singletonList(presenter.getPartner().getDevice()),
                R.layout.view_wd_linking_footer,
                new FooterContainer.Factory());
    }

    private List<MeasurementsContainer.MeasurementItem> getStaticPhysicalActivities() {
        List<MeasurementsContainer.MeasurementItem> physicalActivityList = new ArrayList<>();
        if (presenter.manufacturerContainsTypeKey(WdEventType.HEART_RATE.getKey())) {
            physicalActivityList.add(new MeasurementsContainer.MeasurementItem(WdEventType.HEART_RATE));
        }
        if (presenter.manufacturerContainsTypeKey(WdEventType.DISTANCE.getKey())) {
            physicalActivityList.add(new MeasurementsContainer.MeasurementItem(WdEventType.DISTANCE));
        }
        if (presenter.manufacturerContainsTypeKey(WdEventType.SPEED.getKey())) {
            physicalActivityList.add(new MeasurementsContainer.MeasurementItem(WdEventType.SPEED));
        }
        if (presenter.manufacturerContainsTypeKey(WdEventType.STEPS.getKey())) {
            physicalActivityList.add(new MeasurementsContainer.MeasurementItem(WdEventType.STEPS));
        }
        if (presenter.manufacturerContainsTypeKey(WdEventType.CALORIES_BURNED.getKey())) {
            physicalActivityList.add(new MeasurementsContainer.MeasurementItem(WdEventType.CALORIES_BURNED));
        }
        if (presenter.manufacturerContainsTypeKey(WdEventType.CALORIES_ACTIVE.getKey())) {
            physicalActivityList.add(new MeasurementsContainer.MeasurementItem(WdEventType.CALORIES_ACTIVE));
        }
        return physicalActivityList;
    }

    private List<MeasurementsContainer.MeasurementItem> getStaticMeasurements() {
        List<MeasurementsContainer.MeasurementItem> physicalActivityList = new ArrayList<>();
        if (presenter.manufacturerContainsTypeKey(WdEventType.WEIGHT.getKey())) {
            physicalActivityList.add(new MeasurementsContainer.MeasurementItem(WdEventType.WEIGHT));
        }
        if (presenter.manufacturerContainsTypeKey(WdEventType.HEIGHT.getKey())) {
            physicalActivityList.add(new MeasurementsContainer.MeasurementItem(WdEventType.HEIGHT));
        }
        return physicalActivityList;
    }

    private List<MeasurementsContainer.MeasurementItem> getStaticOtherContent() {
        List<MeasurementsContainer.MeasurementItem> physicalActivityList = new ArrayList<>();
        if (presenter.manufacturerContainsTypeKey(WdEventType.SLEEP.getKey())) {
            physicalActivityList.add(new MeasurementsContainer.MeasurementItem(WdEventType.SLEEP));
        }
        return physicalActivityList;
    }

    private void setUpContainers() {
        List<MeasurementsContainer.MeasurementItem> physicalActivities = getStaticPhysicalActivities();
        List<MeasurementsContainer.MeasurementItem> measurements = getStaticMeasurements();
        List<MeasurementsContainer.MeasurementItem> otherContent = getStaticOtherContent();

        sortListByItemTitle(physicalActivities);
        sortListByItemTitle(measurements);
        sortListByItemTitle(otherContent);

        physicalActivityContainer.setMeasurements(physicalActivities);
        measurementsContainer.setMeasurements(getStaticMeasurements());
        otherContainer.setMeasurements(getStaticOtherContent());
    }

    private void sortListByItemTitle(List<MeasurementsContainer.MeasurementItem> physicalActivities) {
        Collections.sort(physicalActivities, new Comparator<MeasurementsContainer.MeasurementItem>() {
            @Override
            public int compare(MeasurementsContainer.MeasurementItem first, MeasurementsContainer.MeasurementItem second) {
                String firstItemTitle = getString(first.getTitleResourceId());
                String secondItemTitle = getString(second.getTitleResourceId());
                return firstItemTitle.compareToIgnoreCase(secondItemTitle);
            }
        });
    }

    public void onLinkNowClicked(View view) {
        if (presenter.shouldShowWDPrivacyPolicy()) {
            navigationCoordinator.navigateToWellnessDevicesPrivacyPolicy(this, presenter.getPartner(), WEB_ACTIVITY_REQUEST_CODE);
        } else {
            presenter.linkDevice();
        }
    }

    public void onStepsToLinkClicked(View view) {
        navigationCoordinator.navigateToWellnessDevicesInfoActivity(this,
                getString(R.string.steps_to_link_button_text_471),
                presenter.getAssets().getStepsToLinkContentId());
    }

    public void onHavingTroublesClicked(View view) {
        // TODO implement
    }

    public void onDelinkClicked(View view) {
        showDelinkConfirmationDialog();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_wd_linking_points_description) {
            // Learn more clicked
            if (!TextUtils.isEmpty(presenter.getAssets().getPartnerWebsiteUrl())) {
                if (mCustomTabsClient != null &&
                        builder != null) {
                    builder.build().launchUrl(this, Uri.parse(presenter.getAssets().getPartnerWebsiteUrl()));
                } else {
                    navigationCoordinator.navigateToWellnessDevicesBrowser(this, presenter.getAssets().getPartnerWebsiteUrl());
                }
            } else {
                // TODO: 2017/09/06 Learn more span on page should be removed if no url found
//                showEmptyDataAlert();
            }
        }
    }

    @Override
    public void onClicked(int position, MeasurementsContainer.MeasurementItem item) {
        // Physical Activity
        // Health Measurement
        // Other
        final int key = item.getEventType().getKey();
        if (presenter.manufacturerContainsTypeKey(key) &&
                presenter.getPotentialPoints(key) != null &&
                presenter.getPotentialPoints(key).getPointsEntryDetails() != null &&
                !presenter.getPotentialPoints(key).getPointsEntryDetails().isEmpty()) { // TODO these checks are redundant, added them just to not fail on test data
            navigationCoordinator.navigateToWellnessDevicesPointsMonitor(this, key);
        } else {
            // TODO show error
        }
    }

    public void onAboutButtonClicked(View view) {
        navigationCoordinator.navigateToWellnessDevicesInfoActivity(this,
                getString(R.string.about_text_450, presenter.getPartner().getDevice()),
                presenter.getAssets().getAboutPartnerContentId());
    }

    @Override
    @SuppressWarnings("ResourceType")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WEB_ACTIVITY_REQUEST_CODE && data != null) {
            final String requestTypeStr = data.getBundleExtra(WellnessDevicesWebActivity.KEY_WEB_EXTRA)
                    .getString(WellnessDevicesWebActivity.KEY_REQUEST_TYPE);
            if (resultCode == RESULT_CANCELED && isResumedNow()) {
                showGenericErrorMessage(requestTypeStr);
            } else if (resultCode == RESULT_OK) {
                if (requestTypeStr.equals(WD_LINK_DEVICE)) {
                    presenter.setLinked(true);
                    showLinkSuccessfullMessage();
                    presenter.sendUpdateTokenRequest();
                } else if (requestTypeStr.equals(WD_DELINK_DEVICE)) {
                    presenter.setLinked(false);
                    showDelinkSuccessfullMessage();
                }
                updateUi();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void updateUi() {
        setUpRecyclerView((RecyclerView) findViewById(R.id.main_recyclerview));
    }

    @Override
    public void showProgressWithText(@WellnessDevicesLinkingInteractor.RequestType String requestType) {
        showLoadingIndicator();
    }

    @Override
    public void showConnectionErrorMessage(@WellnessDevicesLinkingInteractor.RequestType String requestType) {
        int titleStringId = getDialogTitle(requestType);
        int textStringId = getDialogMessage(requestType);
        AlertDialogFragment.create(requestType,
                getString(titleStringId),
                getString(textStringId),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43))
                .show(getSupportFragmentManager(), requestType);
    }

    @Override
    public void showGenericErrorMessage(@WellnessDevicesLinkingInteractor.RequestType String requestType) {
        int titleStringId = getDialogTitle(requestType);
        int textStringId = getDialogMessage(requestType);
        AlertDialogFragment.create(requestType,
                getString(titleStringId),
                getString(textStringId),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43))
                .show(getSupportFragmentManager(), requestType);
    }

    @Override
    public void showLinkSuccessfullMessage() {
        showSnackBar(R.string.device_linking_successfully_linked_454);
    }

    @Override
    public void showDelinkSuccessfullMessage() {
        showSnackBar(R.string.device_linking_successfully_delinked_470);
    }

    @Override
    public void redirectToPartnerWebSite(String redirectUrl,
                                         @WellnessDevicesLinkingInteractor.RequestType String requestType) {
        hideLoadingIndicator();
        navigationCoordinator.navigateToWellnessDevicesWebLinkFlow(this, redirectUrl, requestType, WEB_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void showDelinkConfirmationDialog() {
        AlertDialogFragment.create(WD_DELINK_DIALOG,
                getString(R.string.WDA_delink_dialog_title_464),
                getString(R.string.delink_dialog_description_465, presenter.getPartner().getDevice()),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.WDA_delink_dialog_title_464).toUpperCase())
                .show(getSupportFragmentManager(), WD_DELINK_DIALOG);
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        if (event.getType().equals(WD_DELINK_DIALOG)) {
            switch (event.getClickedButtonType()) {
                case Positive:
                    presenter.delinkDevice();
                    break;
                default:
                    break; // NOP
            }
        } else if (event.getType().equals(WD_LINK_DEVICE)) {
            switch (event.getClickedButtonType()) {
                case Positive:
                    presenter.linkDevice();
                    break;
                default:
                    break; // NOP
            }
        } else if (event.getType().equals(WD_DELINK_DEVICE)) {
            switch (event.getClickedButtonType()) {
                case Positive:
                    presenter.delinkDevice();
                    break;
                default:
                    break; // NOP
            }
        }
    }

    private void showSnackBar(@StringRes int stringId) {
        Snackbar.make(findViewById(R.id.activity_link_wellness_devices), stringId, Snackbar.LENGTH_SHORT).show();
    }

    @StringRes
    private int getDialogTitle(@WellnessDevicesLinkingInteractor.RequestType String requestType) {
        switch (requestType) {
            case WD_LINK_DEVICE:
                return R.string.WDA_device_detail_sync_failed_dialog_title_512;
            case WD_DELINK_DEVICE:
                return R.string.WDA_device_detail_delink_failed_dialog_title_468;
            default:
                return R.string.error;
        }
    }

    @StringRes
    private int getDialogMessage(@WellnessDevicesLinkingInteractor.RequestType String requestType) {
        switch (requestType) {
            case WD_LINK_DEVICE:
                return R.string.WDA_device_detail_sync_failed_dialog_message_513;
            case WD_DELINK_DEVICE:
                return R.string.delink_failed_dialog_description_469;
            default:
                return R.string.try_again;
        }
    }

    private void prepareBrowserPage(String url) {
        mCustomTabsClient.warmup(0);

        CustomTabsSession session = mCustomTabsClient.newSession(new CustomTabsCallback());
        session.mayLaunchUrl(Uri.parse(url), null, null);
    }

//    public void showEmptyDataAlert() {
//        AlertDialogFragment.create("",
//                null,
//                getString(R.string.wd_linking_no_data),
//                null,
//                null,
//                null)
//                .show(getSupportFragmentManager(), "");
//    }
}
