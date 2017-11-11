package uk.co.maximumlikelihood.eventful.queue;

import uk.co.maximumlikelihood.eventful.event.EventTask;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;

public interface EntityQueuePostProcessor<E extends QueueableEntity<E, T>, T extends Comparable<? super T>> {
    boolean canProcess(E entity);

    EventTask<T> processCompletionTask(E entity, EntityQueue<E, T> queue, T startTime, T completionTime);

    T processCompletionTime(E entity, EntityQueue<E, T> queue, T startTime);

    default void startProcessing(E entity, EntityQueue<E, T> queue, FutureEventsQueue<T> futureEvents) {
        if (!canProcess(entity)) {
            throw new IllegalStateException();
        }

        final T startTime = futureEvents.getCurrentTime();
        final T completionTime = processCompletionTime(entity, queue, startTime);
        final EventTask<T> serviceCompletionTask = processCompletionTask(entity, queue, startTime, completionTime);

        futureEvents.schedule(completionTime, serviceCompletionTask
                .andThen(feq -> {
                    stopProcessing(entity, queue, feq);
                }));
        entity.notifyServiceStart(queue, startTime);
    }

    default void stopProcessing(E entity, EntityQueue<E, T> queue, FutureEventsQueue<T> futureEvents) {
        entity.notifyServiceFinish(queue, futureEvents.getCurrentTime());
        queue.notifyCanServe(futureEvents);
    }
}
