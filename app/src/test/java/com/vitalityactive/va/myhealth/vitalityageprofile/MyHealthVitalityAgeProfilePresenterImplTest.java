package com.vitalityactive.va.myhealth.vitalityageprofile;

import com.vitalityactive.va.EventDispatcherOnInvoke;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.myhealth.MyHealthBaseTest;
import com.vitalityactive.va.myhealth.content.SectionItem;
import com.vitalityactive.va.myhealth.entity.VitalityAge;
import com.vitalityactive.va.myhealth.events.HealthAttributeFeedbackTipsEvent;
import com.vitalityactive.va.myhealth.service.HealthAttributeInformationServiceClient;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MyHealthVitalityAgeProfilePresenterImplTest extends MyHealthBaseTest {

    MyHealthVitalityAgeProfilePresenterImpl myHealthVitalityAgeProfilePresenterImpl;
    @Mock
    MyHealthVitalityAgeProfileInteractor mockMyHealthVitalityAgeProfileInteractor;
    @Mock
    MyHealthVitalityAgeProfilePresenter.UserInterface mockUserInterface;
    @Mock
    EventDispatcher mockEventDispatcher;
    @Mock
    HealthAttributeInformationServiceClient mockHealthAttributeInformationServiceClient;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        myHealthVitalityAgeProfilePresenterImpl = new MyHealthVitalityAgeProfilePresenterImpl(mockMyHealthVitalityAgeProfileInteractor);
        myHealthVitalityAgeProfilePresenterImpl.setUserInterface(mockUserInterface);
        fireFeedbackTipsSuccessEvent();
    }

    @Test
    public void should_fetch_vitality_age() {
        when(mockMyHealthVitalityAgeProfileInteractor.getVitalityAgeHealthAttribute()).thenReturn(createHealthAttributeResponse());
        myHealthVitalityAgeProfilePresenterImpl.onUserInterfaceAppeared();
        verify(mockMyHealthVitalityAgeProfileInteractor, times(1)).getVitalityAgeHealthAttribute();
        verify(mockUserInterface, times(1)).showVitalityAgeAndTips(any(VitalityAge.class),anyListOf(SectionItem.class));
    }

    @Test
    public void should_fetch_health_attribute_and_feedback_tips() throws Exception {
        setUpMockWebServer(200, "myhealth/health_information_response.json");
        myHealthVitalityAgeProfilePresenterImpl.onUserInterfaceAppeared();
        verify(mockMyHealthVitalityAgeProfileInteractor, times(1)).getVitalityAgeHealthAttribute();
        verify(mockMyHealthVitalityAgeProfileInteractor, times(3)).getHealthAttributeSectionDTOByTypeKey(any(Long.class));

    }

    @Test
    public void should_register_event_listener_for_feedback_tips() throws Exception {
        setUpMockWebServer(200, "myhealth/health_information_response.json");
        mockHealthAttributeInformationServiceClient.fetchHealthAttributeFeedbackTips(Matchers.<List<Long>>any(),false );
        verify(mockEventDispatcher, times(1)).dispatchEvent(any(HealthAttributeFeedbackTipsEvent.class));
    }


    private void fireFeedbackTipsSuccessEvent() {
        EventDispatcherOnInvoke
                .fire(mockEventDispatcher, new HealthAttributeFeedbackTipsEvent()).when(mockHealthAttributeInformationServiceClient).fetchHealthAttributeFeedbackTips(Matchers.<List<Long>>any(),false );
    }

}
