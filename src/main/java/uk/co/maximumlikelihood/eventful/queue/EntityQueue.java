package uk.co.maximumlikelihood.eventful.queue;

import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;

import java.util.LinkedList;
import java.util.Queue;

import static java.util.Objects.requireNonNull;

public final class EntityQueue<E extends QueueableEntity<E, T>, T extends Comparable<? super T>> {

    private final Queue<E> queue = new LinkedList<>();
    private final UnmodifiableQueue<E> unmodifiableQueue = new UnmodifiableQueue<>(queue);


    private final EntityQueueConsumer<E, T> consumer;

    public EntityQueue(EntityQueueConsumer<E, T> consumer) {
        this.consumer = requireNonNull(consumer, "consumer");
    }

    public void notifyArrival(E entity, FutureEventsQueue<T> futureEvents) {
        if (consumer.canConsume(entity)) {
            consumer.consumeStart(entity, this, futureEvents);
            entity.notifyArrivalAndConsumptionStart(this, futureEvents.getCurrentTime());
        } else {
            queue.add(entity);
            entity.notifyArrival(this, futureEvents.getCurrentTime());
        }
    }

    public void notifyCanConsume(FutureEventsQueue<T> futureEvents) {
        if (queue.isEmpty()) {
            return;
        }
        if (!consumer.canConsume(queue.peek())) {
            throw new IllegalStateException("Consumer cannot consumeStart an entity despite notification that it can.");
        }
        E entity = queue.poll();
        consumer.consumeStart(entity, this, futureEvents);
    }

    public Queue<E> getQueue() {
        return unmodifiableQueue;
    }

    public EntityQueueConsumer<E, T> getConsumer() {
        return consumer;
    }
}
