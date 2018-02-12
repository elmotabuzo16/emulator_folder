package com.vitalityactive.va.splashscreen;

import android.support.annotation.NonNull;

import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.appconfig.AppConfigServiceClient;
import com.vitalityactive.va.cms.CMSContentDownloader;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.AppConfigResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AppConfigDataUpdaterImpl implements AppConfigDataUpdater {

    private AppConfigServiceClient appConfigServiceClient;
    private AppConfigRepository appConfigRepository;
    private CMSContentDownloader contentDownloader;
    private EventDispatcher eventDispatcher;
    private AtomicBoolean filesDownloaded = new AtomicBoolean();

    public AppConfigDataUpdaterImpl(AppConfigServiceClient appConfigServiceClient, AppConfigRepository appConfigRepository, CMSContentDownloader contentDownloader, EventDispatcher eventDispatcher) {
        this.appConfigServiceClient = appConfigServiceClient;
        this.appConfigRepository = appConfigRepository;
        this.contentDownloader = contentDownloader;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void updateAppConfig() {
        appConfigServiceClient.updateAppConfig(new WebServiceResponseParser<AppConfigResponse>() {
            @Override
            public void parseResponse(AppConfigResponse response) {
                if (response.application == null) {
                    handleEmptyAppConfigResponse();
                } else if (appConfigRepository.persistAppConfig(response.application)) {
                    handleAppConfigChangedResponse();
                } else {
                    handleFailedResponse(RequestResult.REDIRECT);
                }
            }

            @Override
            public void parseErrorResponse(String errorBody, int code) {
                handleFailedResponse(RequestResult.GENERIC_ERROR);
            }

            @Override
            public void handleGenericError(Exception exception) {
                handleFailedResponse(RequestResult.GENERIC_ERROR);
            }

            @Override
            public void handleConnectionError() {
                handleFailedResponse(RequestResult.CONNECTION_ERROR);
            }

            private void handleAppConfigChangedResponse() {
                fetchLogo();
            }

            private void handleFailedResponse(RequestResult errorType) {
                eventDispatcher.dispatchEvent(new AppConfigDataUpdateFailedEvent(errorType));
            }
        });
    }

    @Override
    public void handleEmptyAppConfigResponse() {
        filesDownloaded.set(contentDownloader.logoExists());
        if (filesDownloaded.get()) {
            dispatchAppConfigDataUpdatedEvent();
        } else {
            fetchLogo();
        }
    }

    private void fetchMissingFiles(List<String> fileNamesOfResourceFilesThatDontExist) {
        ArrayList<String> filesToDownload = new ArrayList<>();
        if (!contentDownloader.logoExists()) {
            filesToDownload.add(contentDownloader.getSizedLogoFilename());
        }
        if (!fileNamesOfResourceFilesThatDontExist.isEmpty()) {
            filesToDownload.addAll(fileNamesOfResourceFilesThatDontExist);
        }
        fetchFiles(filesToDownload);
    }

    @Override
    public void fetchLogo() {
        ArrayList<String> fileNames = new ArrayList<>();
        fileNames.add(contentDownloader.getSizedLogoFilename());
        fetchFiles(fileNames);
    }

    private void fetchFiles(List<String> fileNames) {
        contentDownloader.fetchFiles(fileNames, getLiferayGroupId(), getCallback());
    }

    @NonNull
    private CMSContentDownloader.MultiFileCallback getCallback() {
        return new CMSContentDownloader.MultiFileCallback() {
            @Override
            public void onFilesDownloaded() {
                filesDownloaded.set(true);
                dispatchAppConfigDataUpdatedEvent();
            }
        };
    }

    @Override
    public boolean didUpdateAppConfigData() {
        return filesDownloaded.get();
    }

    private void dispatchAppConfigDataUpdatedEvent() {
        eventDispatcher.dispatchEvent(new AppConfigDataUpdatedEvent());
    }

    private List<String> getFileNamesOfResourceFilesThatAreMissing() {
        return appConfigRepository.getFileNamesOfResourceFilesThatAreMissing();
    }

    private String getLiferayGroupId() {
        return appConfigRepository.getLiferayGroupId();
    }
}
