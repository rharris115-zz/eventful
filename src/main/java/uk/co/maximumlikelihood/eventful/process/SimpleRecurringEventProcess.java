package uk.co.maximumlikelihood.eventful.process;

import uk.co.maximumlikelihood.eventful.event.EventTask;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class SimpleRecurringEventProcess<E extends EventTask<T>, T extends Comparable<? super T>> implements RecurringEventProcess<E, T> {

    private final Supplier<E> nextTaskFactory;
    private final Function<T, T> nextTimeFactory;

    public SimpleRecurringEventProcess(Supplier<E> delegateFactory, Function<T, T> nextTimeFactory) {
        this.nextTaskFactory = requireNonNull(delegateFactory, "nextTaskFactory");
        this.nextTimeFactory = requireNonNull(nextTimeFactory, "nextTimeFactory");
    }

    @Override
    public boolean hasMoreEvents(FutureEventsQueue<T> futureEvents) {
        return true;
    }

    @Override
    public T nextEventTime(FutureEventsQueue<T> futureEvents) {
        return nextTimeFactory.apply(futureEvents.getCurrentTime());
    }

    @Override
    public E nextEventTask(FutureEventsQueue<T> futureEvents) {
        return nextTaskFactory.get();
    }
}
