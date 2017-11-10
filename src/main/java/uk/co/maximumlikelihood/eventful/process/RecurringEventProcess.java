package uk.co.maximumlikelihood.eventful.process;

import uk.co.maximumlikelihood.eventful.event.EventTask;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;

import static java.util.Objects.requireNonNull;

public interface RecurringEventProcess<E extends EventTask<T>, T extends Comparable<? super T>> {

    boolean hasMoreEvents(FutureEventsQueue<T> futureEvents);

    T nextEventTime(FutureEventsQueue<T> futureEvents);

    E nextDelegateTask(FutureEventsQueue<T> futureEvents);

    default void scheduleNext(FutureEventsQueue<T> futureEvents) {
        if (hasMoreEvents(futureEvents)) {
            futureEvents.schedule(nextEventTime(futureEvents),
                    new RecurringEventTask<>(this, nextDelegateTask(futureEvents)));
        }
    }
}
