package uk.co.maximumlikelihood.eventful.queue;

import uk.co.maximumlikelihood.eventful.event.EventTask;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;

import java.util.function.Function;
import java.util.function.Supplier;

public class CapacitatedEntityQueueConsumer<E extends QueueableEntity<E, T>, T extends Comparable<? super T>> implements EntityQueueConsumer<E, T> {

    private final Function<T, T> consumptionTimeFactory;

    private final Supplier<EventTask<T>> consumptionTask;

    private final int capacity;

    private int utilisation = 0;

    public CapacitatedEntityQueueConsumer(Function<T, T> consumptionTimeFactory, Supplier<EventTask<T>> consumptionTask, int capacity) {
        this.consumptionTimeFactory = consumptionTimeFactory;
        this.consumptionTask = consumptionTask;
        this.capacity = capacity;
    }

    @Override
    public boolean canConsume(E entity) {
        return utilisation < capacity;
    }

    @Override
    public void consume(E entity, EntityQueue<E, T> queue, FutureEventsQueue<T> futureEvents) {
        if (!canConsume(entity)) {
            throw new IllegalStateException();
        }
        utilisation++;
        T consumptionEndTime = consumptionTimeFactory.apply(futureEvents.getCurrentTime());
        futureEvents.schedule(consumptionEndTime, consumptionTask.get().andThen(feq -> {
            free();
            queue.notifyCanConsume(feq);
        }));
    }

    private void free() {
        utilisation--;
    }
}
