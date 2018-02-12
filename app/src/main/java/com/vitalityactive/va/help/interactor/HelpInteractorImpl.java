package com.vitalityactive.va.help.interactor;

import android.support.annotation.NonNull;

import com.vitalityactive.va.connectivity.ConnectivityListener;
import com.vitalityactive.va.dto.HelpDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.help.HelpEvent;
import com.vitalityactive.va.help.dto.HelpRepository;
import com.vitalityactive.va.help.model.HelpResponse;
import com.vitalityactive.va.help.service.HelpServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by christian.j.p.capin on 11/30/2017.
 */

public class HelpInteractorImpl implements HelpInteractor , WebServiceResponseParser<HelpResponse> {
    private final EventDispatcher eventDispatcher;
    private HelpServiceClient helpServiceClient;
    private HelpResponse helpResponse;
    private final ConnectivityListener connectivityListener;
    private HelpRepository helpRepository;

    @Inject
    public HelpInteractorImpl(HelpServiceClient helpServiceClient,
                              @NonNull EventDispatcher eventDispatcher,
                              @NonNull ConnectivityListener connectivityListener,
                              HelpRepository helpRepository){
        this.helpServiceClient = helpServiceClient;
        this.eventDispatcher = eventDispatcher;
        this.connectivityListener = connectivityListener;
        this.helpRepository = helpRepository;

    }

    @Override
    public boolean isRequestInProgress(){
        return (helpServiceClient != null && helpServiceClient.isHelpRequestInProgress());
    }

    @Override
    public boolean initialize() {
        if (this.connectivityListener.isOnline()) {
            helpServiceClient.fetchHelpDetails(this);
        }
        return true;
    }

    @Override
    public List<HelpDTO> getFiveHelp() {
        return helpRepository.getHelpFive();
    }

    @Override
    public List<HelpDTO> getAllHelp() {
        return helpRepository.getHelp();
    }

    @Override
    public void parseResponse(HelpResponse response) {
        helpRepository.persistHelpResponse(response);
        eventDispatcher.dispatchEvent(new HelpEvent(response, HelpRequestResult.SUCCESSFUL,HelpEvent.Event.RequestMembershipPassStatus));
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        eventDispatcher.dispatchEvent(new HelpEvent(HelpRequestResult.GENERIC_ERROR,HelpEvent.Event.RequestMembershipPassStatus));
    }

    @Override
    public void handleGenericError(Exception exception) {
        eventDispatcher.dispatchEvent(new HelpEvent(HelpRequestResult.GENERIC_ERROR,HelpEvent.Event.RequestMembershipPassStatus));
    }

    @Override
    public void handleConnectionError() {
        eventDispatcher.dispatchEvent(new HelpEvent(HelpRequestResult.CONNECTION_ERROR,HelpEvent.Event.RequestMembershipPassStatus));
    }
}
