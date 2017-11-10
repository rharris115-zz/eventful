package uk.co.maximumlikelihood.eventful.queue;

import uk.co.maximumlikelihood.eventful.event.EventTask;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;

import java.util.function.Function;
import java.util.function.Supplier;

public class CapacitatedEntityQueueConsumer<E extends QueueableEntity<E, T>, T extends Comparable<? super T>> implements EntityQueueConsumer<E, T> {

    private final Function<T, T> consumptionTimeFactory;

    private final Supplier<EventTask<T>> consumptionTaskFactory;

    private final int capacity;

    private int utilisation = 0;

    public CapacitatedEntityQueueConsumer(Function<T, T> consumptionTimeFactory, Supplier<EventTask<T>> consumptionTaskFactory, int capacity) {
        this.consumptionTimeFactory = consumptionTimeFactory;
        this.consumptionTaskFactory = consumptionTaskFactory;
        this.capacity = capacity;
    }

    @Override
    public boolean canConsume(E entity) {
        return utilisation < capacity;
    }

    @Override
    public void consumeStart(E entity, EntityQueue<E, T> queue, FutureEventsQueue<T> futureEvents) {
        if (!canConsume(entity)) {
            throw new IllegalStateException();
        }
        utilisation++;
        entity.notifyConsumptionStart(queue, futureEvents.getCurrentTime());
        T consumptionEndTime = consumptionTimeFactory.apply(futureEvents.getCurrentTime());
        futureEvents.schedule(consumptionEndTime, consumptionTaskFactory.get().andThen(feq -> {
            consumeFinish(entity, queue, feq);
        }));
    }

    @Override
    public void consumeFinish(E entity, EntityQueue<E, T> queue, FutureEventsQueue<T> futureEvents) {
        entity.notifyConsumptionFinish(queue, futureEvents.getCurrentTime());
        utilisation--;
        queue.notifyCanConsume(futureEvents);
    }
}
