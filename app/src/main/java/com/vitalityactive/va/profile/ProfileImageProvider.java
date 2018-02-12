package com.vitalityactive.va.profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.vitalityactive.va.cms.CMSContentDownloader;
import com.vitalityactive.va.events.EventDispatcher;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProfileImageProvider implements CMSContentDownloader.Callback {

    static final String PROFILE_IMAGE_NAME = "profile.jpg";

    private Context context;
    private CMSContentDownloader cmsContentDownloader;
    private EventDispatcher eventDispatcher;

    @Inject
    public ProfileImageProvider(Context context, CMSContentDownloader cmsContentDownloader, EventDispatcher eventDispatcher) {
        this.context = context;
        this.cmsContentDownloader = cmsContentDownloader;
        this.eventDispatcher = eventDispatcher;
    }

    public boolean isProfileImageAvailable() {
        File profileImage = new File(getProfileImagePath());
        return profileImage.exists();
    }

    @NonNull
    public String getProfileImagePath() {
        return context.getFilesDir() + File.separator + PROFILE_IMAGE_NAME;
    }

    public void fetchProfileImage() {
        // Remove this as of now. Profile photo should be saved locally.
        //cmsContentDownloader.fetchUserFile(PROFILE_IMAGE_NAME, this);
    }

    @Override
    public void onFileDownloadSucceeded(String fileName) {
        // Remove this as of now. Profile photo should be saved locally.
        //eventDispatcher.dispatchEvent(new ProfileImageAvailableEvent());
    }

    @Override
    public void onFileDownloadFailed(String fileName) {

    }

    @Override
    public void onConnectionError() {

    }
}
