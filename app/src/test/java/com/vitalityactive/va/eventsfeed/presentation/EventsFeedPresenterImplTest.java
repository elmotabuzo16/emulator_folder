package com.vitalityactive.va.eventsfeed.presentation;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.eventsfeed.EventsFeedContent;
import com.vitalityactive.va.eventsfeed.domain.EventsFeedInteractor;
import com.vitalityactive.va.utilities.TimeUtilities;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EventsFeedPresenterImplTest {

    @Mock private Scheduler scheduler;
    @Mock private EventsFeedInteractor interactor;
    @Mock private EventDispatcher eventDispatcher;
    @Mock private TimeUtilities timeUtilities;
    @Mock private EventsFeedContent content;
    @Mock private PartyInformationRepository partyInformationRepository;

    private EventsFeedPresenter eventsFeedPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        //FIXME:This fails to compile
       // eventsFeedPresenter = new EventsFeedPresenterImpl(scheduler, interactor, eventDispatcher, timeUtilities, content, partyInformationRepository);
    }

}
