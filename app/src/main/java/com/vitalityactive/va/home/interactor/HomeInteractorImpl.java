package com.vitalityactive.va.home.interactor;

import android.support.annotation.NonNull;

import com.vitalityactive.va.connectivity.ConnectivityListener;
import com.vitalityactive.va.dto.HomeCardDTO;
import com.vitalityactive.va.dto.RewardHomeCardDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.home.events.GetCardCollectionResponseEvent;
import com.vitalityactive.va.home.events.GetEventStatusByPartyIdResponseEvent;
import com.vitalityactive.va.home.repository.HomeScreenCardSectionRepository;
import com.vitalityactive.va.home.service.EventByPartyResponse;
import com.vitalityactive.va.home.service.EventByPartyServiceClient;
import com.vitalityactive.va.home.service.HomeScreenCardStatusResponse;
import com.vitalityactive.va.home.service.HomeScreenCardStatusServiceClient;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.vitalitystatus.repository.VitalityStatusRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeInteractorImpl implements HomeInteractor {
    private final HomeScreenCardStatusServiceClient cardsServiceClient;
    private final EventByPartyServiceClient eventByPartyServiceClient;
    private final EventDispatcher eventDispatcher;
    private final HomeScreenCardSectionRepository cardRepository;
    private VitalityStatusRepository vitalityStatusRepository;
    private GetEventStatusByPartyIdResponseEvent getEventStatusByPartyIdResponseEvent = null;
    private final ConnectivityListener connectivityListener;

    public HomeInteractorImpl(HomeScreenCardStatusServiceClient cardsServiceClient,
                              EventByPartyServiceClient eventByPartyServiceClient,
                              @NonNull ConnectivityListener connectivityListener,
                              HomeScreenCardSectionRepository cardRepository,
                              EventDispatcher eventDispatcher,
                              VitalityStatusRepository vitalityStatusRepository) {
        this.cardsServiceClient = cardsServiceClient;
        this.eventByPartyServiceClient = eventByPartyServiceClient;
        this.connectivityListener = connectivityListener;
        this.eventDispatcher = eventDispatcher;
        this.cardRepository = cardRepository;
        this.vitalityStatusRepository = vitalityStatusRepository;
    }

    @Override
    public boolean isRequestInProgress() {
        return (cardsServiceClient != null && cardsServiceClient.isRequestInProgress()) ||
                (eventByPartyServiceClient != null && eventByPartyServiceClient.isRequestInProgress());
    }

    @Override
    public void fetchHomeCards() {
        if (connectivityListener.isOnline()) {
            cardsServiceClient.fetchHomeCards(new FetchDeviceListResponseParser());
        } else {
            onError(new GetCardCollectionResponseEvent(RequestResult.CONNECTION_ERROR));
        }
    }

    @Override
    public List<HomeCardDTO> getHomeCards() {
        return cardRepository.getHomeCards();
    }

    @Override
    public List<RewardHomeCardDTO> getRewardHomeCards(HomeCardType rewardCardType) {
        return cardRepository.getRewardHomeCards(rewardCardType);
    }

    @Override
    public void checkEventStatusByPartyId(int eventTypeKey) {
        getEventStatusByPartyIdResponseEvent = null;
        if (connectivityListener.isOnline()) {
            eventByPartyServiceClient.getEventStatusByKey(eventTypeKey, new GetEventStatusResponseParser());
        } else {
            getEventStatusByPartyIdResponseEvent = new GetEventStatusByPartyIdResponseEvent(RequestResult.CONNECTION_ERROR);
            eventDispatcher.dispatchEvent(getEventStatusByPartyIdResponseEvent);
        }
    }

    @Override
    public @NavigationMode int getVhrStatus() {
        if (getEventStatusByPartyIdResponseEvent == null){
            return UNKNOWN;
        } else if(getEventStatusByPartyIdResponseEvent.getRequestResult() != RequestResult.SUCCESSFUL){
            return UNKNOWN;
        } else if(getEventStatusByPartyIdResponseEvent.getResponseBody() == null ||
                getEventStatusByPartyIdResponseEvent.getResponseBody().event == null ||
                getEventStatusByPartyIdResponseEvent.getResponseBody().event.isEmpty()){
            return NOT_STARTED;
        } else {
            return STARTED;
        }
    }

    @Override
    public void clearVhrStatus() {
        getEventStatusByPartyIdResponseEvent = null;
    }

    private void onError(GetCardCollectionResponseEvent event) {
        eventDispatcher.dispatchEvent(event);
    }


    private class FetchDeviceListResponseParser implements WebServiceResponseParser<HomeScreenCardStatusResponse> {

        @Override
        public void parseResponse(HomeScreenCardStatusResponse response) {

            //TODO: jay: Remove this when API is already available
            List<HomeScreenCardStatusResponse.Section> section = response.sections;
            section.get(0).cards.add(createVNACard());

            cardRepository.persistCardSectionResponse(response);
            vitalityStatusRepository.persistVitalityStatusResponse(response);
            eventDispatcher.dispatchEvent(new GetCardCollectionResponseEvent(RequestResult.SUCCESSFUL, response));
        }

        @Override
        public void parseErrorResponse(String errorBody, int code) {
            onError(new GetCardCollectionResponseEvent(RequestResult.GENERIC_ERROR));
        }

        @Override
        public void handleGenericError(Exception exception) {
            onError(new GetCardCollectionResponseEvent(RequestResult.GENERIC_ERROR));
        }

        @Override
        public void handleConnectionError() {
            onError(new GetCardCollectionResponseEvent(RequestResult.CONNECTION_ERROR));
        }
    }

    private class GetEventStatusResponseParser implements WebServiceResponseParser<EventByPartyResponse> {

        @Override
        public void parseResponse(EventByPartyResponse response) {
            getEventStatusByPartyIdResponseEvent = new GetEventStatusByPartyIdResponseEvent(RequestResult.SUCCESSFUL, response);
            eventDispatcher.dispatchEvent(getEventStatusByPartyIdResponseEvent);
        }

        @Override
        public void parseErrorResponse(String errorBody, int code) {
            getEventStatusByPartyIdResponseEvent = new GetEventStatusByPartyIdResponseEvent(RequestResult.GENERIC_ERROR);
            eventDispatcher.dispatchEvent(getEventStatusByPartyIdResponseEvent);
        }

        @Override
        public void handleGenericError(Exception exception) {
            getEventStatusByPartyIdResponseEvent = new GetEventStatusByPartyIdResponseEvent(RequestResult.GENERIC_ERROR);
            eventDispatcher.dispatchEvent(getEventStatusByPartyIdResponseEvent);
        }

        @Override
        public void handleConnectionError() {
            getEventStatusByPartyIdResponseEvent = new GetEventStatusByPartyIdResponseEvent(RequestResult.CONNECTION_ERROR);
            eventDispatcher.dispatchEvent(getEventStatusByPartyIdResponseEvent);
        }
    }

    //TODO: jay: Remove this when API is already available
    //VNA MOCK TEMP
    private  HomeScreenCardStatusResponse.Card createVNACard(){
        HomeScreenCardStatusResponse.Card snvCard = new HomeScreenCardStatusResponse.Card();
        snvCard.amountCompleted = 0;
        snvCard.typeKey = "2";
        snvCard.statusTypeKey = 3;
        snvCard.total = "9999";
        snvCard.priority = 2;
        snvCard.cardMetadatas = new ArrayList<>();
        snvCard.cardItems = new ArrayList<>();
        return  snvCard;
    }

}
