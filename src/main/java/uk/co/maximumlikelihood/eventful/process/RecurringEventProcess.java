package uk.co.maximumlikelihood.eventful.process;

import uk.co.maximumlikelihood.eventful.event.EventTask;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;

public interface RecurringEventProcess<T extends Comparable<? super T>> {

    boolean hasMoreEvents(T time);

    T nextEventTime(FutureEventsQueue<T> futureEvents);

    EventTask<T> nextEventTask(FutureEventsQueue<T> futureEvents);

    default void scheduleNext(FutureEventsQueue<T> futureEvents) {
        if (hasMoreEvents(futureEvents.getCurrentTime())) {
            futureEvents.schedule(nextEventTime(futureEvents),
                    nextEventTask(futureEvents).andThen(this::scheduleNext));
        }
    }
}
