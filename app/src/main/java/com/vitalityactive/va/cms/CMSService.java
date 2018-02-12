package com.vitalityactive.va.cms;

import com.vitalityactive.va.networking.model.FileUploadServiceResponse;

import okhttp3.MultipartBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface CMSService {

    String ENDPOINT = "liferay-content-management/1.0/";

    @Headers({"Content-Type: application/json"})
    @GET(ENDPOINT + "content-service/get-article-by-url-title/groupId/{groupId}/urlTitle/{urlTitle}")
    Call<String> getContentRequest(@Path("groupId") String groupId, @Path("urlTitle") String contentId, @Header("Authorization") String authorization);

    @Streaming
    @GET(ENDPOINT + "document-service/get-file/{fileName}/groupId/{groupId}")
    Call<ResponseBody> getFileRequest(@Path("fileName") String fileName, @Path("groupId") String groupId, @Header("Authorization") String authorization);

    @Streaming
    @GET(ENDPOINT + "document-service/get-file/{fileName}/partyId/{partyId}")
    Call<ResponseBody> getFileRequest(@Path("fileName") String fileName, @Path("partyId") long partyId, @Header("Authorization") String authorization);

    @Multipart
    @POST(ENDPOINT + "document-service/upload-file/{fileName}/partyId/{partyId}")
    Call<FileUploadServiceResponse> getFileUploadRequest(@Path("fileName") String fileName,
                                                         @Path("partyId") String partyId,
                                                         @Header("Authorization") String authorization,
                                                         @Part MultipartBody.Part file);

    @Streaming
    @GET(ENDPOINT + "document-service/get-file/referenceId/{referenceId}")
    Call<ResponseBody> getFileByReferenceIdRequest(@Path("referenceId") int referenceId,
                                      @Header("Authorization") String authorization,
                                      @Header("Content-Type") String contentType);

}
