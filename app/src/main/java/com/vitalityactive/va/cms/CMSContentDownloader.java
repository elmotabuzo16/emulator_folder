package com.vitalityactive.va.cms;

import android.content.Context;
import android.support.annotation.NonNull;

import com.vitalityactive.va.ExecutorServiceScheduler;
import com.vitalityactive.va.networking.WebServiceResponseHeaderHelper;
import com.vitalityactive.va.networking.WebServiceResponseParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class CMSContentDownloader {
    public static final String FILE_NAME_LOGO = "logo.png";
    private static final String TAG = "CMSContentDownloader";
    private final CMSServiceClient serviceClient;
    private Context context;
    private String sizedLogoFilename = FILE_NAME_LOGO;

    public CMSContentDownloader(CMSServiceClient serviceClient,
                                Context context) {
        this.serviceClient = serviceClient;
        this.context = context;
    }

    public String getSizedLogoFilename() {
        return sizedLogoFilename;
    }

    public void setSizedLogoFilename(String sizedLogoFilename) {
        this.sizedLogoFilename = sizedLogoFilename;
    }

//    public void fetchFiles(List<String> fileNames, String liferayGroupId) {
//        serviceClient.fetchFiles(fileNames, liferayGroupId, new CMSServiceClient.FileUploadResponseParser<ResponseBody, String>() {
//            @Override
//            public void parseResponse(ResponseBody response, String fileName) {
//                writeResponseBodyToDisk(response, fileName);
//            }
//
//            @Override
//            public void parseErrorResponse(String errorBody, int code, String file) {
//            }
//
//            @Override
//            public void handleGenericError(Exception exception, String file) {
//            }
//
//            @Override
//            public void handleConnectionError(String file) {
//            }
//        }, new ExecutorServiceScheduler.Callback() {
//            @Override
//            public void onSchedulingCompleted() {
//
//            }
//        });
//    }

    public void fetchFiles(List<String> fileNames, String liferayGroupId, final MultiFileCallback callback) {
        serviceClient.fetchFiles(fileNames, liferayGroupId, new CMSServiceClient.FileUploadResponseParser<ResponseBody, String>() {
            @Override
            public void parseResponse(ResponseBody response, String fileName) {
                String localFileName = checkNameAndFixLogo(fileName);
                writeResponseBodyToDisk(response, localFileName);
            }

            @Override
            public void parseErrorResponse(String errorBody, int code, String file) {
            }

            @Override
            public void handleGenericError(Exception exception, String file) {
            }

            @Override
            public void handleConnectionError(String file) {
            }
        }, new ExecutorServiceScheduler.Callback() {
            @Override
            public void onSchedulingCompleted() {
                callback.onFilesDownloaded();
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String fileName) {
        File file = getFile(fileName);

        byte[] fileReader = new byte[4096];

        long fileSize = body.contentLength();
        long fileSizeDownloaded = 0;

        try (InputStream inputStream = body.byteStream(); OutputStream outputStream = new FileOutputStream(file)) {
            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
                fileSizeDownloaded += read;
            }
            outputStream.flush();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @NonNull
    private File getFile(String fileName) {
        return new File(context.getFilesDir() + File.separator + fileName);
    }

//    public void getLogo(String liferayGroupId) {
//        serviceClient.fetchFile(liferayGroupId, FILE_NAME_LOGO, new WebServiceResponseParser<ResponseBody>() {
//            @Override
//            public void parseResponse(ResponseBody body) {
//                if (writeResponseBodyToDisk(body, FILE_NAME_LOGO)) {
//                    eventDispatcher.dispatchEvent(new InsurerLogoDownloadSucceededEvent());
//                } else {
//                    eventDispatcher.dispatchEvent(new InsurerLogoDownloadFailedEvent());
//                }
//            }
//
//            @Override
//            public void parseErrorResponse(String errorBody, int code) {
//
//            }
//
//            @Override
//            public void handleGenericError(Exception exception) {
//
//            }
//
//            @Override
//            public void handleConnectionError() {
//
//            }
//        });
//    }

    public boolean logoExists() {
        return getLogoFile().exists();
    }

    @NonNull
    private File getLogoFile() {
        return getFile(FILE_NAME_LOGO);
    }

    public String getLogoPath() {
        return getLogoFile().getAbsolutePath();
    }

    public void getLogo(String liferayGroupId, final Callback callback) {
        fetchPublicFile(liferayGroupId, getSizedLogoFilename(), callback);
    }

    public void fetchPublicFile(String liferayGroupId, final String fileName, final Callback callback) {
        serviceClient.fetchPublicFile(liferayGroupId, fileName, new WebServiceResponseParser<ResponseBody>() {
            @Override
            public void parseResponse(ResponseBody body) {
                writeResponseBodyToDiskAndNotifyCallback(body, fileName, callback);
            }

            @Override
            public void parseErrorResponse(String errorBody, int code) {
                callback.onFileDownloadFailed(fileName);
            }

            @Override
            public void handleGenericError(Exception exception) {
                callback.onFileDownloadFailed(fileName);
            }

            @Override
            public void handleConnectionError() {
                callback.onConnectionError();
            }
        });
    }

    private void writeResponseBodyToDiskAndNotifyCallback(ResponseBody body, String fileName, Callback callback) {
        if (writeResponseBodyToDisk(body, fileName)) {
            callback.onFileDownloadSucceeded(fileName);
        } else {
            callback.onFileDownloadFailed(fileName);
        }
    }

    public void fetchUserFile(final String fileName, final Callback callback) {
        serviceClient.fetchUserFile(fileName, new WebServiceResponseParser<ResponseBody>() {
            @Override
            public void parseResponse(ResponseBody body) {
                writeResponseBodyToDiskAndNotifyCallback(body, fileName, callback);
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

    public void fetchByReferenceId(int referenceId, final Callback callback){
        serviceClient.fetchFileByReferenceId(referenceId, new WebServiceResponseParser<ResponseBody>() {
            @Override
            public void parseResponse(ResponseBody responseBody) {
                String contentDisposition = WebServiceResponseHeaderHelper.headers.get("Content-Disposition");
                String fileName = contentDisposition.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
                writeResponseBodyToDiskAndNotifyCallback(responseBody, fileName, callback);
            }

            @Override
            public void parseErrorResponse(String errorBody, int code) {
                callback.onFileDownloadFailed("Something went wrong when dowloading the file");
            }

            @Override
            public void handleGenericError(Exception exception) {
                callback.onFileDownloadFailed("Something went wrong when dowloading the file");
            }

            @Override
            public void handleConnectionError() {
                callback.onConnectionError();
            }
        });
    }

    public interface Callback {
        void onFileDownloadSucceeded(String fileName);

        void onFileDownloadFailed(String fileName);

        void onConnectionError();
    }

    public interface MultiFileCallback {
        void onFilesDownloaded();
    }

    private String checkNameAndFixLogo(String filename) {
        if (filename.startsWith("logo") && filename.contains(".png")) {
            return CMSContentDownloader.FILE_NAME_LOGO;
        } else {
            return filename;
        }
    }


}
