package com.vitalityactive.va.vhc;

import android.support.annotation.NonNull;

import com.vitalityactive.va.Presenter;

import java.io.File;

public interface PDFPresenter extends Presenter<PDFPresenter.UserInterface> {

    void fetchPDF();
    void setFileName(@NonNull String fileName);

    interface UserInterface extends Presenter.UserInterface {
        void displayFileInPDFView(File file);
        void displayGenericError();
        void displayConnectionError();
        void showPopulatedState();
        void createShareIntent(File healthCareBenefitFilePath);
    }
}
