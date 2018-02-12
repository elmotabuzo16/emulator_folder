package com.vitalityactive.va.snv.confirmandsubmit.service;

import com.vitalityactive.va.EventServiceClient;
import com.vitalityactive.va.ExecutorServiceScheduler;
import com.vitalityactive.va.ProcessEventsServiceResponse;
import com.vitalityactive.va.cms.CMSServiceClient;
import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.FileUploadServiceResponse;
import com.vitalityactive.va.snv.confirmandsubmit.repository.SNVItemRepository;

import java.util.List;

public class SNVSubmitterImpl implements SNVSubmitter, WebServiceResponseParser<ProcessEventsServiceResponse> {
    private RequestResult submissionRequestResult = RequestResult.NONE;
    private EventServiceClient eventServiceClient;
    private EventDispatcher eventDispatcher;
    private CMSServiceClient cmsServiceClient;
    private SNVItemRepository repository;
    private State apiState;
    private volatile int proofUploadedCounter;
    private int totalProofsToUpload;

    private enum State{
        DEFAULT,
        UPLOAD_PROOFS_IN_PROGRESS,
        UPLOAD_PROOFS_COMPLETE,
        SNV_SUBMISSION_IN_PROGRESS,
        SNV_SUBMISSION_COMPLETE
    };

    public SNVSubmitterImpl(EventServiceClient eventServiceClient,
                            EventDispatcher eventDispatcher, CMSServiceClient cmsServiceClient, SNVItemRepository repository) {
        this.eventServiceClient = eventServiceClient;
        this.eventDispatcher = eventDispatcher;
        this.cmsServiceClient = cmsServiceClient;
        this.repository = repository;
    }

    @Override
    public void submit() {

        if (isSubmitting()) {
            return;
        }
        List<ProofItemDTO> proofItemsThatHaveNotBeenSubmitted = repository.getProofItemsThatHaveNotBeenSubmitted();
        totalProofsToUpload = proofItemsThatHaveNotBeenSubmitted.size();

        if (haveAllProofItemsBeenSubmitted(proofItemsThatHaveNotBeenSubmitted)) {
            submitSNV();
        } else {
            apiState = State.UPLOAD_PROOFS_IN_PROGRESS;
            cmsServiceClient.uploadFiles(proofItemsThatHaveNotBeenSubmitted, new CMSServiceClient.FileUploadResponseParser<FileUploadServiceResponse, ProofItemDTO>() {
                @Override
                public void parseResponse(FileUploadServiceResponse response, ProofItemDTO proofItem) {
                    repository.setProofItemReferenceId(proofItem, response.body.referenceId);
                    proceedIfUploadIsComplete();
                }

                @Override
                public void parseErrorResponse(String errorBody, int code, ProofItemDTO proofItem) {
                    proceedIfUploadIsComplete();
                }

                @Override
                public void handleGenericError(Exception exception, ProofItemDTO proofItem) {
                    proceedIfUploadIsComplete();
                }

                @Override
                public void handleConnectionError(ProofItemDTO proofItem) {
                    proceedIfUploadIsComplete();
                }
            }, new ExecutorServiceScheduler.Callback() {
                @Override
                public void onSchedulingCompleted() {
                    if (haveAllProofItemsBeenSubmitted()) {
                        apiState = State.UPLOAD_PROOFS_COMPLETE;
                        submitSNV();
                    } else {
                        onRequestCompleted(RequestResult.GENERIC_ERROR);
                    }
                }
            });
        }
    }

    private void proceedIfUploadIsComplete(){
        proofUploadedCounter++;
        if(proofUploadedCounter == totalProofsToUpload){
            proofUploadedCounter = 0;
            submitSNV();
        }
    }

    private boolean haveAllProofItemsBeenSubmitted(List<ProofItemDTO> proofItemUrisThatHaveNotBeenSubmitted) {
        return proofItemUrisThatHaveNotBeenSubmitted.isEmpty();
    }

    private boolean haveAllProofItemsBeenSubmitted() {
        return haveAllProofItemsBeenSubmitted(repository.getProofItemsThatHaveNotBeenSubmitted());
    }

    private void submitSNV() {
        apiState = State.SNV_SUBMISSION_IN_PROGRESS;
        eventServiceClient.submitSNV(repository.getScreeningItems(), repository.getVaccinationItems(), repository.getProofItems(), SNVSubmitterImpl.this);
    }

    @Override
    public boolean isSubmitting() {
        return cmsServiceClient.isUploadingFile() || eventServiceClient.isSubmittingSNV();
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
        apiState = State.SNV_SUBMISSION_COMPLETE;
        onRequestCompleted(RequestResult.SUCCESSFUL);
    }

    private void removeAllCachedSubmissionData() {
        repository.clearAllItems();
        repository.removeAllProofItems();
    }

    private void onRequestCompleted(RequestResult requestResult) {
        setSubmissionRequestResult(requestResult);
        eventDispatcher.dispatchEvent(new SNVSubmissionRequestCompletedEvent());
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
