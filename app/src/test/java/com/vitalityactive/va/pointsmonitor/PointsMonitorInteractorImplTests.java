package com.vitalityactive.va.pointsmonitor;

import com.vitalityactive.va.events.EventDispatcher;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PointsMonitorInteractorImplTests {
    @Mock
    private PointsMonitorRepository repository;
    @Mock
    private PointsMonitorServiceClient serviceClient;
    @Mock
    private EventDispatcher eventDispatcher;
    private PointsMonitorInteractorImpl interactor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        interactor = new PointsMonitorInteractorImpl(repository, serviceClient, eventDispatcher);
    }

    @Test
    public void update_even_when_already_have_values() {
        when(repository.hasPointsEntries()).thenReturn(true);

        interactor.fetchPointsHistory();

        verify(serviceClient).fetchPointsHistory(eq(interactor));
    }
}
