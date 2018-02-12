package com.vitalityactive.va.profile;


import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.dto.PersonalDetailsDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

public class ChangeEntityNumberPresenter extends BasePresenter<ChangeEntityNumberPresenter.UI> implements EventListener<ChangeEntityNumberEvent> {

    private String partyId;
    private String newEntityNumber;
    private String dateOfBirthStr;
    private String currentEN;
    private final ChangeEntityNumberInteractor interactor;
    private final EventDispatcher eventDispatcher;
    private final PartyInformationRepository partyInfoRepo;
    private final DateFormattingUtilities dateFormatUtil;

    public ChangeEntityNumberPresenter(EventDispatcher eventDispatcher, ChangeEntityNumberInteractor changeEntityNumberInteractor, PartyInformationRepository partyInformationRepository, DateFormattingUtilities dateFormatUtil) {
        this.interactor = changeEntityNumberInteractor;
        this.eventDispatcher = eventDispatcher;
        this.partyInfoRepo = partyInformationRepository;
        this.dateFormatUtil = dateFormatUtil;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        super.onUserInterfaceCreated(isNewNavigation);
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();
        eventDispatcher.addEventListener(ChangeEntityNumberEvent.class, this);
        userInterface.hideLoadingIndicator();
        showEntityView();
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);
        eventDispatcher.removeEventListener(ChangeEntityNumberEvent.class, this);
    }

    @Override
    public void onUserInterfaceDestroyed() {
        super.onUserInterfaceDestroyed();
    }

    @Override
    public void setUserInterface(UI ui) {
        this.userInterface = ui;
    }

    protected void onUserEntersEntityNumber(String newEntity) {
        partyId = String.valueOf(partyInfoRepo.getPartyId());
        PersonalDetailsDTO personalDetails = partyInfoRepo.getPersonalDetails();
        dateOfBirthStr = personalDetails.getDateOfBirthString();
        newEntityNumber = newEntity;

        if(newEntity.length() > 0){
            if (currentEN.length() > 0) {
                userInterface.showLoadingIndicator();
                interactor.changeEntityNumber(partyId, dateOfBirthStr, newEntityNumber);
            } else {
                userInterface.showLoadingIndicator();
                interactor.addEntityNumber(partyId, dateOfBirthStr, newEntityNumber);
            }
        } else {
            userInterface.hideLoadingIndicator();
        }
    }

    private void showEntityView(){
        if(TextUtilities.isNullOrEmpty(partyInfoRepo.getEntityNumber())){
            currentEN = partyInfoRepo.getEntityNumberFromDevicePreference();
        } else {
            currentEN = partyInfoRepo.getEntityNumber();
        }
        userInterface.showEntityNumber(currentEN);
    }

    @Override
    public void onEvent(ChangeEntityNumberEvent event) {
        userInterface.hideLoadingIndicator();
        if(event.getEventType() == ChangeEntityNumberEvent.EventType.CHANGE_ENTITY_SUCCESS){
            userInterface.showChangeEntityConfirmation();
        } else if(event.getEventType() == ChangeEntityNumberEvent.EventType.CHANGE_ENTITY_FAILED){
            userInterface.showIncorrectEntityNumber();
        } else if(event.getEventType() == ChangeEntityNumberEvent.EventType.CHANGE_ENTITY_CONNECTION_FAILED){
            userInterface.showENConnectionError();
        } else if(event.getEventType() == ChangeEntityNumberEvent.EventType.CHANGE_ENTITY_ERROR){
            userInterface.showUnknownError();
        } else {
            userInterface.hideLoadingIndicator();
        }
    }


    interface UI {
        void showEntityNumber(String currentEntity);

        void showIncorrectEntityNumber();
        void showENConnectionError();
        void showUnknownError();
        void showChangeEntityConfirmation();

        void showLoadingIndicator();
        void hideLoadingIndicator();
    }
}
