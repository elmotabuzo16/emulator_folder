package com.vitalityactive.va.profile;

import android.content.Context;

import com.vitalityactive.va.cms.CMSContentDownloader;
import com.vitalityactive.va.events.EventDispatcher;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProfileImageProviderTest {

    @Mock
    private Context context;
    @Mock
    private CMSContentDownloader contentDownloader;
    @Mock
    private EventDispatcher eventDispatcher;

    private ProfileImageProvider profileImageProvider;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        profileImageProvider = new ProfileImageProvider(context, contentDownloader, eventDispatcher);
    }

    @Test
    public void should_return_profile_image_path() throws Exception {
        when(context.getFilesDir()).thenReturn(mock(File.class));
        profileImageProvider.getProfileImagePath();
        verify(context).getFilesDir();
    }

    @Test
    @Ignore("Fix me")
    public void should_fetch_user_profile_image() throws Exception {
        profileImageProvider.fetchProfileImage();
        verify(contentDownloader).fetchUserFile(ProfileImageProvider.PROFILE_IMAGE_NAME, profileImageProvider);
    }

    @Test
    @Ignore("Fix me")
    public void when_profile_image_downloaded_then_should_dispatch_event() throws Exception {
        profileImageProvider.onFileDownloadSucceeded("profileImageName");
        verify(eventDispatcher).dispatchEvent(any(ProfileImageAvailableEvent.class));
    }

}
