package com.vitalityactive.va;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;

public class SpyOnMockRequestPosition {
    int currentPosition;

    public Answer getNextAnswer(final int expectedPosition) {
        return new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ++currentPosition;
                assertEquals(String.format("expected position %d, but current is %d", expectedPosition, currentPosition),
                        expectedPosition, currentPosition);
                return null;
            }
        };
    }

    public Stubber getNextDoAnswer(int expectedPosition) {
        return doAnswer(getNextAnswer(expectedPosition));
    }

    public int getTotalRequests() {
        return currentPosition;
    }
}
