package uk.co.maximumlikelihood.eventful.event;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.*;

public final class FutureEventsQueueTest {


    @Test
    public void testCurrentTimeUpdate() {
        FutureEventsQueue<Integer> futureEvents = FutureEventsQueue.starting(0);

        final Integer futureTime = 10;

        futureEvents.schedule(futureTime, feq -> {
        });

        while (futureEvents.hasNext()) {
            Integer currentTime = futureEvents.next();
            assertEquals("", futureTime, currentTime);
            assertEquals("", futureTime, futureEvents.getCurrentTime());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionOnCreateWithNullInitialTime() {
        FutureEventsQueue.starting(null);
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionOnScheduleWithNullFutureTime() {
        FutureEventsQueue.starting(0).schedule(null, feq -> {
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionOnScheduleInPresent() {
        FutureEventsQueue.starting(0).schedule(0, feq -> {
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionOnScheduleInPast() {
        FutureEventsQueue.starting(0).schedule(-1, feq -> {
        });
    }
}
