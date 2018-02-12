package com.vitalityactive.va.search;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;

/**
 * Created by chelsea.b.pioquinto on 1/29/2018.
 */

public class ContentHelpPresenterImpl <UserInterface extends ContentHelpPresenter.UserInterface> extends BasePresenter<UserInterface> implements ContentHelpPresenter<UserInterface>, EventListener<ContentHelpEvent> {

    private final EventDispatcher eventDispatcher;
    private final ContentHelpInteractor interactor;

    public ContentHelpPresenterImpl(EventDispatcher eventDispatcher, ContentHelpInteractor contentHelpInteractor) {
        this.eventDispatcher = eventDispatcher;
        this.interactor = contentHelpInteractor;
    }

    public void loadHelpDetails(String tagkey, String tagName){
        userInterface.showLoadingIndicator();
        interactor.fetchDetailsData(tagkey, tagName);
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        super.onUserInterfaceCreated(isNewNavigation);
        eventDispatcher.addEventListener(ContentHelpEvent.class, this);
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();
        eventDispatcher.addEventListener(ContentHelpEvent.class, this);
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);
        eventDispatcher.removeEventListener(ContentHelpEvent.class, this);
    }

    @Override
    public void onUserInterfaceDestroyed() {
        super.onUserInterfaceDestroyed();
        eventDispatcher.removeEventListener(ContentHelpEvent.class, this);
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void onEvent(ContentHelpEvent event) {
        if(event.getEventType() == ContentHelpEvent.EventType.DETAILS_SUCCESS){
            userInterface.hideLoadingIndicator();
            userInterface.setRelatedHelp(interactor.getThreeHelp());
            userInterface.setDetailsView(interactor.getContents());
        }

    }
}
