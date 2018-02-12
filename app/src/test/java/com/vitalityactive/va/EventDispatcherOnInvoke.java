package com.vitalityactive.va;

import com.vitalityactive.va.events.EventDispatcher;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import static org.mockito.Mockito.doAnswer;

public class EventDispatcherOnInvoke {
    public static Stubber fire(final EventDispatcher eventDispatcher, final Object event) {
        return doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                eventDispatcher.dispatchEvent(event);
                return null;
            }
        });
    }
}
