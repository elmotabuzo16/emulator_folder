package com.vitalityactive.va.snv.history.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cms.CMSContentDownloader;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by dharel.h.rosell on 12/10/2017.
 */

public class SNVHistoryDetailProofProvider implements CMSContentDownloader.Callback {

    private CMSContentDownloader cmsContentDownloader;
    private EventDispatcher eventDispatcher;
    private String fileNameToFetch = "";
    private Context context;


    @Inject
    public SNVHistoryDetailProofProvider(Context context, CMSContentDownloader cmsContentDownloader, EventDispatcher eventDispatcher) {
        this.cmsContentDownloader = cmsContentDownloader;
        this.eventDispatcher = eventDispatcher;
        this.context = context;
    }

    public void fetchUploadedProof(int referenceId) {
        cmsContentDownloader.fetchByReferenceId(referenceId, this);
    }

    public boolean isProfileImageAvailable(String fileName) {
        File profileImage = new File(getProfileImagePath(fileName));
        return profileImage.exists();
    }

    @NonNull
    public String getProfileImagePath(String fileName) {
       return context.getFilesDir() + File.separator + fileName;
    }

    @Override
    public void onFileDownloadSucceeded(String fileName) {
            eventDispatcher.dispatchEvent(new UploadedProofEventSuccess(fileName));
    }

    @Override
    public void onFileDownloadFailed(String fileName) {
            eventDispatcher.dispatchEvent(new UploadedProofEventFail(fileName));
    }

    @Override
    public void onConnectionError() {
        eventDispatcher.dispatchEvent(new UploadedProofEventFail(this.context.getString(R.string.download_failed_message)));
    }
}
