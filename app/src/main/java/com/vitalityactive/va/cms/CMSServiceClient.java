package com.vitalityactive.va.cms;

import com.vitalityactive.va.ExecutorServiceScheduler;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.FileUploadServiceResponse;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.vhc.addproof.ProofImageFetcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;

@Singleton
public class CMSServiceClient {
    private WebServiceClient webServiceClient;
    private CMSService cmsService;
    private final AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;
    private HashMap<String, String> contentBeingFetched = new HashMap<>();
    private HashMap<String, String> filesBeingFetched = new HashMap<>();
    private HashMap<String, String> filesBeingUploaded = new HashMap<>();
    private PartyInformationRepository partyInformationRepository;
    private ProofImageFetcher proofImageFetcher;
    private ExecutorServiceScheduler scheduler;

    @Inject
    public CMSServiceClient(WebServiceClient webServiceClient,
                            CMSService cmsService,
                            EventDispatcher eventDispatcher,
                            AccessTokenAuthorizationProvider accessTokenAuthorizationProvider,
                            PartyInformationRepository partyInformationRepository, ProofImageFetcher proofImageFetcher, ExecutorServiceScheduler scheduler) {
        this.webServiceClient = webServiceClient;
        this.cmsService = cmsService;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
        this.partyInformationRepository = partyInformationRepository;
        this.proofImageFetcher = proofImageFetcher;
        this.scheduler = scheduler;
        eventDispatcher.addEventListener(WebServiceClient.RequestSucceededEvent.class, new EventListener<WebServiceClient.RequestSucceededEvent>() {
            @Override
            public void onEvent(WebServiceClient.RequestSucceededEvent event) {
                stopRequest(event.getRequestDescription());
            }
        });
        eventDispatcher.addEventListener(WebServiceClient.RequestFailedEvent.class, new EventListener<WebServiceClient.RequestFailedEvent>() {
            @Override
            public void onEvent(WebServiceClient.RequestFailedEvent event) {
                stopRequest(event.getRequestDescription());
            }
        });
        eventDispatcher.addEventListener(WebServiceClient.RequestCancelledEvent.class, new EventListener<WebServiceClient.RequestCancelledEvent>() {
            @Override
            public void onEvent(WebServiceClient.RequestCancelledEvent event) {
                stopRequest(event.getRequestDescription());
            }
        });
    }

    private void stopRequest(String requestDescription) {
        stopFetchingContent(requestDescription);
        stopFetchingFile(requestDescription);
        stopUploadingFile(requestDescription);
    }

    private synchronized void stopUploadingFile(String requestIdentifier) {
        for (String fileId : filesBeingUploaded.keySet()) {
            if (getFileUploadRequest(fileId).equals(requestIdentifier)) {
                filesBeingUploaded.remove(fileId);
                break;
            }
        }
    }

    public void fetchContentWithId(String groupId, String contentId, WebServiceResponseParser<String> parser) {
        Call<String> contentRequest = cmsService.getContentRequest(groupId, contentId, accessTokenAuthorizationProvider.getAuthorization());
        if (beginFetchingContent(contentId, contentRequest.request().toString())) {
            webServiceClient.executeAsynchronousRequest(contentRequest, parser);
        }
    }

    public void fetchPublicFile(String groupId, String fileName, WebServiceResponseParser<ResponseBody> parser) {
        Call<ResponseBody> fileRequest = cmsService.getFileRequest(fileName, groupId, accessTokenAuthorizationProvider.getAuthorization());
        if (beginFetchingFile(fileName, fileRequest.request().toString())) {
            webServiceClient.executeAsynchronousRequest(fileRequest, parser);
        }
    }

    private synchronized boolean beginFetchingFile(String fileName, String requestIdentifier) {
        if (isFetchingFileWithName(fileName)) {
            return false;
        }
        filesBeingFetched.put(fileName, requestIdentifier);
        return true;
    }

    public boolean isFetchingFileWithName(String fileName) {
        return webServiceClient.isRequestInProgress(getFileDownloadRequest(fileName));
    }

    private synchronized String getFileDownloadRequest(String fileName) {
        return filesBeingFetched.get(fileName);
    }

    private synchronized boolean beginFetchingContent(String contentId, String requestIdentifier) {
        if (isFetchingContentWithId(contentId)) {
            return false;
        }
        contentBeingFetched.put(contentId, requestIdentifier);
        return true;
    }

    private synchronized void stopFetchingFile(String requestIdentifier) {
        for (String fileName : filesBeingFetched.keySet()) {
            if (getFileDownloadRequest(fileName).equals(requestIdentifier)) {
                filesBeingFetched.remove(fileName);
                break;
            }
        }
    }

    private synchronized void stopFetchingContent(String requestIdentifier) {
        for (String contentId : contentBeingFetched.keySet()) {
            if (contentBeingFetched.get(contentId).equals(requestIdentifier)) {
                contentBeingFetched.remove(contentId);
                break;
            }
        }
    }

    public boolean isFetchingContentWithId(String contentId) {
        return webServiceClient.isRequestInProgress(contentBeingFetched.get(contentId));
    }

