package com.vitalityactive.va.vhc;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.UnitsOfMeasure;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.myhealth.service.VitalityAgeServiceClient;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;
import com.vitalityactive.va.vhc.addproof.VHCProofItemRepository;
import com.vitalityactive.va.vhc.captureresults.models.CapturedField;
import com.vitalityactive.va.vhc.captureresults.models.CapturedGroup;
import com.vitalityactive.va.vhc.captureresults.models.GroupType;
import com.vitalityactive.va.vhc.content.VHCHealthAttributeContent;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepository;
import com.vitalityactive.va.vhc.submission.VHCSubmissionRequestCompletedEvent;
import com.vitalityactive.va.vhc.submission.VHCSubmitter;
import com.vitalityactive.va.vhc.summary.PresentableProof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VHCSummaryPresenterImpl implements VHCSummaryPresenter {
    private HealthAttributeRepository repository;
    private final DateFormattingUtilities dateFormattingUtilities;
    private VHCHealthAttributeContent content;
    private VHCProofItemRepository addProofRepository;
    private VHCSubmitter vhcSubmitter;
    private InsurerConfigurationRepository insurerConfigurationRepository;
    private UserInterface userInterface;
    private EventDispatcher eventDispatcher;
    private Scheduler scheduler;
    private boolean hasShownRequestErrorMessage = false;
    VitalityAgeServiceClient vitalityAgeServiceClient;
    private EventListener<VHCSubmissionRequestCompletedEvent> submissionRequestCompletedEventListener = new EventListener<VHCSubmissionRequestCompletedEvent>() {
        @Override
        public void onEvent(VHCSubmissionRequestCompletedEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    userInterface.hideLoadingIndicator();
                    if (shouldShowRequestErrorMessage()) {
                        showRequestErrorMessage();
                    } else {
                        userInterface.navigateAfterUserConfirmed();
                        vitalityAgeServiceClient.getVitalityAgeValue();
                    }
                }
            });
        }
    };

    public VHCSummaryPresenterImpl(HealthAttributeRepository repository,
                                   DateFormattingUtilities dateFormattingUtilities,
                                   VHCProofItemRepository addProofRepository,
                                   VHCSubmitter vhcSubmitter,
                                   InsurerConfigurationRepository insurerConfigurationRepository,
                                   EventDispatcher eventDispatcher,
                                   Scheduler scheduler,
                                   VHCHealthAttributeContent content, VitalityAgeServiceClient vitalityAgeServiceClient) {
        this.repository = repository;
        this.dateFormattingUtilities = dateFormattingUtilities;
        this.content = content;
        this.addProofRepository = addProofRepository;
        this.vhcSubmitter = vhcSubmitter;
        this.insurerConfigurationRepository = insurerConfigurationRepository;
        this.eventDispatcher = eventDispatcher;
        this.scheduler = scheduler;
        this.vitalityAgeServiceClient = vitalityAgeServiceClient;
    }

    @NonNull
    @Override
    public List<PresentableCapturedGroup> getCapturedGroups() {
        ArrayList<PresentableCapturedGroup> capturedGroups = new ArrayList<>();
        for (CapturedGroup capturedGroup : repository.getCapturedGroupsWithOneOrMoreCompletelyCapturedFields()) {
            PresentableCapturedGroup presentableCapturedGroup = new PresentableCapturedGroup(capturedGroup.getGroupDescription(), getCapturedFields(capturedGroup));
            sortCaptureFieldsIfBloodPressureGroup(capturedGroup, presentableCapturedGroup);
            capturedGroups.add(presentableCapturedGroup);
        }
        return capturedGroups;
    }

    private void sortCaptureFieldsIfBloodPressureGroup(CapturedGroup capturedGroup, PresentableCapturedGroup presentableCapturedGroup) {
        if (capturedGroup.getGroupType() == GroupType.BLOOD_PRESSURE) {
            Collections.sort(presentableCapturedGroup.capturedFields, new Comparator<PresentableCapturedField>() {
                @Override
                public int compare(PresentableCapturedField first, PresentableCapturedField second) {
                    return content.getSystolicBloodPressureString().equals(first.fieldName) ? -1 : 1;
                }
            });
        }
    }

    @NonNull
    @Override
    public PresentableProof getUploadedProof() {
        return new PresentableProof(content.getProofSectionTitle(), addProofRepository.getProofItems());
    }

    @Override
    public void onUserConfirmed() {
        hasShownRequestErrorMessage = false;
        if (insurerConfigurationRepository.shouldShowVHCPrivacyPolicy()) {
            userInterface.navigateAfterUserConfirmed();
        } else {
            userInterface.showLoadingIndicator();
            vhcSubmitter.submit();
        }
    }

    private List<PresentableCapturedField> getCapturedFields(CapturedGroup capturedGroup) {
        ArrayList<PresentableCapturedField> capturedFields = new ArrayList<>();
        for (CapturedField capturedField : repository.getCompletelyCapturedFields(capturedGroup)) {
            capturedFields.add(
                    new PresentableCapturedField(getValue(capturedField, capturedGroup),
                            content.getFieldName(Integer.valueOf(capturedField.getKey())),
                            getDateTested(capturedField)));
        }
        return capturedFields;
    }

    @NonNull
    private String getValue(CapturedField capturedField, CapturedGroup capturedGroup) {
        if (capturedField.getTypeOfCapture() == CapturedField.CaptureType.SELECTION) {
            return capturedField.getValueForDisplay();
        }

        if (capturedField.getSelectedUnitOfMeasure().getTypeKey().equals(UnitsOfMeasure.FOOTINCH.getTypeKey())) {
            return String.format(content.getFootInchString(),
                    ViewUtilities.roundToNoDecimal(capturedField.getPrimaryValue()),
                    ViewUtilities.roundToNoDecimal(capturedField.getSecondaryValue()));
        }

        if (capturedField.getSelectedUnitOfMeasure().getTypeKey().equals(UnitsOfMeasure.STONEPOUND.getTypeKey())) {
            return String.format(content.getStonePoundString(),
                    ViewUtilities.roundToNoDecimal(capturedField.getPrimaryValue()),
                    ViewUtilities.roundToNoDecimal(capturedField.getSecondaryValue()));
        }

        return String.format(content.getValueString(),
                capturedGroup.getGroupType() == GroupType.BLOOD_PRESSURE ? String.valueOf(capturedField.getPrimaryValue().intValue()) : ViewUtilities.roundToTwoDecimalsRemoveTrailingZeros(capturedField.getValueForDisplay()),
                content.getUnitOfMeasureSymbol(capturedField.getSelectedUnitOfMeasure()));
    }

    @NonNull
    private String getDateTested(CapturedField capturedField) {
        return String.format(content.getTestedOnString(),
                dateFormattingUtilities.formatWeekdayDateMonthYear(capturedField.getDateTested()));
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {

    }

    @Override
    public void onUserInterfaceAppeared() {
        if (insurerConfigurationRepository.shouldShowVHCPrivacyPolicy()) {
            return;
        }

        eventDispatcher.addEventListener(VHCSubmissionRequestCompletedEvent.class, submissionRequestCompletedEventListener);
        if (vhcSubmitter.isSubmitting()) {
            userInterface.showLoadingIndicator();
        } else if (shouldShowRequestErrorMessage()) {
            showRequestErrorMessage();
        } else {
            userInterface.hideLoadingIndicator();
        }
    }

    private boolean shouldShowRequestErrorMessage() {
        return !hasShownRequestErrorMessage && vhcSubmitter.getSubmissionRequestResult() != RequestResult.SUCCESSFUL;
    }

    private void showRequestErrorMessage() {
        hasShownRequestErrorMessage = true;
        if (vhcSubmitter.getSubmissionRequestResult() == RequestResult.CONNECTION_ERROR) {
            showConnectionErrorMessage();
        } else if (vhcSubmitter.getSubmissionRequestResult() == RequestResult.GENERIC_ERROR) {
            showGenericErrorMessage();
        }
    }

    private void showGenericErrorMessage() {
        userInterface.showGenericErrorMessage();
    }

    private void showConnectionErrorMessage() {
        userInterface.showConnectionErrorMessage();
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        if (insurerConfigurationRepository.shouldShowVHCPrivacyPolicy()) {
            return;
        }
        eventDispatcher.removeEventListener(VHCSubmissionRequestCompletedEvent.class, submissionRequestCompletedEventListener);
    }

    @Override
    public void onUserInterfaceDestroyed() {

    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }
}
