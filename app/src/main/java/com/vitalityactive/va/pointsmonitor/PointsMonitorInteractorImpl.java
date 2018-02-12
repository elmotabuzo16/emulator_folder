package com.vitalityactive.va.pointsmonitor;

import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestFailedEvent;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.PointsHistoryServiceResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PointsMonitorInteractorImpl implements PointsMonitorInteractor, WebServiceResponseParser<PointsHistoryServiceResponse> {
    private PointsMonitorRepository repository;
    private PointsMonitorServiceClient serviceClient;
    private EventDispatcher eventDispatcher;

    @Inject
    PointsMonitorInteractorImpl(PointsMonitorRepository repository, PointsMonitorServiceClient serviceClient, EventDispatcher eventDispatcher) {
        this.repository = repository;
        this.serviceClient = serviceClient;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public boolean hasPointsEntries() {
        return repository.hasPointsEntries();
    }

    @Override
    public boolean isFetchingPointsHistory() {
        return serviceClient.isFetchingPointsHistory();
    }

    @Override
    public void fetchPointsHistory() {
        // VA-15847: always fetch history, even if there are present
        refreshPointsHistory();
    }

    @Override
    public void refreshPointsHistory() {
        if (isFetchingPointsHistory()) {
            return;
        }
        serviceClient.fetchPointsHistory(this);
    }

    @Override
    public void parseResponse(PointsHistoryServiceResponse response) {
        repository.persistPointsHistoryResponse(response);
        eventDispatcher.dispatchEvent(new PointsHistoryRequestCompletedEvent());
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        eventDispatcher.dispatchEvent(new RequestFailedEvent(RequestFailedEvent.Type.GENERIC_ERROR));
    }

    @Override
    public void handleGenericError(Exception exception) {
        eventDispatcher.dispatchEvent(new RequestFailedEvent(RequestFailedEvent.Type.GENERIC_ERROR));
    }

    @Override
    public void handleConnectionError() {
        eventDispatcher.dispatchEvent(new RequestFailedEvent(RequestFailedEvent.Type.CONNECTION_ERROR));
    }
}
