package uk.co.maximumlikelihood.eventful.queue;

import uk.co.maximumlikelihood.eventful.event.EventTask;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;

import java.util.function.Function;
import java.util.function.Supplier;

public class CapacitatedEntityQueueServer<E extends QueueableEntity<E, T>, T extends Comparable<? super T>> implements EntityQueueServer<E, T> {

    private final Function<T, T> serviceCompletionTimeFactory;

    private final Supplier<EventTask<T>> serviceCompletionTaskFactory;

    private final int capacity;

    private int utilisation = 0;

    public CapacitatedEntityQueueServer(Function<T, T> serviceCompletionTimeFactory, Supplier<EventTask<T>> serviceCompletionTaskFactory, int capacity) {
        this.serviceCompletionTimeFactory = serviceCompletionTimeFactory;
        this.serviceCompletionTaskFactory = serviceCompletionTaskFactory;
        this.capacity = capacity;
    }

    @Override
    public boolean canServe(E entity) {
        return utilisation < capacity;
    }

    @Override
    public void startServing(E entity, EntityQueue<E, T> queue, FutureEventsQueue<T> futureEvents) {
        EntityQueueServer.super.startServing(entity, queue, futureEvents);
        utilisation++;
    }

    @Override
    public void stopServing(E entity, EntityQueue<E, T> queue, FutureEventsQueue<T> futureEvents) {
        utilisation--;
        EntityQueueServer.super.stopServing(entity, queue, futureEvents);
    }

    @Override
    public EventTask<T> serviceCompletionTask(E entity, EntityQueue<E, T> queue, T startTime, T completionTime) {
        return serviceCompletionTaskFactory.get();
    }

    @Override
    public T serviceCompletionTime(E entity, EntityQueue<E, T> queue, T startTime) {
        return serviceCompletionTimeFactory.apply(startTime);
    }
}
