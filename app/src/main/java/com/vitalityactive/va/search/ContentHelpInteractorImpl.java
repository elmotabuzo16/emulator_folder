package com.vitalityactive.va.search;

import android.util.Log;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.dto.ContentHelpDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.help.dto.HelpRepository;
import com.vitalityactive.va.help.model.HelpResponse;
import com.vitalityactive.va.help.service.HelpServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.partnerjourney.models.PartnerItemDetails;
import com.vitalityactive.va.partnerjourney.service.models.PartnerDetailResponse;
import com.vitalityactive.va.persistence.models.ContentHelp;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by chelsea.b.pioquinto on 1/30/2018.
 */

public class ContentHelpInteractorImpl implements ContentHelpInteractor {

    private final EventDispatcher eventDispatcher;
    private final HelpServiceClient helpServiceClient;
    private final PartyInformationRepository partyInfo;
    private final MainThreadScheduler scheduler;
    private final HelpRepository helpRepository;


    @Inject
    public ContentHelpInteractorImpl(EventDispatcher eventDispatcher, HelpServiceClient helpServiceClient, PartyInformationRepository partyInfo, MainThreadScheduler mainThreadScheduler, HelpRepository helpRepository) {
        this.eventDispatcher = eventDispatcher;
        this.helpServiceClient = helpServiceClient;
        this.partyInfo = partyInfo;
        this.scheduler = mainThreadScheduler;
        this.helpRepository = helpRepository;
    }

    @Override
    public void fetchDetailsData(String tagkey, String tagName){
        helpServiceClient.fetchHelpContents(tagkey, tagName, new WebServiceResponseParser<ContentHelpResponse>(){

            @Override
            public void parseResponse(ContentHelpResponse response) {
                scheduler.schedule(() -> {
                    helpRepository.persistHelpContent(response);
                    eventDispatcher.dispatchEvent(new ContentHelpEvent(ContentHelpEvent.EventType.DETAILS_SUCCESS));
                });
            }

            @Override
            public void parseErrorResponse(String errorBody, int code) {
                scheduler.schedule(() -> eventDispatcher.dispatchEvent(new ContentHelpEvent(ContentHelpEvent.EventType.DETAILS_FAILED)));
            }

            @Override
            public void handleGenericError(Exception exception) {
                scheduler.schedule(() -> eventDispatcher.dispatchEvent(new ContentHelpEvent(ContentHelpEvent.EventType.DETAILS_FAILED)));
            }

            @Override
            public void handleConnectionError() {
                scheduler.schedule(() -> eventDispatcher.dispatchEvent(new ContentHelpEvent(ContentHelpEvent.EventType.DETAILS_CONNECTION_ERROR)));
            }
        });
    }

    @Override
    public List<ContentHelpDTO> getContents(){
        Log.d("ContentHelp", helpRepository.getContentHelp().toString());
        return helpRepository.getContentHelp();
    }

    @Override
    public List<ContentHelpDTO> getThreeHelp(){
        return helpRepository.getThreeRelatedHelp();
    }
}
