package uk.co.maximumlikelihood.eventful.process;

import uk.co.maximumlikelihood.eventful.event.EventTask;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;

public interface RecurringEventProcess<E extends EventTask<T>, T extends Comparable<? super T>> {

    boolean hasMoreEvents(FutureEventsQueue<T> queue);

    T nextEventTime(FutureEventsQueue<T> queue);

    E nextDelegateTask(FutureEventsQueue<T> queue);

    default void scheduleNext(FutureEventsQueue<T> queue) {
        if (hasMoreEvents(queue)) {
            T nextTime = nextEventTime(queue);
            E nextTask = nextDelegateTask(queue);
            queue.schedule(nextTime, new RecurringEventTask<>(this, nextTask));
        }
    }
}
