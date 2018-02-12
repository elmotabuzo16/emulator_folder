package com.vitalityactive.va.userpreferences;

import android.support.annotation.VisibleForTesting;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.userpreferences.entities.DefaultUserPreferencePresenter;

public class EmailPreferencePresenter
        extends DefaultUserPreferencePresenter
        implements EventListener<EmailPreferenceToggleCompletedEvent> {

    private EmailPreferenceServiceClient serviceClient;
    private EventDispatcher eventDispatcher;
    private Scheduler scheduler;
    private PartyInformationRepository repository;
    private UserInterface userInterface;

    public EmailPreferencePresenter(int title, int description, boolean enabled,
                                    int iconID, Type type, boolean hasToggleSwitch,
                                    boolean hasSettingsButton, EmailPreferenceServiceClient serviceClient,
                                    EventDispatcher eventDispatcher, MainThreadScheduler scheduler,
                                    PartyInformationRepository repository) {

        super(title, description, enabled, iconID, type, hasToggleSwitch, hasSettingsButton, null);
        this.serviceClient = serviceClient;
        this.eventDispatcher = eventDispatcher;
        this.scheduler = scheduler;
        this.repository = repository;
        setRepo(repository);
    }

    @Override
    public void onUserInterfaceAppeared() {
        eventDispatcher.addEventListener(EmailPreferenceToggleCompletedEvent.class, this);
        if (userInterface != null) {
            userInterface.synchroniseOptedInState();
            synchroniseSwitchEnabledState();
        }
    }

    private void synchroniseSwitchEnabledState() {
        if (serviceClient.isEmailToggleRequestInProgress()) {
            userInterface.disableSwitch();
        } else {
            userInterface.enableSwitch();
        }
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        eventDispatcher.removeEventListener(EmailPreferenceToggleCompletedEvent.class, this);
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void onToggle(boolean checked) {
        userInterface.disableSwitch();
        if (checked) {
            serviceClient.optInToEmailCommunication();
        } else {
            serviceClient.optOutOfEmailCommunication();
        }
    }

    @Override
    public void onEvent(EmailPreferenceToggleCompletedEvent event) {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                handleToggleCompletion();
            }
        });
    }

    @VisibleForTesting
    void handleToggleCompletion() {
        if (serviceClient.getEmailPreferenceRequestResult() == RequestResult.SUCCESSFUL) {
            repository.setOptedInToEmailCommunication(!repository.isOptedInToEmailCommunication());
        } else {
            userInterface.showErrorMessage();
        }
        userInterface.synchroniseOptedInState();
        userInterface.enableSwitch();
    }

    @Override
    public boolean isOptedIn() {
        return repository.isOptedInToEmailCommunication();
    }
}
