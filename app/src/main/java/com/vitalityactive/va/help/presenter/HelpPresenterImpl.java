package com.vitalityactive.va.help.presenter;

import android.util.Log;

import com.drew.lang.annotations.Nullable;
import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.dto.HelpDTO;
import com.vitalityactive.va.dto.MembershipPassDTO;
import com.vitalityactive.va.dto.PersonalDetailsDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.help.HelpEvent;
import com.vitalityactive.va.help.dto.HelpRepository;
import com.vitalityactive.va.help.interactor.HelpInteractor;
import com.vitalityactive.va.help.model.HelpResponse;
import com.vitalityactive.va.membershippass.dto.MembershipPassRepository;
import com.vitalityactive.va.persistence.models.Help;
import com.vitalityactive.va.search.ContentHelpPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christian.j.p.capin on 11/30/2017.
 */

public class HelpPresenterImpl <UI extends HelpPresenter.UI> extends BasePresenter<UI> implements HelpPresenter<UI> {

    private MainThreadScheduler scheduler;
    private final EventDispatcher eventDispatcher;
    private HelpInteractor interactor;
    private HelpRepository repository;
    private List<HelpDTO> helpDetails;
    private HelpResponse helpResponse;


    public HelpPresenterImpl(HelpInteractor interactor,
                             EventDispatcher eventDispatcher,
                             HelpRepository helpRepository,
                             MainThreadScheduler scheduler) {
        this.interactor = interactor;
        this.eventDispatcher = eventDispatcher;
        this.repository = helpRepository;
        this.scheduler = scheduler;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        eventDispatcher.addEventListener(HelpEvent.class, helpEventEventListener);
    }

    @Override
    public void loadSuggestion(){
        userInterface.showLoadingIndicator();
        interactor.initialize();
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();
       // initMembershipPassInitial();
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);
//
    }

    @Override
    public void onUserInterfaceDestroyed() {
        userInterface.activityDestroyed();
        eventDispatcher.addEventListener(HelpEvent.class, helpEventEventListener);
    }

    @Override
    public void setUserInterface(UI userInterface) {
        this.userInterface = userInterface;
    }

    public void loadHelpSuggestions(HelpEvent event) {

        if (event.getHelpRequestResult() == HelpInteractor.HelpRequestResult.SUCCESSFUL) {
            userInterface.hideLoadingIndicator();
            helpResponse=event.getResponse();
            userInterface.showSuggestions(interactor.getFiveHelp(), interactor.getAllHelp());
//            if (event.getHelpRequestResult() == HelpInteractor.HelpRequestResult.SUCCESSFUL) {
//
//                scheduler.schedule(new Runnable() {
//                @Override
//                public void run() {
//                    userInterface.showSuggestions(repository.getHelp());
//                }
//            });

        }
    }

    private void initMembershipPassInitial() {
        if (hasHelpSuggestions(helpDetails)){

        }
    }

    private boolean hasHelpSuggestions(List<HelpDTO> help) {
        return (help != null);
    }

    public final EventListener<HelpEvent> helpEventEventListener = new EventListener<HelpEvent>() {
        @Override
        public void onEvent(HelpEvent helpEventEventListener) {
            loadHelpSuggestions(helpEventEventListener);

        }
    };
}