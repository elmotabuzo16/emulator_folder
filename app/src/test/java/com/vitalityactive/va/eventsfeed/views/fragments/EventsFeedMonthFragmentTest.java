package com.vitalityactive.va.eventsfeed.views.fragments;

import com.vitalityactive.va.events.EventDispatcher;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by jayellos on 11/27/17.
 */
public class EventsFeedMonthFragmentTest {

    @Mock EventDispatcher eventDispatcher;

    EventsFeedMonthFragment fragment;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

    }


    @Test
    public void should_call_addEventListener(){

    }
}