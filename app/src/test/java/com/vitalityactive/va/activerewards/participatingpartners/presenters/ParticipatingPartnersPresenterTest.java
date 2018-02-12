package com.vitalityactive.va.activerewards.participatingpartners.presenters;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.activerewards.participatingpartners.ParticipatingPartnersInteractor;
import com.vitalityactive.va.dto.PartnerItemDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.partnerjourney.models.PartnerItem;
import com.vitalityactive.va.partnerjourney.service.PartnerListRequestEvent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ParticipatingPartnersPresenterTest {
    @Mock
    private EventDispatcher eventDispatcher;
    @Mock
    private ParticipatingPartnersInteractor interactor;
    @Mock
    private MainThreadScheduler scheduler;
    @Mock
    private ParticipatingPartnersPresenter.UserInterface userInterface;

    private ParticipatingPartnersPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        eventDispatcher = Mockito.spy(new EventDispatcher());
        scheduler = Mockito.spy(new SameThreadScheduler());

        presenter = new ParticipatingPartnersPresenter(interactor, eventDispatcher, scheduler);
        presenter.setUserInterface(userInterface);
    }

    @Test
    public void should_setup_event_listener_when_ui_created() throws Exception {
        presenter.onUserInterfaceCreated(true);
        InOrder inOrder = Mockito.inOrder(eventDispatcher);
        inOrder.verify(eventDispatcher).removeEventListener(PartnerListRequestEvent.class, presenter);
        inOrder.verify(eventDispatcher).addEventListener(PartnerListRequestEvent.class, presenter);
    }

    @Test
    public void should_fetch_participating_partners_when_ui_appears() throws Exception {
        presenter.onUserInterfaceAppeared();

    }

    @Test
    public void on_retry_should_fetch_participating_partners() throws Exception {
        presenter.onRetry();
        verify(interactor).fetchRewardPartners();
    }

    @Test
    public void should_fetch_participating_partners_from_remote_when_none_found_locally() throws Exception {
        when(interactor.getActiveRewardsPartners()).thenReturn(Collections.<PartnerItemDTO>emptyList());

        presenter.fetchRewardPartners();

        verify(interactor).fetchRewardPartners();
    }

    @Test
    public void should_load_persisted_participating_partners() throws Exception {
        when(interactor.getActiveRewardsPartners()).thenReturn(Collections.singletonList(new PartnerItemDTO(new PartnerItem())));

        presenter.fetchRewardPartners();

        verify(interactor, never()).fetchRewardPartners();
    }

}