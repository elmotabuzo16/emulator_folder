package com.vitalityactive.va.myhealth.vitalityageprofile;


import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.myhealth.content.FeedbackItem;
import com.vitalityactive.va.myhealth.content.FeedbackTip;
import com.vitalityactive.va.myhealth.shared.MyHealthUtils;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MyHealthUtilsTest extends BaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }


    @Test
    public void can_format_attribute_created_date() {
        String expectedDate="11 October 2017, 02:33";
        assertNotNull(MyHealthUtils.formatToAttributeEventDate("2017-10-11T07:33:55.018000000Z[UTC]"));
        assertEquals(expectedDate,MyHealthUtils.formatToAttributeEventDate("2017-10-11T07:33:55.018000000Z[UTC]"));
    }

    @Test
    public void can_convert_to_sentence_case(){
       assertEquals("This is a sentence. This is the second part of the sentence",MyHealthUtils.toSentenceCase("THIS IS A SENTENCE. THIS IS THE SECOND PART OF THE SENTENCE"));
       assertEquals("This is a sentence. This is the second part of the sentence",MyHealthUtils.toSentenceCase("This is a Sentence. This is THE second part of the sentence"));
       assertEquals("This is a senten$$ce. This is the seco##nd part of the sentence",MyHealthUtils.toSentenceCase("This is a Senten$$ce. This is THE seco##nd part of the sentence"));
    }

    @Test
    public void can_prioritize_feedback_that_has_tips_1(){
        FeedbackItem feedbackItemWithTips=new FeedbackItem.Builder()
                .setFeedbackName("feedbackItemWithTips")
                .setFeedbackTips(Arrays.asList(new FeedbackTip(),new FeedbackTip()))
                .build();
        FeedbackItem feedbackItemWithOutTips=new FeedbackItem.Builder()
                .setFeedbackName("feedbackItemWithOutTips")
                .build();
        List<FeedbackItem> feedbackItems = MyHealthUtils.prioritizeFeedbackThatHasTips(Arrays.asList(feedbackItemWithOutTips, feedbackItemWithTips));
        assertNotNull(feedbackItems);
        assertTrue(feedbackItems.get(0).getFeedbackName()=="feedbackItemWithTips");
        assertTrue(feedbackItems.get(1).getFeedbackName()=="feedbackItemWithOutTips");
    }

    @Test
    public void can_prioritize_feedback_that_has_tips_2(){
        FeedbackItem feedbackItemWithTips=new FeedbackItem.Builder()
                .setFeedbackName("feedbackItemWithTips")
                .setFeedbackTips(Arrays.asList(new FeedbackTip(),new FeedbackTip()))
                .build();
        FeedbackItem feedbackItemWithOutTips=new FeedbackItem.Builder()
                .setFeedbackName("feedbackItemWithOutTips")
                .build();
        List<FeedbackItem> feedbackItems = MyHealthUtils.prioritizeFeedbackThatHasTips(Arrays.asList(feedbackItemWithTips,feedbackItemWithOutTips));
        assertNotNull(feedbackItems);
        assertTrue(feedbackItems.get(0).getFeedbackName()=="feedbackItemWithTips");
        assertTrue(feedbackItems.get(1).getFeedbackName()=="feedbackItemWithOutTips");
    }
    @Test
    public void can_prioritize_feedback_that_has_tips_maintain_original_order(){
        FeedbackItem feedbackItemWithTips=new FeedbackItem.Builder()
                .setFeedbackName("feedbackItemWithTips")
                .build();
        FeedbackItem feedbackItemWithOutTips=new FeedbackItem.Builder()
                .setFeedbackName("feedbackItemWithOutTips")
                .build();
        List<FeedbackItem> feedbackItems = MyHealthUtils.prioritizeFeedbackThatHasTips(Arrays.asList(feedbackItemWithTips,feedbackItemWithOutTips));
        assertNotNull(feedbackItems);
        assertTrue(feedbackItems.get(0).getFeedbackName()=="feedbackItemWithTips");
        assertTrue(feedbackItems.get(1).getFeedbackName()=="feedbackItemWithOutTips");
    }


    @Test
    public void can_order_feedback_by_typekey()throws Exception{
        FeedbackItem feedbackItemOne=new FeedbackItem.Builder()
                .setFeedbackName("feedbackItemOne")
                .setFeedbackTypeKey(1)
                .build();
        FeedbackItem feedbackItemTwo=new FeedbackItem.Builder()
                .setFeedbackName("feedbackItemTwo")
                .setFeedbackTypeKey(2)
                .build();
        FeedbackItem feedbackItemThree=new FeedbackItem.Builder()
                .setFeedbackName("feedbackItemThree")
                .setFeedbackTypeKey(3)
                .build();
        List<FeedbackItem> feedbackItems = MyHealthUtils.orderByHighestTypeKey(Arrays.asList(feedbackItemThree,feedbackItemOne,feedbackItemTwo));
        assertNotNull(feedbackItems);
        assertTrue(feedbackItems.get(0).getFeedbackName()=="feedbackItemThree");
        assertTrue(feedbackItems.get(1).getFeedbackName()=="feedbackItemTwo");
        assertTrue(feedbackItems.get(2).getFeedbackName()=="feedbackItemOne");
    }
}
