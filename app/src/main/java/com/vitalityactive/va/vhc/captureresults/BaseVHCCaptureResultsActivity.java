package com.vitalityactive.va.vhc.captureresults;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.uicomponents.ButtonBarConfigurator;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.CompositeRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.vhc.captureresults.viewholder.CaptureMeasurementParameters;
import com.vitalityactive.va.vhc.captureresults.viewholder.CaptureMeasurementRootTitledList;
import com.vitalityactive.va.vhc.captureresults.viewholder.EmptyViewHolder;
import com.vitalityactive.va.vhc.dto.MeasurementItem;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class BaseVHCCaptureResultsActivity
        extends BasePresentedActivity<CaptureResultsPresenter.UserInterface, CaptureResultsPresenter>
        implements ButtonBarConfigurator.OnClickListener, CaptureResultsPresenter.UserInterface,
        EventListener<AlertDialogFragment.DismissedEvent> {

    protected final String INCOMPLETE_BMI_ALERT = "INCOMPLETE_BMI_ALERT";
    @Inject
    CaptureResultsPresenter presenter;
    @Inject
    EventDispatcher eventDispatcher;
    @Inject
    InsurerConfigurationRepository insurerConfigurationRepository;
    private CaptureMeasurementRootTitledList captureMeasurementItems;

    @Override
    public void onButtonBarForwardClicked() {
        presenter.submit();
    }

    @Override
    public void showIncompleteBMIInformationAlert() {
        AlertDialogFragment alert =
                AlertDialogFragment.create(INCOMPLETE_BMI_ALERT,
                        getString(R.string.measurement_body_mass_index_title_134),
                        getString(R.string.capture_results_body_mass_index_alert_message_162),
                        getString(R.string.cancel_button_title_24),
                        null,
                        getString(R.string.continue_button_title_87));
        alert.show(getSupportFragmentManager(), INCOMPLETE_BMI_ALERT);
    }

    @Override
    public void navigateAway() {
        navigationCoordinator.navigateAfterVHCCaptureResultsCaptured(this);
    }

    @Override
    public void updateMeasurementFields(List<MeasurementItem> items) {
        captureMeasurementItems.updateItems(items);
    }

    @Override
    public void canSubmit(boolean allowed) {
        setupButtonBar().setForwardButtonEnabled(allowed);
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
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        if (eventIsOfType(event, INCOMPLETE_BMI_ALERT)
                && buttonTypeTapped(event, AlertDialogFragment.DismissedEvent.ClickedButtonType.Positive)) {
            navigateAway();
        }
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_vhc_capture_results);

        setUpActionBarWithTitle(R.string.onboarding_section_2_title_128)
                .setDisplayHomeAsUpEnabled(true);
        setupAdapters((RecyclerView) findViewById(R.id.recycler_view));
        setupButtonBar()
                .setForwardButtonTextToNext()
                .setForwardButtonOnClick(this);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        navigationCoordinator.getVHCCaptureDependencyInjector().inject(this);
    }

    @Override
    protected CaptureResultsPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected CaptureResultsPresenter getPresenter() {
        return presenter;
    }

    private void setupAdapters(RecyclerView recyclerView) {
        HashMap<Integer, GenericRecyclerViewAdapter> compositeRecyclerViewAdapters = createNestedAdapters();

        int[] viewTypes = new int[]{R.layout.vhc_capture_results_header, R.layout.capture_list_root_container};
        CompositeRecyclerViewAdapter compositeRecyclerViewAdapter = new CompositeRecyclerViewAdapter(
                compositeRecyclerViewAdapters, viewTypes);

        recyclerView.setAdapter(compositeRecyclerViewAdapter);
        ViewUtilities.scrollToTop(recyclerView);
    }

    @NonNull
    private HashMap<Integer, GenericRecyclerViewAdapter> createNestedAdapters() {
        @SuppressLint("UseSparseArrays") HashMap<Integer, GenericRecyclerViewAdapter> compositeRecyclerViewAdapters = new HashMap<>();
        @ColorInt int tintColor = ResourcesCompat.getColor(getResources(), R.color.jungle_green, getTheme());
        @ColorInt int spinnerColor = ResourcesCompat.getColor(getResources(), android.R.color.black, getTheme());
        @ColorInt int dialogValueSelectedColor = ResourcesCompat.getColor(getResources(), R.color.light_primary_87, getTheme());
        @ColorInt int dialogValueUnselectedColor = ResourcesCompat.getColor(getResources(), R.color.light_secondary_54, getTheme());
        CaptureMeasurementParameters parameters = new CaptureMeasurementParameters(tintColor,
                spinnerColor,
                dialogValueSelectedColor,
                dialogValueUnselectedColor,
                insurerConfigurationRepository.getCurrentMembershipPeriodStart());
        captureMeasurementItems = new CaptureMeasurementRootTitledList(presenter, parameters);
        createViewForOnlyCaptureMeasurementsNoteAtTop(compositeRecyclerViewAdapters);
        createViewForAllCaptureGroups(compositeRecyclerViewAdapters);
        return compositeRecyclerViewAdapters;
    }

    private void createViewForOnlyCaptureMeasurementsNoteAtTop(HashMap<Integer, GenericRecyclerViewAdapter> compositeRecyclerViewAdapters) {
        compositeRecyclerViewAdapters.put(R.layout.vhc_capture_results_header,
                new GenericRecyclerViewAdapter<>(this, captureMeasurementItems,
                        R.layout.vhc_capture_results_header,
                        new EmptyViewHolder()));
    }

    private void createViewForAllCaptureGroups(HashMap<Integer, GenericRecyclerViewAdapter> compositeRecyclerViewAdapters) {
        compositeRecyclerViewAdapters.put(R.layout.capture_list_root_container,
                new GenericRecyclerViewAdapter<>(this, captureMeasurementItems,
                        R.layout.capture_list_root_container,
                        new GenericTitledListContainerWithAdapter.Factory<>()));
    }
}
