package com.vitalityactive.va.eventsfeed;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.eventsfeed.data.net.EventsFeedApiServiceClient;
import com.vitalityactive.va.eventsfeed.data.net.request.EffectivePeriod;
import com.vitalityactive.va.eventsfeed.data.net.request.EventsFeedRequest;
import com.vitalityactive.va.eventsfeed.data.net.response.EventsFeedResult;
import com.vitalityactive.va.eventsfeed.domain.EventsFeedInteractorImpl;
import com.vitalityactive.va.networking.RequestFailedEvent;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class EventsFeedInteractorImplTest {
    @Mock private EventsFeedRepository repository;
    @Mock private EventsFeedApiServiceClient serviceClient;
    @Mock private EventDispatcher eventDispatcher;
    @Mock private PartyInformationRepository partyInformationRepository;
    @Mock private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;

    private EventsFeedInteractorImpl interactor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        interactor = new EventsFeedInteractorImpl(repository, serviceClient, eventDispatcher, partyInformationRepository, accessTokenAuthorizationProvider);
    }

    @Test
    public void should_check_repo_has_entries() {
        interactor.hasEntries();
        Mockito.verify(repository).hasEntries();
    }

    @Test
    public void should_check_serviceClient_is_fetching(){
        interactor.isFetching();
        Mockito.verify(serviceClient).isFetching();
    }

    @Test
    @Ignore
    public void should_check_interactor_calling_fetchEventsFeed(){

        EffectivePeriod effectivePeriod = new EffectivePeriod();

        EventsFeedRequest request = new EventsFeedRequest(effectivePeriod, null, null);
        request.setEffectivePeriod(effectivePeriod);

        interactor.refresh(request);
        Mockito.verify(serviceClient).getEventsFeedByParty(interactor,
                partyInformationRepository.getTenantId(),
                partyInformationRepository.getPartyId()+"",
                request,
                accessTokenAuthorizationProvider.getAuthorization()
                );
    }

    @Test
    public void should_call_parseResponse(){
        EventsFeedResult eventsFeedResult = new EventsFeedResult();

        interactor.parseResponse(eventsFeedResult);

        Mockito.verify(repository).persistResponse(eventsFeedResult);
        Mockito.verify(eventDispatcher).dispatchEvent(Mockito.any(EventsFeedRequestCompletedEvent.class));
    }

    @Test
    public void should_call_parseErrorResponse(){
        interactor.parseErrorResponse(null, 0);
        Mockito.verify(eventDispatcher).dispatchEvent(Mockito.any(RequestFailedEvent.class));
    }

    @Test
    public void should_call_handleGenericError(){
        interactor.handleGenericError(null);
        Mockito.verify(eventDispatcher).dispatchEvent(Mockito.any(RequestFailedEvent.class));
    }

    @Test
    public void should_call_handleConnectionError(){
        interactor.handleConnectionError();
        Mockito.verify(eventDispatcher).dispatchEvent(Mockito.any(RequestFailedEvent.class));
    }
}
