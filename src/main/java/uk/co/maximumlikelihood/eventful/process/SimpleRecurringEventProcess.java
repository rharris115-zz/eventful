package uk.co.maximumlikelihood.eventful.process;

import uk.co.maximumlikelihood.eventful.event.EventTask;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;

import java.util.function.Function;
import java.util.function.Supplier;

public class SimpleRecurringEventProcess<E extends EventTask<T>, T extends Comparable<? super T>> implements RecurringEventProcess<E, T> {

    private final Supplier<E> delegateFactory;

    private final Function<T, T> nextTimeFactory;

    public SimpleRecurringEventProcess(Supplier<E> delegateFactory, Function<T, T> nextTimeFactory) {
        this.delegateFactory = delegateFactory;
        this.nextTimeFactory = nextTimeFactory;
    }

    @Override
    public boolean hasMoreEvents(FutureEventsQueue<T> queue) {
        return true;
    }

    @Override
    public T nextEventTime(FutureEventsQueue<T> queue) {
        return nextTimeFactory.apply(queue.getCurrentTime());
    }

    @Override
    public E nextDelegateTask(FutureEventsQueue<T> queue) {
        return delegateFactory.get();
    }
}
