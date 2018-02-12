package com.vitalityactive.va.wellnessdevices.pointsmonitor;

import android.support.annotation.NonNull;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.wellnessdevices.dto.PotentialPointsDto;

public class WellnessDevicesPointsPresenterImpl implements WellnessDevicesPointsPresenter {
    private final WellnessDevicesPointsInteractorImpl interactor;
    private final EventDispatcher eventDispatcher;
    private final MeasurementContentFromResourceString uomStringsProvider;
    private WellnessDevicesPointsPresenter.UserInterface userInterface;
    private MainThreadScheduler scheduler;
    private boolean isUserInterfaceVisible;

    public WellnessDevicesPointsPresenterImpl(@NonNull WellnessDevicesPointsInteractorImpl interactor,
                                              @NonNull EventDispatcher eventDispatcher,
                                              @NonNull MainThreadScheduler scheduler,
                                              @NonNull MeasurementContentFromResourceString uomStringsProvider) {
        this.interactor = interactor;
        this.eventDispatcher = eventDispatcher;
        this.scheduler = scheduler;
        this.uomStringsProvider = uomStringsProvider;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        // NOP
    }

    @Override
    public void onUserInterfaceAppeared() {
        isUserInterfaceVisible = true;
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        isUserInterfaceVisible = false;
        scheduler.cancel();
    }

    @Override
    public void onUserInterfaceDestroyed() {
        // NOP
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public PotentialPointsDto getPotentialPoints(int typeKey) {
        return interactor.getPotentialPoints(typeKey);
    }

    @Override
    public MeasurementContentFromResourceString getUomStringsProvider() {
        return uomStringsProvider;
    }

}
