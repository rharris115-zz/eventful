package uk.co.maximumlikelihood.eventful.queue;

import uk.co.maximumlikelihood.eventful.event.EventTask;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;

public interface EntityQueueServer<E extends QueueableEntity<E, T>, T extends Comparable<? super T>> {
    boolean canServe(E entity);

    EventTask<T> serviceCompletionTask(E entity, EntityQueue<E, T> queue, T startTime, T completionTime);

    T serviceCompletionTime(E entity, EntityQueue<E, T> queue, T startTime);

    default void startServing(E entity, EntityQueue<E, T> queue, FutureEventsQueue<T> futureEvents) {
        if (!canServe(entity)) {
            throw new IllegalStateException();
        }

        final T startTime = futureEvents.getCurrentTime();
        final T completionTime = serviceCompletionTime(entity, queue, startTime);
        final EventTask<T> serviceCompletionTask = serviceCompletionTask(entity, queue, startTime, completionTime);

        futureEvents.schedule(completionTime, serviceCompletionTask.andThen(feq -> {
            stopServing(entity, queue, feq);
        }));
        entity.notifyServiceStart(queue, startTime);
    }

    default void stopServing(E entity, EntityQueue<E, T> queue, FutureEventsQueue<T> futureEvents) {
        entity.notifyServiceFinish(queue, futureEvents.getCurrentTime());
        queue.notifyCanServe(futureEvents);
    }
}
