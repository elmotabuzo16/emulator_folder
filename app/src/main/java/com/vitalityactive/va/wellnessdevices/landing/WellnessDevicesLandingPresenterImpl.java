package com.vitalityactive.va.wellnessdevices.landing;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.util.Log;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.wellnessdevices.Constants;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;
import com.vitalityactive.va.wellnessdevices.landing.events.DeviceActivityMappingResponseEvent;
import com.vitalityactive.va.wellnessdevices.landing.events.FetchDevicesResponseEvent;
import com.vitalityactive.va.wellnessdevices.landing.service.GetFullListResponse;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.events.PotentialPointsResponseEvent;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.service.EventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WellnessDevicesLandingPresenterImpl implements WellnessDevicesLandingPresenter {
    private final WellnessDevicesLandingInteractorImpl interactor;
    private final EventDispatcher eventDispatcher;
    private UserInterface userInterface;
    private EventListener<FetchDevicesResponseEvent> fetchDevicesResponseEvent;
    private EventListener<PotentialPointsResponseEvent> potentialPointsEventListener;
    private EventListener<DeviceActivityMappingResponseEvent> deviceActivityMappingEventListener;
    private MainThreadScheduler scheduler;
    private boolean isProgressShown;
    private EventType potentialPoints;

    public WellnessDevicesLandingPresenterImpl(@NonNull WellnessDevicesLandingInteractorImpl interactor,
                                               @NonNull EventDispatcher eventDispatcher,
                                               @NonNull MainThreadScheduler scheduler) {
        this.interactor = interactor;
        this.eventDispatcher = eventDispatcher;
        this.scheduler = scheduler;

        createEventListeners();
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        // NOP
    }

    @Override
    public void onUserInterfaceAppeared() {
        addEventListeners();

        if (isRequestInProgress()) {
            if(isProgressShown) {
                showProgress();
            }
            return;
        }

        if (interactor.getDeviceActivityMappingRequestResult() != null) {
            handleDeviceActivityMappingEvent(interactor.getDeviceActivityMappingRequestResult());
        } else {
            fetchDeviceActivityMapping();
        }

    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        scheduler.cancel();
        removeEventListeners();
    }

    @Override
    public void onUserInterfaceDestroyed() {
        // NOP
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    private void fetchDeviceList() {
        showProgress();
        interactor.fetchDeviceList();
    }

    @Override
    public void fetchDeviceActivityMapping() {
        showProgress();
        interactor.fetchDeviceActivityMapping();
    }

    @Override
    public void loadDeviceDetails(int typeKey) {
        showProgress();
        interactor.loadDeviceDetails(typeKey);
    }

    @Override
    public EventType getPotentialPoints() {
        return potentialPoints;
    }

    @Override
    public List<EventType> getChildEventTypes() {
        if (potentialPoints == null ||
                potentialPoints.eventType == null ||
                potentialPoints.eventType.isEmpty()) {
            return new ArrayList<>();
        } else {
            return potentialPoints.eventType.get(0).eventType;
        }
    }

    @Override
    public boolean manufacturerContainsTypeKey(int typeKey) {
        for (EventType eventType : getChildEventTypes()) {
            if (eventType.typeKey == typeKey) {
                return true;
            }
        }
        return false;
    }

    @Override
    public EventType getEventType(int typeKey) {
        for (EventType eventType : getChildEventTypes()) {
            if (eventType.typeKey == typeKey) {
                return eventType;
            }
        }
        return null;
    }

    private void createEventListeners() {
        fetchDevicesResponseEvent = new EventListener<FetchDevicesResponseEvent>() {
            @Override
            public void onEvent(FetchDevicesResponseEvent event) {
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        hideProgress();
                    }
                });

                if (event.isSuccessfull()) {
                    final Pair<ArrayList<PartnerDto>, ArrayList<PartnerDto>> splitList =
                            splitDeviceList(event.getResponseBody());

                    sortListByDeviceName(splitList);

                    scheduler.schedule(new Runnable() {
                        @Override
                        public void run() {
                            userInterface.updateLinkedDevices(splitList.first, splitList.second);
                        }
                    });
                } else if (event.getRequestResult() == RequestResult.CONNECTION_ERROR) {
                    userInterface.showConnectionErrorMessage(WellnessDevicesLandingInteractor.WD_FETCH_DEVICE_LIST);
                } else if (event.getRequestResult() == RequestResult.GENERIC_ERROR) {
                    userInterface.showGenericErrorMessage(WellnessDevicesLandingInteractor.WD_FETCH_DEVICE_LIST);
                }
            }
        };

        potentialPointsEventListener = new EventListener<PotentialPointsResponseEvent>() {
            @Override
            public void onEvent(PotentialPointsResponseEvent event) {
                handlePotentialPointsEvent(event);
            }
        };

        deviceActivityMappingEventListener = new EventListener<DeviceActivityMappingResponseEvent>() {
            @Override
            public void onEvent(DeviceActivityMappingResponseEvent event) {
                handleDeviceActivityMappingEvent(event);
            }
        };
    }

    private void sortListByDeviceName(Pair<ArrayList<PartnerDto>, ArrayList<PartnerDto>> splittedList) {
        Comparator<PartnerDto> deviceNameComparator = new Comparator<PartnerDto>() {
            @Override
            public int compare(PartnerDto first, PartnerDto second) {
                return first.getDevice().compareToIgnoreCase(second.getDevice());
            }
        };

        Collections.sort(splittedList.first, deviceNameComparator);
        Collections.sort(splittedList.second, deviceNameComparator);
    }

    private void handleDeviceActivityMappingEvent(DeviceActivityMappingResponseEvent event) {
        final DeviceActivityMappingResponseEvent localEvent = event;
        if (localEvent.isSuccessfull()) {
            if (interactor.getPotentialPointsRequestResult() != null) {
                handlePotentialPointsEvent(interactor.getPotentialPointsRequestResult());
            } else {
                loadDeviceDetails(com.vitalityactive.va.constants.EventType._DEVICEDATAUPLOAD);
            }
        } else {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    hideProgress();
                }
            });

            if (event.getRequestResult() == RequestResult.CONNECTION_ERROR) {
                userInterface.showConnectionErrorMessage(WellnessDevicesLandingInteractor.WD_FETCH_DEVICE_ACTIVITY_MAPPING);
            } else if (event.getRequestResult() == RequestResult.GENERIC_ERROR) {
                userInterface.showGenericErrorMessage(WellnessDevicesLandingInteractor.WD_FETCH_DEVICE_ACTIVITY_MAPPING);
            }
        }
    }

    private void handlePotentialPointsEvent(PotentialPointsResponseEvent event) {
        final PotentialPointsResponseEvent localEvent = event;
        if (localEvent.isSuccessfull()) {
            potentialPoints = event.getResponseBody();
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    userInterface.handleSuccessfullPointsResponse();
                }
            });

            fetchDeviceList();

        } else {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    hideProgress();
                }
            });

            if (event.getRequestResult() == RequestResult.CONNECTION_ERROR) {
                userInterface.showConnectionErrorMessage(WellnessDevicesLandingInteractor.WD_GET_POTENTIAL_POINTS);
            } else if (event.getRequestResult() == RequestResult.GENERIC_ERROR) {
                userInterface.showGenericErrorMessage(WellnessDevicesLandingInteractor.WD_GET_POTENTIAL_POINTS);
            }
        }
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(FetchDevicesResponseEvent.class, fetchDevicesResponseEvent);
        eventDispatcher.addEventListener(PotentialPointsResponseEvent.class, potentialPointsEventListener);
        eventDispatcher.addEventListener(DeviceActivityMappingResponseEvent.class, deviceActivityMappingEventListener);
    }

    private void removeEventListeners() {
        eventDispatcher.removeEventListener(FetchDevicesResponseEvent.class, fetchDevicesResponseEvent);
        eventDispatcher.removeEventListener(PotentialPointsResponseEvent.class, potentialPointsEventListener);
        eventDispatcher.removeEventListener(DeviceActivityMappingResponseEvent.class, deviceActivityMappingEventListener);
    }

    @Override
    public void updateDeviceList(boolean showFullScreenIndicator) {
        if (showFullScreenIndicator) {
            showProgress();
        }
        interactor.fetchDeviceList();
    }

    private Pair<ArrayList<PartnerDto>, ArrayList<PartnerDto>> splitDeviceList(@NonNull GetFullListResponse fullList) {
        Pair<ArrayList<PartnerDto>, ArrayList<PartnerDto>> splittedList =
                new Pair<>(new ArrayList<PartnerDto>(), new ArrayList<PartnerDto>());
        for (GetFullListResponse.Market market : fullList.markets) {
            if (Constants.UNLINKED.equals(market.partner.partnerLinkedStatus)) {
                splittedList.first.add(new PartnerDto(market.partner));
            } else {
                splittedList.second.add(new PartnerDto(market.partner));
            }
        }
        return splittedList;
    }

    private boolean isRequestInProgress(){
        return interactor.isRequestInProgress();
    }

    private void showProgress() {
        Log.d(">>>", "show progress");
        isProgressShown = true;
        userInterface.showLoadingIndicator();
    }

    private void hideProgress() {
        Log.d(">>>", "hide progress");
        isProgressShown = false;
        userInterface.hideLoadingIndicator();
    }
}