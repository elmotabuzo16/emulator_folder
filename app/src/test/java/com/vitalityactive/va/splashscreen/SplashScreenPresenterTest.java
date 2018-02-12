package com.vitalityactive.va.splashscreen;

import android.support.annotation.NonNull;

import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.TestUtilities;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.appconfig.AppConfigService;
import com.vitalityactive.va.appconfig.AppConfigServiceClient;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.cms.CMSContentDownloader;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.model.Application;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SplashScreenPresenterTest extends BaseTest {

    private static final String LIFERAY_GROUP_ID = "liferayGroupId";
    private SplashScreenPresenter presenter;
    @Mock
    private AppConfigServiceClient mockAppConfigServiceClient;
    @Mock
    private CMSContentDownloader mockCMSContentDownloader;
    @Mock
    private SplashScreenPresenter.UserInterface mockUserInterface;
    @Mock
    private AppConfigRepository mockAppConfigRepository;
    @Mock
    private AppConfigDataUpdater appConfigDataUpdater;
    private static List<String> resourceFileNames = new ArrayList<>();

    @Test
    public void logo_is_shown_initially_if_present_and_downloaded_if_app_config_changed() throws Exception {
        when(mockCMSContentDownloader.getSizedLogoFilename()).thenReturn(CMSContentDownloader.FILE_NAME_LOGO);
        givenAppConfigChangedAndLogoExists();

        presenter.onUserInterfaceCreated(true);

        verify(mockUserInterface).showLogo(anyString());
        verify(mockCMSContentDownloader, times(1)).fetchFiles(eq(getAllFileNames()), eq(LIFERAY_GROUP_ID), any(CMSContentDownloader.MultiFileCallback.class));
    }

    @Test
    public void logo_is_not_shown_if_missing() throws Exception {
        givenAppConfigChangedAndLogoIsMissing();

        presenter.onUserInterfaceCreated(true);

        verify(mockUserInterface, never()).showLogo(anyString());
    }

    @Test
    public void logo_is_not_downloaded_if_app_config_remains_the_same_and_logo_existed() throws Exception {
        givenAppConfigRemainsTheSameAndLogoExistsAndResourceFilesExist();

        presenter.onUserInterfaceCreated(true);

        verify(mockCMSContentDownloader, never()).getLogo(anyString(), any(CMSContentDownloader.Callback.class));
    }

    @Test
    public void logo_is_downloaded_if_app_config_remains_the_same_and_logo_is_missing() throws Exception {
        when(mockCMSContentDownloader.getSizedLogoFilename()).thenReturn(CMSContentDownloader.FILE_NAME_LOGO);
        givenAppConfigRemainsTheSameAndLogoIsMissing();

        presenter.onUserInterfaceCreated(true);

        verify(mockCMSContentDownloader, times(1)).fetchFiles(eq(Collections.singletonList(CMSContentDownloader.FILE_NAME_LOGO)), eq(LIFERAY_GROUP_ID), any(CMSContentDownloader.MultiFileCallback.class));
    }

    @Ignore
    @Test
    public void resource_files_are_downloaded_if_app_config_changed() throws Exception {
        when(mockCMSContentDownloader.getSizedLogoFilename()).thenReturn(CMSContentDownloader.FILE_NAME_LOGO);
        givenAppConfigChangedAndLogoExists();

        presenter.onUserInterfaceCreated(true);

        verify(mockCMSContentDownloader, times(1)).fetchFiles(eq(getAllFileNames()), eq(LIFERAY_GROUP_ID), any(CMSContentDownloader.MultiFileCallback.class));
    }

    @Test
    public void resource_files_are_not_downloaded_if_app_config_remains_the_same_and_they_are_present() throws Exception {
        givenAppConfigRemainsTheSameAndLogoExistsAndResourceFilesExist();

        presenter.onUserInterfaceCreated(true);

        verify(mockCMSContentDownloader, never()).fetchFiles(anyListOf(String.class), anyString(), any(CMSContentDownloader.MultiFileCallback.class));
    }

    @Ignore
    @Test
    public void resource_files_are_downloaded_if_app_config_remains_the_same_and_they_are_missing() throws Exception {
        givenAppConfigRemainsTheSameAndLogoExistsAndResourceFilesDontExist();

        presenter.onUserInterfaceCreated(true);

        verify(mockCMSContentDownloader, times(1)).fetchFiles(eq(resourceFileNames), eq(LIFERAY_GROUP_ID), any(CMSContentDownloader.MultiFileCallback.class));
    }

    @Ignore
    @Test
    public void navigate_to_home_screen_if_empty_app_config_returned() throws Exception {
        givenAppConfigRemainsTheSameAndLogoIsMissing();
        when(mockCMSContentDownloader.logoExists()).thenReturn(false);

        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface, times(1)).dismiss();
    }

    @NonNull
    private ArrayList<String> getAllFileNames() {
        ArrayList<String> fileNames = new ArrayList<>(SplashScreenPresenterTest.resourceFileNames);
        fileNames.add(CMSContentDownloader.FILE_NAME_LOGO);
        return fileNames;
    }

    private void givenAppConfigRemainsTheSameAndLogoExistsAndResourceFilesDontExist() throws Exception {
        when(mockCMSContentDownloader.logoExists()).thenReturn(true);
        when(mockAppConfigRepository.getFileNamesOfResourceFilesThatAreMissing()).thenReturn(resourceFileNames);
        presenter = new SetupDataBuilder().build();
    }

    private void givenAppConfigRemainsTheSameAndLogoIsMissing() throws Exception {
        when(mockCMSContentDownloader.logoExists()).thenReturn(false);
        presenter = new SetupDataBuilder()
                .resourceFileNames(Collections.<String>emptyList())
                .build();
    }

    private void givenAppConfigChangedAndLogoIsMissing() throws Exception {
        when(mockCMSContentDownloader.logoExists()).thenReturn(false);
        presenter = new SetupDataBuilder()
                .responseBody(getAppConfigChangedResponse())
                .build();
    }

    private void givenAppConfigChangedAndLogoExists() throws Exception {
        when(mockCMSContentDownloader.logoExists()).thenReturn(true);
        presenter = new SetupDataBuilder()
                .responseBody(getAppConfigChangedResponse())
                .resourceFileNames(resourceFileNames)
                .build();
    }

    private String getAppConfigChangedResponse() {
        return new TestUtilities().readFile("app_config/AppConfig_successful.json");
    }

    private void givenAppConfigRemainsTheSameAndLogoExistsAndResourceFilesExist() throws Exception {
        when(mockCMSContentDownloader.logoExists()).thenReturn(true);
        presenter = new SetupDataBuilder()
                .resourceFileNames(Collections.<String>emptyList())
                .build();
    }

    private class SetupDataBuilder {
        private MainThreadScheduler mainThreadScheduler = sameThreadScheduler;
        private Scheduler scheduler = sameThreadScheduler;
        private EventDispatcher eventDispatcher = BaseTest.eventDispatcher;
        private AppConfigService appConfigService;
        private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider = mockAccessTokenAuthorizationProvider;
        private PartyInformationRepository partyInformationRepository = getPartyInformationRepository();
        private CMSContentDownloader cmsContentDownloader = mockCMSContentDownloader;
        private SplashScreenPresenter.UserInterface userInterface = mockUserInterface;
        private int responseCode = 200;
        private String responseBody = "{}";
        private AppConfigRepository appConfigRepository = mockAppConfigRepository;
        private boolean appConfigParsingSucceeded = true;
        private String liferayGroupId = LIFERAY_GROUP_ID;
        private List<String> resourceFileNames = SplashScreenPresenterTest.resourceFileNames;
        private AppConfigDataUpdater appConfigDataUpdater;

        SplashScreenPresenter build() throws Exception {
            setUpMockWebServer(responseCode, responseBody);
            if (appConfigService == null) {
                appConfigService = getService(AppConfigService.class);
            }
            when(appConfigRepository.persistAppConfig(any(Application.class))).thenReturn(appConfigParsingSucceeded);
            when(mockAppConfigRepository.getLiferayGroupId()).thenReturn(liferayGroupId);
            when(mockAppConfigRepository.getFileNamesOfResourceFilesThatAreMissing()).thenReturn(resourceFileNames);
            when(mockAppConfigRepository.getResourceFileNames()).thenReturn(resourceFileNames);
            AppConfigServiceClient appConfigServiceClient = new AppConfigServiceClient(new WebServiceClient(scheduler, eventDispatcher), appConfigService, partyInformationRepository, accessTokenAuthorizationProvider);
            appConfigDataUpdater = new AppConfigDataUpdaterImpl(appConfigServiceClient, appConfigRepository, cmsContentDownloader, eventDispatcher);
            SplashScreenPresenter splashScreenPresenter = new SplashScreenPresenter(cmsContentDownloader, eventDispatcher, mainThreadScheduler, appConfigRepository, appConfigDataUpdater);
            splashScreenPresenter.setUserInterface(userInterface);
            return splashScreenPresenter;
        }

        SetupDataBuilder responseBody(String responseBody) {
            this.responseBody = responseBody;
            return this;
        }

        SetupDataBuilder resourceFileNames(List<String> resourceFileNames) {
            this.resourceFileNames = resourceFileNames;
            return this;
        }
    }
}
