package uk.co.maximumlikelihood.eventful.queue;

import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;

public interface EntityQueueConsumer<E extends QueueableEntity<E, T>, T extends Comparable<? super T>> {
    boolean canConsume(E entity);

    void consumeStart(E entity, EntityQueue<E, T> queue, FutureEventsQueue<T> futureEvents);

    void consumeFinish(E entity, EntityQueue<E, T> queue, FutureEventsQueue<T> futureEvents);
}
