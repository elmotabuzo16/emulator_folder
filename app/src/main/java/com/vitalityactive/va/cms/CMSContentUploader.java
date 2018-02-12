package com.vitalityactive.va.cms;

import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.FileUploadServiceResponse;

public class CMSContentUploader {

    private final CMSServiceClient serviceClient;

    public CMSContentUploader(CMSServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    public void uploadProfilePhoto(CMSServiceClient.UploadableFile file, byte[] fileAsByte){
        String remoteFileName = "profile.jpg";
        serviceClient.uploadFile(file, remoteFileName, fileAsByte, new WebServiceResponseParser<FileUploadServiceResponse>() {
            @Override
            public void parseResponse(FileUploadServiceResponse response) {

            }

            @Override
            public void parseErrorResponse(String errorBody, int code) {

            }

            @Override
            public void handleGenericError(Exception exception) {

            }

            @Override
            public void handleConnectionError() {

            }
        });
    }
}
