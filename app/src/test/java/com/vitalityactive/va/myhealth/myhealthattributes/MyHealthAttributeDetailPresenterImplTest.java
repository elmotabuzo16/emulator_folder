package com.vitalityactive.va.myhealth.myhealthattributes;


import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.myhealth.content.FeedbackTip;
import com.vitalityactive.va.myhealth.content.HealthAttributeRecommendationItem;
import com.vitalityactive.va.myhealth.dto.AttributeDTO;
import com.vitalityactive.va.myhealth.healthattributes.MyHealthAttributeDetailInteractor;
import com.vitalityactive.va.myhealth.healthattributes.MyHealthAttributeDetailPresenter;
import com.vitalityactive.va.myhealth.healthattributes.MyHealthAttributeDetailPresenterImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.stubbing.OngoingStubbing;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MyHealthAttributeDetailPresenterImplTest extends BaseTest {

    @Mock
    MyHealthAttributeDetailPresenter.UserInterface mockUserInterface;
    @Mock
    MyHealthAttributeDetailInteractor mockMyHealthAttributeDetailInteractor;

    MyHealthAttributeDetailPresenterImpl myHealthAttributeDetailPresenterImpl;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        myHealthAttributeDetailPresenterImpl = new MyHealthAttributeDetailPresenterImpl(mockMyHealthAttributeDetailInteractor);
        myHealthAttributeDetailPresenterImpl.setUserInterface(mockUserInterface);
        myHealthAttributeDetailPresenterImpl.setHealthAttributeTypeKey(1);
        myHealthAttributeDetailPresenterImpl.setSectionTypeKey(1);
        final OngoingStubbing<AttributeDTO> attributeDTOOngoingStubbing = when(mockMyHealthAttributeDetailInteractor.getHealthAttributeBySectionTypeKeyAndAttributeTypeKey(anyInt(), anyInt())).thenReturn(new AttributeDTO());
    }

    @Test
    public void can_load_attribute_recommendations_and_feedback_tips() {
        myHealthAttributeDetailPresenterImpl.onUserInterfaceAppeared();
        verify(mockUserInterface, times(1)).showHealthAttributeRecommendationAndFeedbackTip(any(HealthAttributeRecommendationItem.class), Matchers.<List<FeedbackTip>>any());
        verify(mockUserInterface, times(1)).showTitle(anyString());
    }

}
