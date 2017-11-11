package uk.co.maximumlikelihood.eventful.queue;

import uk.co.maximumlikelihood.eventful.event.EventTask;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;

import java.util.function.Function;
import java.util.function.Supplier;

public class CapacitatedEntityQueuePostProcessor<E extends QueueableEntity<E, T>, T extends Comparable<? super T>> implements EntityQueuePostProcessor<E, T> {

    private final Function<T, T> processCompletionTimeFactory;

    private final Supplier<EventTask<T>> processCompletionTaskFactory;

    private final int capacity;

    private int utilisation = 0;

    public CapacitatedEntityQueuePostProcessor(Function<T, T> processCompletionTimeFactory, Supplier<EventTask<T>> processCompletionTaskFactory, int capacity) {
        this.processCompletionTimeFactory = processCompletionTimeFactory;
        this.processCompletionTaskFactory = processCompletionTaskFactory;
        this.capacity = capacity;
    }

    @Override
    public boolean canProcess(E entity) {
        return utilisation < capacity;
    }

    @Override
    public void startProcessing(E entity, EntityQueue<E, T> queue, FutureEventsQueue<T> futureEvents) {
        EntityQueuePostProcessor.super.startProcessing(entity, queue, futureEvents);
        utilisation++;
    }

    @Override
    public void stopProcessing(E entity, EntityQueue<E, T> queue, FutureEventsQueue<T> futureEvents) {
        utilisation--;
        EntityQueuePostProcessor.super.stopProcessing(entity, queue, futureEvents);
    }

    @Override
    public EventTask<T> processCompletionTask(E entity, EntityQueue<E, T> queue, T startTime, T completionTime) {
        return processCompletionTaskFactory.get();
    }

    @Override
    public T processCompletionTime(E entity, EntityQueue<E, T> queue, T startTime) {
        return processCompletionTimeFactory.apply(startTime);
    }
}
