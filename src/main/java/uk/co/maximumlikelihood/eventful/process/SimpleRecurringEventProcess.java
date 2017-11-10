package uk.co.maximumlikelihood.eventful.process;

import uk.co.maximumlikelihood.eventful.event.EventTask;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;

import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class SimpleRecurringEventProcess<T extends Comparable<? super T>> implements RecurringEventProcess<T> {

    private final Supplier<EventTask<T>> nextTaskFactory;
    private final Function<T, T> nextTimeFactory;

    public SimpleRecurringEventProcess(Supplier<EventTask<T>> nextTaskFactory, Function<T, T> nextTimeFactory) {
        this.nextTaskFactory = requireNonNull(nextTaskFactory, "nextTaskFactory");
        this.nextTimeFactory = requireNonNull(nextTimeFactory, "nextTimeFactory");
    }

    @Override
    public boolean hasMoreEvents(T time) {
        return true;
    }

    @Override
    public T nextEventTime(FutureEventsQueue<T> futureEvents) {
        return nextTimeFactory.apply(futureEvents.getCurrentTime());
    }

    @Override
    public EventTask<T> nextEventTask(FutureEventsQueue<T> futureEvents) {
        return nextTaskFactory.get();
    }
}
