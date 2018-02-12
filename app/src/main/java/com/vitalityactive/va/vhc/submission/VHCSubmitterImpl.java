package com.vitalityactive.va.vhc.submission;

import com.vitalityactive.va.EventServiceClient;
import com.vitalityactive.va.ExecutorServiceScheduler;
import com.vitalityactive.va.ProcessEventsServiceResponse;
import com.vitalityactive.va.cms.CMSServiceClient;
import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.FileUploadServiceResponse;
import com.vitalityactive.va.vhc.addproof.VHCProofItemRepository;
import com.vitalityactive.va.vhc.captureresults.models.CapturedField;
import com.vitalityactive.va.vhc.captureresults.models.CapturedGroup;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepository;

import java.util.ArrayList;
import java.util.List;

public class VHCSubmitterImpl implements VHCSubmitter, WebServiceResponseParser<ProcessEventsServiceResponse> {
    private RequestResult submissionRequestResult = RequestResult.NONE;
    private EventServiceClient eventServiceClient;
    private HealthAttributeRepository healthAttributeRepository;
    private EventDispatcher eventDispatcher;
    private CMSServiceClient cmsServiceClient;
    private VHCProofItemRepository proofItemRepository;

    public VHCSubmitterImpl(EventServiceClient eventServiceClient,
                            HealthAttributeRepository healthAttributeRepository,
                            EventDispatcher eventDispatcher, CMSServiceClient cmsServiceClient, VHCProofItemRepository proofItemRepository) {
        this.eventServiceClient = eventServiceClient;
        this.healthAttributeRepository = healthAttributeRepository;
        this.eventDispatcher = eventDispatcher;
        this.cmsServiceClient = cmsServiceClient;
        this.proofItemRepository = proofItemRepository;
    }

    @Override
    public void submit() {
        if (isSubmitting()) {
            return;
        }
        List<ProofItemDTO> proofItemsThatHaveNotBeenSubmitted = proofItemRepository.getProofItemsThatHaveNotBeenSubmitted();
        if (haveAllProofItemsBeenSubmitted(proofItemsThatHaveNotBeenSubmitted)) {
            submitVHC();
        } else {
            cmsServiceClient.uploadFiles(proofItemsThatHaveNotBeenSubmitted, new CMSServiceClient.FileUploadResponseParser<FileUploadServiceResponse, ProofItemDTO>() {
                @Override
                public void parseResponse(FileUploadServiceResponse response, ProofItemDTO proofItem) {
                    proofItemRepository.setProofItemReferenceId(proofItem, response.body.referenceId);
                }

                @Override
                public void parseErrorResponse(String errorBody, int code, ProofItemDTO proofItem) {
                    // We don't do anything when individual proof items fail to upload
                }

                @Override
                public void handleGenericError(Exception exception, ProofItemDTO proofItem) {
                    // We don't do anything when individual proof items fail to upload
                }

                @Override
                public void handleConnectionError(ProofItemDTO proofItem) {
                    // We don't do anything when individual proof items fail to upload
                }
            }, new ExecutorServiceScheduler.Callback() {
                @Override
                public void onSchedulingCompleted() {
                    if (haveAllProofItemsBeenSubmitted()) {
                        submitVHC();
                    } else {
                        onRequestCompleted(RequestResult.GENERIC_ERROR);
                    }
                }
            });
        }
    }

    private boolean haveAllProofItemsBeenSubmitted(List<ProofItemDTO> proofItemUrisThatHaveNotBeenSubmitted) {
        return proofItemUrisThatHaveNotBeenSubmitted.isEmpty();
    }

    private boolean haveAllProofItemsBeenSubmitted() {
        return haveAllProofItemsBeenSubmitted(proofItemRepository.getProofItemsThatHaveNotBeenSubmitted());
    }

    private void submitVHC() {
        eventServiceClient.submitVHC(getCapturedFields(), proofItemRepository.getProofItems(), VHCSubmitterImpl.this);
    }

    private List<CapturedField> getCapturedFields() {
        List<CapturedGroup> capturedGroups = healthAttributeRepository.getCapturedGroupsWithOneOrMoreCompletelyCapturedFields();
        ArrayList<CapturedField> capturedFields = new ArrayList<>();
        for (CapturedGroup capturedGroup : capturedGroups) {
            capturedFields.addAll(healthAttributeRepository.getCompletelyCapturedFields(capturedGroup));
        }
        return capturedFields;
    }

    @Override
    public boolean isSubmitting() {
        return cmsServiceClient.isUploadingFile() || eventServiceClient.isSubmittingVHC();
    }

    @Override
    public synchronized RequestResult getSubmissionRequestResult() {
        return submissionRequestResult;
    }

    private synchronized void setSubmissionRequestResult(RequestResult submissionRequestResult) {
        this.submissionRequestResult = submissionRequestResult;
    }

    @Override
    public void parseResponse(ProcessEventsServiceResponse body) {
        removeAllCachedSubmissionData();
        onRequestCompleted(RequestResult.SUCCESSFUL);
    }

    private void removeAllCachedSubmissionData() {
        healthAttributeRepository.removeAllCapturedGroups();
        proofItemRepository.removeAllProofItems();
    }

    private void onRequestCompleted(RequestResult requestResult) {
        setSubmissionRequestResult(requestResult);
        eventDispatcher.dispatchEvent(new VHCSubmissionRequestCompletedEvent());
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        onRequestCompleted(RequestResult.GENERIC_ERROR);
    }

    @Override
    public void handleGenericError(Exception exception) {
        onRequestCompleted(RequestResult.GENERIC_ERROR);
    }

    @Override
    public void handleConnectionError() {
        onRequestCompleted(RequestResult.CONNECTION_ERROR);
    }
}
