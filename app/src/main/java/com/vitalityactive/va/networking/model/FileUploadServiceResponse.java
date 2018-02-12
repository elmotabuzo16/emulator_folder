package com.vitalityactive.va.networking.model;

import com.google.gson.annotations.SerializedName;

public class FileUploadServiceResponse {
    @SerializedName("success")
    public FileUploadServiceResponseBody body;

    public static class FileUploadServiceResponseBody {
        @SerializedName("referenceId")
        public String referenceId;
    }
}