    public <FileType extends UploadableFile> void uploadFile(FileType file, String remoteFileName, byte[] fileAsByte, WebServiceResponseParser<FileUploadServiceResponse> parser) {

        if(TextUtilities.isNullOrEmpty(remoteFileName)){
            remoteFileName = UUID.randomUUID().toString() + ".jpg";
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileAsByte );
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", remoteFileName, requestBody);

        Call<FileUploadServiceResponse> uploadRequest = cmsService.getFileUploadRequest(remoteFileName, String.valueOf(partyInformationRepository.getPartyId()), accessTokenAuthorizationProvider.getAuthorization(), body);

        if (beginUploadingFile(file.getId(), uploadRequest.request().toString())) {
            webServiceClient.executeAsynchronousRequest(uploadRequest, parser);
        }
    }

    private synchronized boolean beginUploadingFile(String fileId, String requestIdentifier) {
        if (isUploadingFileWithId(fileId)) {
            return false;
        }
        filesBeingUploaded.put(fileId, requestIdentifier);
        return true;
    }

    private boolean isUploadingFileWithId(String fileId) {
        return webServiceClient.isRequestInProgress(getFileUploadRequest(fileId));
    }

    private synchronized String getFileUploadRequest(String fileId) {
        return filesBeingUploaded.get(fileId);
    }

    public <FileType extends UploadableFile> void uploadFiles(final List<FileType> files, final FileUploadResponseParser<FileUploadServiceResponse, FileType> parser, final ExecutorServiceScheduler.Callback callback) {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                List<Runnable> runnables = new ArrayList<>();
                for (final FileType file : files) {
                    String remoteFileName = UUID.randomUUID().toString() + ".jpg";

                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), proofImageFetcher.getImageFileAsByteArray(file.getUri()));
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file", remoteFileName, requestBody);

                    final Call<FileUploadServiceResponse> uploadRequest = cmsService.getFileUploadRequest(remoteFileName, String.valueOf(partyInformationRepository.getPartyId()), accessTokenAuthorizationProvider.getAuthorization(), body);

                    if (beginUploadingFile(file.getId(), uploadRequest.request().toString())) {
                        runnables.add(new Runnable() {
                            @Override
                            public void run() {
                                webServiceClient.executeSynchronousRequest(uploadRequest, new WebServiceResponseParser<FileUploadServiceResponse>() {
                                    @Override
                                    public void parseResponse(FileUploadServiceResponse body) {
                                        parser.parseResponse(body, file);
                                    }

                                    @Override
                                    public void parseErrorResponse(String errorBody, int code) {
                                        parser.parseErrorResponse(errorBody, code, file);
                                    }

                                    @Override
                                    public void handleGenericError(Exception exception) {
                                        parser.handleGenericError(exception, file);
                                    }

                                    @Override
                                    public void handleConnectionError() {
                                        parser.handleConnectionError(file);
                                    }
                                });
                            }
                        });
                    }
                }
                scheduler.schedule(runnables, callback);
            }
        });
    }

    public synchronized boolean isUploadingFile() {
        return !filesBeingUploaded.isEmpty();
    }

    public void fetchFiles(final List<String> fileNames, final String liferayGroupId, final FileUploadResponseParser<ResponseBody, String> parser, final ExecutorServiceScheduler.Callback callback) {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                List<Runnable> runnables = new ArrayList<>();
                for (final String fileName : fileNames) {
                    final Call<ResponseBody> fileRequest = cmsService.getFileRequest(fileName, liferayGroupId, accessTokenAuthorizationProvider.getAuthorization());
                    if (beginFetchingFile(fileName, fileRequest.request().toString())) {
                        runnables.add(new Runnable() {
                            @Override
                            public void run() {
                                webServiceClient.executeSynchronousRequest(fileRequest, new WebServiceResponseParser<ResponseBody>() {
                                    @Override
                                    public void parseResponse(ResponseBody body) {
                                        parser.parseResponse(body, fileName);
                                    }

                                    @Override
                                    public void parseErrorResponse(String errorBody, int code) {
                                        parser.parseErrorResponse(errorBody, code, fileName);
                                    }

                                    @Override
                                    public void handleGenericError(Exception exception) {
                                        parser.handleGenericError(exception, fileName);
                                    }

                                    @Override
                                    public void handleConnectionError() {
                                        parser.handleConnectionError(fileName);
                                    }
                                });
                            }
                        });
                    }
                }
                scheduler.schedule(runnables, callback);
            }
        });
    }

    public void fetchUserFile(String fileName, WebServiceResponseParser<ResponseBody> parser) {
        Call<ResponseBody> fileRequest = cmsService.getFileRequest(fileName, partyInformationRepository.getPartyId(), accessTokenAuthorizationProvider.getAuthorization());
        if (beginFetchingFile(fileName, fileRequest.request().toString())) {
            webServiceClient.executeAsynchronousRequest(fileRequest, parser);
        }
    }

    public void fetchFileByReferenceId(int referenceId, WebServiceResponseParser<ResponseBody> parser) {
        Call<ResponseBody> fileRequest = cmsService.getFileByReferenceIdRequest(referenceId, accessTokenAuthorizationProvider.getAuthorization(),
                "application/octet-stream");
        if(beginFetchingFile(Integer.toString(referenceId), fileRequest.request().toString())) {
                    webServiceClient.executeAsynchronousRequest(fileRequest, parser);
        }
    }

    public interface UploadableFile {
        String getUri();

        String getId();
    }

    public interface FileUploadResponseParser<ResponseType, FileType> {
        void parseResponse(ResponseType response, FileType file);

        void parseErrorResponse(String errorBody, int code, FileType file);

        void handleGenericError(Exception exception, FileType file);

        void handleConnectionError(FileType file);
    }
}
