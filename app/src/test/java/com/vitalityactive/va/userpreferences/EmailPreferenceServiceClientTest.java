package com.vitalityactive.va.userpreferences;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.model.UpdatePartyServiceRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import retrofit2.Call;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmailPreferenceServiceClientTest {

    @Mock
    private WebServiceClient webSvc;
    @Mock
    private UserPreferencesService userPrefsService;
    @Mock
    private PartyInformationRepository repository;
    @Mock
    private EventDispatcher eventDispatcher;
    @Mock
    private AccessTokenAuthorizationProvider accessTokenAuthProvider;
    @Captor
    private ArgumentCaptor<String> stringCaptor;
    @Captor
    private ArgumentCaptor<Long> longCaptor;
    @Captor
    private ArgumentCaptor<UpdatePartyServiceRequest> serviceRequestCaptor;

    @Mock
    private Call<String> request;

    private EmailPreferenceServiceClient emailPreferenceSvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        emailPreferenceSvc = spy(new EmailPreferenceServiceClient(webSvc, userPrefsService, repository, eventDispatcher, accessTokenAuthProvider));
    }

    @Test
    public void should_opt_in_to_email_communication() throws Exception {
        when(userPrefsService.getEmailPreferenceUpdateRequest(anyString(), anyLong(), anyLong(), any(UpdatePartyServiceRequest.class)))
                .thenReturn(request);

        emailPreferenceSvc.optInToEmailCommunication();

        verify(userPrefsService).getEmailPreferenceUpdateRequest(stringCaptor.capture(), longCaptor.capture(), longCaptor.capture(), serviceRequestCaptor.capture());
        verify(webSvc).executeAsynchronousRequest(request, emailPreferenceSvc);
        UpdatePartyServiceRequest.UpdatePartyRequestBody requestBody = serviceRequestCaptor.getValue().updatePartyRequest;
        assertNotNull(requestBody);
        assertEquals(UpdatePartyServiceRequest.GeneralPreferenceType.EMAIL.getTypeKey(), requestBody.generalPreferences[0].typeKey);
        assertEquals("true", requestBody.generalPreferences[0].value);
    }

    @Test
    public void should_opt_out_of_email_communication() throws Exception {
        when(userPrefsService.getEmailPreferenceUpdateRequest(anyString(), anyLong(), anyLong(), any(UpdatePartyServiceRequest.class)))
                .thenReturn(request);

        emailPreferenceSvc.optOutOfEmailCommunication();

        verify(userPrefsService).getEmailPreferenceUpdateRequest(stringCaptor.capture(), longCaptor.capture(), longCaptor.capture(), serviceRequestCaptor.capture());
        verify(webSvc).executeAsynchronousRequest(request, emailPreferenceSvc);
        UpdatePartyServiceRequest.UpdatePartyRequestBody requestBody = serviceRequestCaptor.getValue().updatePartyRequest;
        assertNotNull(requestBody);
        assertEquals(UpdatePartyServiceRequest.GeneralPreferenceType.EMAIL.getTypeKey(), requestBody.generalPreferences[0].typeKey);
        assertEquals("false", requestBody.generalPreferences[0].value);
    }

    @Test
    public void should_parse_success_response() throws Exception {
        emailPreferenceSvc.parseResponse("");
        verify(eventDispatcher).dispatchEvent(any(EmailPreferenceToggleCompletedEvent.class));
        assertThat(emailPreferenceSvc.getEmailPreferenceRequestResult(), is(RequestResult.SUCCESSFUL));
    }

    @Test
    public void should_parse_error_response() throws Exception {
        emailPreferenceSvc.parseErrorResponse("", 0);

        verify(eventDispatcher).dispatchEvent(any(EmailPreferenceToggleCompletedEvent.class));
        assertThat(emailPreferenceSvc.getEmailPreferenceRequestResult(), is(RequestResult.GENERIC_ERROR));
    }

    @Test
    public void should_handle_generic_error() throws Exception {
        emailPreferenceSvc.handleGenericError(mock(Exception.class));

        verify(eventDispatcher).dispatchEvent(any(EmailPreferenceToggleCompletedEvent.class));
        assertThat(emailPreferenceSvc.getEmailPreferenceRequestResult(), is(RequestResult.GENERIC_ERROR));
    }

    @Test
    public void should_handle_connection_error() throws Exception {
        emailPreferenceSvc.handleConnectionError();

        verify(eventDispatcher).dispatchEvent(any(EmailPreferenceToggleCompletedEvent.class));
        assertThat(emailPreferenceSvc.getEmailPreferenceRequestResult(), is(RequestResult.CONNECTION_ERROR));
    }

}