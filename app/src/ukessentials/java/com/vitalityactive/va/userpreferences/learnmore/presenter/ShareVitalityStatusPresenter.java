package com.vitalityactive.va.userpreferences.learnmore.presenter;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.view.View;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.UkeNavigationCoordinator;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.userpreferences.ShareVitlaityStatusPreferenceToggleCompletedEvent;
import com.vitalityactive.va.userpreferences.entities.DefaultUserPreferencePresenter;
import com.vitalityactive.va.userpreferences.ShareVitalityStatusPreferenceServiceClient;

import javax.inject.Inject;

/**
 * Created by dharel.h.rosell on 12/28/2017.
 */

public class ShareVitalityStatusPresenter extends DefaultUserPreferencePresenter implements
        EventListener<ShareVitlaityStatusPreferenceToggleCompletedEvent> {

    @Inject
    protected UkeNavigationCoordinator ukeNavigationCoordinator;


    private final DeviceSpecificPreferences deviceSpecificPreferences;
    private UserInterface userInterface;
    private Context context;
    private UkeNavigationCoordinator uke;
    private View.OnClickListener listener;
    private PartyInformationRepository repository;
    private Scheduler scheduler;
    private ShareVitalityStatusPreferenceServiceClient serviceClient;
    private EventDispatcher eventDispatcher;

    public ShareVitalityStatusPresenter(int title, int description, boolean optedIn, int iconID, Type type,
                                        boolean hasToggleSwitch, boolean hasSettingsButton,
                                        DeviceSpecificPreferences deviceSpecificPreferences,
                                        View.OnClickListener listenerParam, MainThreadScheduler scheduler,
                                        PartyInformationRepository repository, EventDispatcher eventDispatcherParam,
                                        ShareVitalityStatusPreferenceServiceClient serviceClientParam) {

        super(title, description, optedIn, iconID, type, hasToggleSwitch, hasSettingsButton, null);
        this.deviceSpecificPreferences = deviceSpecificPreferences;
        listener = listenerParam;
        this.repository = repository;
        this.scheduler = scheduler;
        this.eventDispatcher = eventDispatcherParam;
        this.serviceClient = serviceClientParam;
    }

    @Override
    public void onUserInterfaceAppeared() {
        eventDispatcher.addEventListener(ShareVitlaityStatusPreferenceToggleCompletedEvent.class, this);
        if (userInterface != null) {
            userInterface.synchroniseOptedInState();
            synchroniseSwitchEnabledState();
        }
    }

    private void synchroniseSwitchEnabledState() {
        if (serviceClient.isShareVitalityToggleRequestInProgress()) {
            userInterface.disableSwitch();
        } else {
            userInterface.enableSwitch();
        }
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        eventDispatcher.removeEventListener(ShareVitlaityStatusPreferenceToggleCompletedEvent.class, this);
    }

    @Override
    public void onToggle(boolean checked) {
        userInterface.disableSwitch();
        if (checked) {
            serviceClient.optInToShareStatusVitality();
        } else {
            serviceClient.optOutOfShareStatusVitality();
        }
    }

    @Override
    public boolean isOptedIn() {
        return repository.isOptedInShareVitalityStatus();
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void setClickListener(View.OnClickListener clickListenerParam){
        this.listener = clickListenerParam;
    }

    public View.OnClickListener getClickListener() {
        return this.listener;
    }

    public UkeNavigationCoordinator getNavigator() {
        return this.uke;
    }

    @Override
    public void onEvent(ShareVitlaityStatusPreferenceToggleCompletedEvent event) {
        handleToggleCompletion();
    }

    @VisibleForTesting
    void handleToggleCompletion() {
        if (serviceClient.geShareVitalityStatusRequestResult() == RequestResult.SUCCESSFUL) {
            repository.setOptedInShareVitalityStatus(!repository.isOptedInShareVitalityStatus());
        } else {
            userInterface.showErrorMessage();
        }
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                userInterface.synchroniseOptedInState();
                userInterface.enableSwitch();
            }
        });

    }
}
