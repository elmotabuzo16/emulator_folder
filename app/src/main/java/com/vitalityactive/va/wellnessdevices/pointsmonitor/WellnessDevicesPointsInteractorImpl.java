package com.vitalityactive.va.wellnessdevices.pointsmonitor;

import android.support.annotation.NonNull;

import com.vitalityactive.va.connectivity.ConnectivityListener;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.wellnessdevices.dto.PotentialPointsDto;
import com.vitalityactive.va.wellnessdevices.linking.repository.LinkingPageRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WellnessDevicesPointsInteractorImpl implements WellnessDevicesPointsInteractor {
    private final EventDispatcher eventDispatcher;
    private final ConnectivityListener connectivityListener;
    private final LinkingPageRepository linkingPageRepository;

    @Inject
    public WellnessDevicesPointsInteractorImpl(@NonNull EventDispatcher eventDispatcher,
                                                @NonNull ConnectivityListener connectivityListener,
                                               @NonNull LinkingPageRepository linkingPageRepository){
        this.eventDispatcher = eventDispatcher;
        this.connectivityListener = connectivityListener;
        this.linkingPageRepository = linkingPageRepository;
    }

    /**
     * WellnessDevicesPointsInteractor implementation
     */
    @Override
    public PotentialPointsDto getPotentialPoints(int typeKey){
        return linkingPageRepository.getPotentialPoints(typeKey);
    }

}
