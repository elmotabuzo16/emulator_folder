package com.vitalityactive.va.vhc;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.cms.CMSContentDownloader;

import java.io.File;

public class PDFPresenterImpl implements PDFPresenter, CMSContentDownloader.Callback {
    private final CMSContentDownloader contentDownloader;
    private final AppConfigRepository appConfigRepository;
    private final Scheduler scheduler;
    private UserInterface userInterface;
    @NonNull
    private String fileName = "";

    public PDFPresenterImpl(CMSContentDownloader contentDownloader, AppConfigRepository appConfigRepository, Scheduler scheduler) {
        this.contentDownloader = contentDownloader;
        this.appConfigRepository = appConfigRepository;
        this.scheduler = scheduler;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        if (fileExists()) {
            handlePDFDownloadSuccessful();
        } else {
            fetchPDF();
        }
    }

    private boolean fileExists() {
        return getFileToShow().exists();
    }

    @NonNull
    private File getFileToShow() {
        return appConfigRepository.getFile(fileName);
    }

    @Override
    public void setFileName(@NonNull String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void fetchPDF() {
        userInterface.showLoadingIndicator();

        contentDownloader.fetchPublicFile(appConfigRepository.getLiferayGroupId(), fileName, this);
    }

    @Override
    public void onFileDownloadSucceeded(final String fileName) {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                handlePDFDownloadSuccessful();
            }
        });
    }

    @Override
    public void onFileDownloadFailed(String fileName) {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                handlePDFDownloadFailed();
            }
        });
    }

    @Override
    public void onConnectionError() {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                handleConnectionError();
            }
        });
    }

    @VisibleForTesting
    void handleConnectionError() {
        userInterface.hideLoadingIndicator();
        userInterface.displayConnectionError();
    }

    @VisibleForTesting
    void handlePDFDownloadFailed() {
        userInterface.hideLoadingIndicator();
        userInterface.displayGenericError();
    }

    @VisibleForTesting
    void handlePDFDownloadSuccessful() {
        userInterface.hideLoadingIndicator();
        File fileToShow = getFileToShow();
        userInterface.displayFileInPDFView(fileToShow);
        userInterface.createShareIntent(fileToShow);
    }

    @Override
    public void onUserInterfaceAppeared() {

    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {

    }

    @Override
    public void onUserInterfaceDestroyed() {

    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

}
