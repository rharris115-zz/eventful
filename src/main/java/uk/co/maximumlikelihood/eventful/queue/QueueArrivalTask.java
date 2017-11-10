package uk.co.maximumlikelihood.eventful.queue;

import uk.co.maximumlikelihood.eventful.event.EventTask;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;

import static java.util.Objects.requireNonNull;

public class QueueArrivalTask<E extends QueueableEntity<E, T>, T extends Comparable<? super T>> implements EventTask<T> {

    private final E entity;
    private final EntityQueue<E, T> entityQueue;

    public QueueArrivalTask(E entity, EntityQueue<E, T> entityQueue) {
        this.entity = requireNonNull(entity, "entity");
        this.entityQueue = requireNonNull(entityQueue, "entityQueue");
    }

    @Override
    public void perform(FutureEventsQueue<T> futureEvents) {
        entityQueue.notifyArrival(entity, futureEvents);
    }
}
