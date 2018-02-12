package com.vitalityactive.va.myhealth;

import android.support.annotation.CallSuper;

import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.myhealth.content.AttributeItem;
import com.vitalityactive.va.myhealth.content.FeedbackItem;
import com.vitalityactive.va.myhealth.content.FeedbackTip;
import com.vitalityactive.va.myhealth.content.SectionItem;
import com.vitalityactive.va.myhealth.dto.AttributeDTO;
import com.vitalityactive.va.myhealth.dto.AttributeFeedbackDTO;
import com.vitalityactive.va.myhealth.dto.HealthInformationSectionDTO;
import com.vitalityactive.va.myhealth.dto.MyHealthRepositoryImpl;
import com.vitalityactive.va.myhealth.healthattributes.MyHealthAttributeDetailInteractorImpl;
import com.vitalityactive.va.myhealth.shared.MyHealthUtils;
import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;


public class MyHealthUtilsTest {

    @Rule
    public TestName name = new TestName();
    MyHealthAttributeDetailInteractorImpl myHealthAttributeDetailInteractorImpl;
    MyHealthRepositoryImpl myHealthRepository;

    @Before
    @CallSuper
    public void setUp() throws IOException {
        TestHarness.Settings.useMockService = true;
        TestHarness.setup(name.getMethodName())
                .clearEverythingLikeAFreshInstall()
                .withLoggedInUser();
        myHealthRepository = new MyHealthRepositoryImpl(TestHarness.setup().getDefaultDataStore());
        myHealthAttributeDetailInteractorImpl = new MyHealthAttributeDetailInteractorImpl(myHealthRepository);
    }

    @Test
    public void can_map_to_feedbacktips() throws Exception {
        persistHealthAttributes(null);
        AttributeDTO attribute = myHealthAttributeDetailInteractorImpl.getHealthAttributeBySectionTypeKeyAndAttributeTypeKey(8, 48);
        assertNotNull(attribute);
        assertNotNull(attribute.getAttributeFeedbackDtos());
        List<FeedbackTip> feedbackTips = MyHealthUtils.toFeedbackTips(attribute.getAttributeFeedbackDtos().get(0));
        assertNotNull(feedbackTips);
        assertNotNull(feedbackTips);
        assertTrue(feedbackTips.size() > 0);
        for (FeedbackTip feedbackTip : feedbackTips) {
            assertNotNull(feedbackTip.getFeedbackTipName());
            assertNotNull(feedbackTip.getFeedbackTipNote());
            assertNotNull(feedbackTip.getFeedbackTipTypeCode());
            assertNotNull(feedbackTip.getFeedbackName());
            assertTrue(feedbackTip.getFeedbackTipTypeKey() != 0);
        }
    }

    @Test
    public void can_map_to_feedback() throws Exception {
        int sectionTypeKey = 8;
        persistHealthAttributes(null);
        AttributeDTO attribute = myHealthAttributeDetailInteractorImpl.getHealthAttributeBySectionTypeKeyAndAttributeTypeKey(sectionTypeKey, 48);
        assertNotNull(attribute);
        assertNotNull(attribute.getAttributeFeedbackDtos());
        AttributeFeedbackDTO feedback = attribute.getAttributeFeedbackDtos().get(0);
        List<FeedbackItem> feedbackItems = MyHealthUtils.toFeedback(sectionTypeKey, attribute);
        assertNotNull(feedbackItems);
        for (FeedbackItem feedbackItem : feedbackItems) {
            assertNotNull(feedbackItem.getFeedbackTips());
            assertNotNull(feedbackItem.getFeedbackTypeCode());
            assertTrue(feedbackItem.getFeedbackTypeKey() != 0);
            assertNotNull(feedbackItem.getAttributeTitle());
            assertNotNull(feedbackItem.getFeedbackName());
            assertNotNull(feedbackItem.getAttributeFriendlyValue());
        }
    }

    @Test
    public void can_map_to_attribute() throws Exception {
        int sectionTypeKey = 8;
        persistHealthAttributes(null);
        HealthInformationSectionDTO section = myHealthAttributeDetailInteractorImpl.getHealthAttributeSectionDTOByTypeKey(sectionTypeKey);
        assertNotNull(section);
        List<AttributeItem> attributeItems = MyHealthUtils.toAttributeItems(section);
        for (AttributeItem attributeItem : attributeItems) {
            assertNotNull(attributeItem);
            assertNotNull(attributeItem.getAttributeFriendlyValue());
            assertNotNull(attributeItem.getAttributeTitle());
            assertNotNull(attributeItem.getAttributeValue());
            assertNotNull(attributeItem.getFeedbackItems());
            assertNotNull(attributeItem.getAttributeTypeKey() != 0);
            assertEquals(sectionTypeKey, attributeItem.getSectionTypeKey());
        }
    }

    @Test
    public void can_map_to_sectionitem() throws Exception {
        int sectionTypeKey = 8;
        persistHealthAttributes(null);
        HealthInformationSectionDTO section = myHealthAttributeDetailInteractorImpl.getHealthAttributeSectionDTOByTypeKey(sectionTypeKey);
        SectionItem sectionItem = MyHealthUtils.toSectionItem(section);
        assertNotNull(sectionItem);
        assertNotNull(sectionItem.getAttributeItems());
        assertNotNull(sectionItem.getSectionTitle());
        assertNotNull(sectionItem.getTintColor());
        assertTrue(sectionItem.getSectionTypekey() != 0);
        assertTrue(sectionItem.getSectionIcon() != 0);
        assertEquals(sectionTypeKey, sectionItem.getSectionTypekey());
    }

    @Test
    public void can_prioritize_feedback_with_tips() throws Exception {
        int sectionTypeKey = 8;
        persistHealthAttributes("myhealth/health_information_response_withattributedetails2.json");
        AttributeDTO attribute = myHealthAttributeDetailInteractorImpl.getHealthAttributeBySectionTypeKeyAndAttributeTypeKey(sectionTypeKey, 42);
        assertNotNull(attribute);
        assertNotNull(attribute.getAttributeFeedbackDtos());
        AttributeItem attributeItem = MyHealthUtils.toAttributeItem(attribute, sectionTypeKey);
        assertNotNull(attributeItem);
        List<FeedbackItem> feedbackItems = attributeItem.getFeedbackItems();

        FeedbackItem feedbackItemOne = feedbackItems.get(0);
        assertNotNull(feedbackItemOne);
        assertTrue(feedbackItemOne.getFeedbackTips().size() > 0);
        FeedbackTip feedbackTipOne = feedbackItemOne.getFeedbackTips().get(0);
        assertNotNull(feedbackTipOne.getFeedbackName());
        assertNotNull(feedbackTipOne.getFeedbackTipNote());

        FeedbackItem feedbackItemTwo = feedbackItems.get(1);
        assertTrue(feedbackItemTwo.getFeedbackTips().size() == 0);
    }

    @Test
    public void can_order_feedback_by_typekey()throws Exception{

    }

    private void persistHealthAttributes(String file) throws Exception {
        file = file != null ? file : "myhealth/health_information_response_withattributedetails.json";
        HealthAttributeInformationResponse response = RepositoryTestBase.getResponse(HealthAttributeInformationResponse.class, file);
        myHealthRepository.persistHealthAttributeTipResponse(response);
    }
}
