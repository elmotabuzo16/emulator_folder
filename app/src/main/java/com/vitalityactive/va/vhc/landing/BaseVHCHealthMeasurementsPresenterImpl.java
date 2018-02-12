package com.vitalityactive.va.vhc.landing;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.myhealth.content.MyHealthContent;
import com.vitalityactive.va.myhealth.events.VitalityAgeEvents;
import com.vitalityactive.va.myhealth.service.VitalityAgeServiceClient;
import com.vitalityactive.va.networking.RequestFailedEvent;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.vhc.dto.HealthAttributeGroupDTO;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepository;
import com.vitalityactive.va.vhc.service.HealthAttributeRequestSuccess;
import com.vitalityactive.va.vhc.service.HealthAttributeServiceClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public abstract class BaseVHCHealthMeasurementsPresenterImpl implements VHCHealthMeasurementsPresenter {
    private final HealthAttributeServiceClient healthAttributeServiceClient;
    private final HealthAttributeRepository healthAttributeRepository;
    private final EventDispatcher eventDispatcher;
    private final Scheduler scheduler;
    private final InsurerConfigurationRepository insurerConfigurationRepository;
    private final DeviceSpecificPreferences deviceSpecificPreferences;
    private List<HealthAttributeGroupDTO> configuredHealthAttributeGroups;
    private UserInterface userInterface;
    private EventListener<HealthAttributeRequestSuccess> healthAttributeRequestSuccessEventListener;
    private EventListener<RequestFailedEvent> requestFailureEventListener;
    private EventListener<VitalityAgeEvents.VitalityAgeResponseEvent> vitalityAgeEventListener;
    private final VitalityAgeServiceClient vitalityAgeServiceClient;
    boolean isCompletingFormSubmittion = false;
    private List<HealthAttributeGroupDTO> completeGroups = new ArrayList<>();
    protected List<HealthAttributeGroupDTO> incompleteGroups = new ArrayList<>();

    public BaseVHCHealthMeasurementsPresenterImpl(HealthAttributeServiceClient healthAttributeServiceClient,
                                              HealthAttributeRepository healthAttributeRepository,
                                              EventDispatcher eventDispatcher,
                                              Scheduler scheduler,
                                              InsurerConfigurationRepository insurerConfigurationRepository, VitalityAgeServiceClient vitalityAgeServiceClient, DeviceSpecificPreferences deviceSpecificPreferences) {
        this.healthAttributeServiceClient = healthAttributeServiceClient;
        this.healthAttributeRepository = healthAttributeRepository;
        this.eventDispatcher = eventDispatcher;
        this.scheduler = scheduler;
        this.insurerConfigurationRepository = insurerConfigurationRepository;
        this.vitalityAgeServiceClient = vitalityAgeServiceClient;
        this.deviceSpecificPreferences = deviceSpecificPreferences;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        createEventListeners();

        userInterface.setUpPullToRefresh();

        loadAttributes();
    }

    @Override
    public void loadAttributes() {
        userInterface.showLoadingIndicator();

        loadHealthAttributesFromRepository();

        fetchAttributesFromService();
    }

    @Override
    public void fetchAttributesFromService() {
        final int[] eventTypeKeys = getConfiguredEventTypeKeys();

        healthAttributeServiceClient.fetchHealthAttributeEventTypes(eventTypeKeys);
    }

    private int[] getConfiguredEventTypeKeys() {
        final List<Integer> eventTypeKeys = insurerConfigurationRepository.getConfiguredEventTypeKeys();

        int[] eventTypeKeyArray = new int[eventTypeKeys.size()];
        int i = 0;
        for (Integer eventTypeKey : eventTypeKeys) {
            eventTypeKeyArray[i++] = eventTypeKey;
        }
        return eventTypeKeyArray;
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(HealthAttributeRequestSuccess.class, healthAttributeRequestSuccessEventListener);
        eventDispatcher.addEventListener(RequestFailedEvent.class, requestFailureEventListener);
        eventDispatcher.addEventListener(VitalityAgeEvents.VitalityAgeResponseEvent.class, vitalityAgeEventListener);
    }

    private void createEventListeners() {
        healthAttributeRequestSuccessEventListener = new EventListener<HealthAttributeRequestSuccess>() {
            @Override
            public void onEvent(HealthAttributeRequestSuccess event) {
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        loadHealthAttributesFromRepository();

                        userInterface.hideLoadingIndicator();
                    }
                });
            }
        };

        requestFailureEventListener = new EventListener<RequestFailedEvent>() {
            @Override
            public void onEvent(final RequestFailedEvent event) {
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (event.getType() == RequestFailedEvent.Type.CONNECTION_ERROR) {
                            userInterface.showConnectionError();
                        } else {
                            userInterface.showGenericError();
                        }

                        userInterface.hideLoadingIndicator();
                    }
                });
            }
        };

        vitalityAgeEventListener = new EventListener<VitalityAgeEvents.VitalityAgeResponseEvent>() {
            @Override
            public void onEvent(VitalityAgeEvents.VitalityAgeResponseEvent event) {
                if ((vitalityAgeServiceClient.getVitalityAgeServiceResult() == RequestResult.SUCCESSFUL)) {
                    if (event.vitalityAge == null || (!MyHealthContent.vitalityAgeCalculated(Integer.valueOf(event.vitalityAge.getEffectiveType())))) {
                        userInterface.navigateToVitalityAge();
                        setReturnfromFormSubmission(false);
                    }
                }
                userInterface.hideLoadingIndicator();
            }
        };
    }

    @Override
    public boolean loadHealthAttributesFromRepository() {
        configuredHealthAttributeGroups = healthAttributeRepository.getHealthAttributeGroups(insurerConfigurationRepository.getConfiguredVHCFeatureTypes());

        completeGroups = new ArrayList<>();
        incompleteGroups = new ArrayList<>();
        setCompleteAndIncompleteHealthAttributeGroups(incompleteGroups, completeGroups);

        marketUiUpdate();

        userInterface.onHealthAttributesReady(incompleteGroups, completeGroups);

        return completeGroups.size() > 0 || incompleteGroups.size() > 0;
    }

    protected abstract void marketUiUpdate();


    @Override
    public boolean shouldShowVitalityAgeVHRPrompt() {
        return !deviceSpecificPreferences.hasCurrentUserCompletedVHC() && isCompletingFormSubmittion;
    }

    @Override
    public void setVHCHasCompletedBefore() {
        deviceSpecificPreferences.setCurrentUserHasCompletedVHC();
    }


    private void setCompleteAndIncompleteHealthAttributeGroups(List<HealthAttributeGroupDTO> incompleteGroups, List<HealthAttributeGroupDTO> completeGroups) {
        Collections.sort(configuredHealthAttributeGroups, new Comparator<HealthAttributeGroupDTO>() {
            @Override
            public int compare(HealthAttributeGroupDTO o1, HealthAttributeGroupDTO o2) {
                final String typeDescription1 = o1.getDescription();
                final String typeDescription2 = o2.getDescription();
                return typeDescription1.compareTo(typeDescription2);
            }
        });

        for (HealthAttributeGroupDTO group : configuredHealthAttributeGroups) {
            if (group.groupHasReadings()) {
                completeGroups.add(group);
            } else {
                incompleteGroups.add(group);
            }
        }

        Collections.sort(completeGroups, new Comparator<HealthAttributeGroupDTO>() {

            @Override
            public int compare(HealthAttributeGroupDTO o1, HealthAttributeGroupDTO o2) {

                int compareInt = Boolean.compare(o2.isPartiallyComplete(), o1.isPartiallyComplete());
                if (compareInt != 0) return compareInt;

                final Date currentMembershipPeriodStart = insurerConfigurationRepository.getCurrentMembershipPeriodStart();
                final Date currentMembershipPeriodEnd = insurerConfigurationRepository.getCurrentMembershipPeriodEnd();

                final boolean objectOneInPeriod = TimeUtilities.isDateInPeriod(o1.getMeasuredOn(),
                        currentMembershipPeriodStart,
                        currentMembershipPeriodEnd);
                final boolean objectTwoInPeriod = TimeUtilities.isDateInPeriod(o2.getMeasuredOn(),
                        currentMembershipPeriodStart,
                        currentMembershipPeriodEnd);

                compareInt = Boolean.compare(objectOneInPeriod, objectTwoInPeriod);
                if (compareInt != 0) {
                    return compareInt;
                }

                return Boolean.compare(o1.groupHasReadings(), o2.groupHasReadings());
            }
        });
    }

    @Override
    public void onUserInterfaceAppeared() {
        addEventListeners();
        if (shouldShowVitalityAgeVHRPrompt()) {
            vitalityAgeServiceClient.getVitalityAgeValue();
            setVHCHasCompletedBefore();
        }
    }


    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        eventDispatcher.removeEventListener(HealthAttributeRequestSuccess.class, healthAttributeRequestSuccessEventListener);
        eventDispatcher.removeEventListener(RequestFailedEvent.class, requestFailureEventListener);
        eventDispatcher.removeEventListener(VitalityAgeEvents.VitalityAgeResponseEvent.class, vitalityAgeEventListener);
        setReturnfromFormSubmission(false);
    }

    @Override
    public void onUserInterfaceDestroyed() {
        vitalityAgeServiceClient.cancelRequest();
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void onCompletedHealthAttributeGroupTapped(HealthAttributeGroupDTO healthAttributeGroup) {
        healthAttributeServiceClient.cancelRequest();
        userInterface.showHealthAttributeGroupDetails(healthAttributeGroup.getFeatureTypeKey());
    }

    @Override
    public void setReturnfromFormSubmission(boolean isCompletingFormSubmittion) {
        this.isCompletingFormSubmittion = isCompletingFormSubmittion;
    }
}
