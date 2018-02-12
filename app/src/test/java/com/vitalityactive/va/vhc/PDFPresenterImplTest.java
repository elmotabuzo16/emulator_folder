package com.vitalityactive.va.vhc;

import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.cms.CMSContentDownloader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.File;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PDFPresenterImplTest {

    private static final String FILE_NAME = "hcb.pdf";

    @Mock
    private File file;
    @Mock
    private CMSContentDownloader contentDownloader;
    @Mock
    private AppConfigRepository appConfigRepo;
    @Mock
    private PDFPresenter.UserInterface userInterface;
    @Spy
    private Scheduler scheduler = new SameThreadScheduler();

    private PDFPresenterImpl presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        presenter = new PDFPresenterImpl(contentDownloader, appConfigRepo, scheduler);
        presenter.setUserInterface(userInterface);
        presenter.setFileName(FILE_NAME);
    }

    @Test
    public void onUserInterfaceCreated() throws Exception {
        when(appConfigRepo.getFile(anyString())).thenReturn(file);
        presenter.onUserInterfaceCreated(false);
        verify(appConfigRepo).getFile(anyString());
    }

    @Test
    public void should_fetch_health_care_benefit_file() throws Exception {
        String lgId = "123";
        when(appConfigRepo.getLiferayGroupId()).thenReturn(lgId);

        presenter.fetchPDF();

        InOrder inOrder = inOrder(userInterface, contentDownloader, appConfigRepo);
        inOrder.verify(userInterface).showLoadingIndicator();
        inOrder.verify(appConfigRepo).getLiferayGroupId();
        inOrder.verify(contentDownloader).fetchPublicFile(lgId, FILE_NAME, presenter);
    }

    @Test
    public void when_health_care_benefit_file_download_succeeds_then_schedule_for_handling() throws Exception {
        when(appConfigRepo.getFile(anyString())).thenReturn(file);
        presenter.onFileDownloadSucceeded(FILE_NAME);
        verify(scheduler).schedule(any(Runnable.class));
    }

    @Test
    public void when_health_care_benefit_file_download_fails_then_schedule_for_handling() throws Exception {
        presenter.onFileDownloadFailed(FILE_NAME);
        verify(scheduler).schedule(any(Runnable.class));
    }

    @Test
    public void when_a_connection_error_prevents_health_care_benefit_file_download_then_schedule_for_handling() throws Exception {
        presenter.onConnectionError();
        verify(scheduler).schedule(any(Runnable.class));
    }

    @Test
    public void when_cannot_connect_to_download_health_care_benefit_file_then_display_connection_error() throws Exception {
        presenter.handleConnectionError();

        InOrder inOrder = inOrder(userInterface);
        inOrder.verify(userInterface).hideLoadingIndicator();
        inOrder.verify(userInterface).displayConnectionError();
    }

    @Test
    public void when_health_care_benefit_file_download_fails_then_display_generic_error() throws Exception {
        presenter.handlePDFDownloadFailed();

        InOrder inOrder = inOrder(userInterface);
        inOrder.verify(userInterface).hideLoadingIndicator();
        inOrder.verify(userInterface).displayGenericError();
    }

    @Test
    public void when_health_care_benefit_file_download_succeeds_then_display_it_in_pdf_view_and_also_create_share_intent() throws Exception {
        when(appConfigRepo.getFile(anyString())).thenReturn(file);

        presenter.handlePDFDownloadSuccessful();

        InOrder inOrder = inOrder(userInterface, appConfigRepo);
        inOrder.verify(userInterface).hideLoadingIndicator();
        inOrder.verify(appConfigRepo).getFile(FILE_NAME);
        inOrder.verify(userInterface).displayFileInPDFView(any(File.class));
        inOrder.verify(userInterface).createShareIntent(any(File.class));
    }
}