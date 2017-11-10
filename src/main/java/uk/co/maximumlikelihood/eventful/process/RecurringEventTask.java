package uk.co.maximumlikelihood.eventful.process;

import uk.co.maximumlikelihood.eventful.event.EventTask;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;

import static java.util.Objects.requireNonNull;

class RecurringEventTask<E extends EventTask<T>, T extends Comparable<? super T>> implements EventTask<T> {

    private final RecurringEventProcess<E, T> process;
    private final E delegate;

    RecurringEventTask(RecurringEventProcess<E, T> process, E delegate) {
        this.process = requireNonNull(process, "process");
        this.delegate = requireNonNull(delegate, "delegate");
    }

    @Override
    public void perform(FutureEventsQueue<T> futureEvents) {
        delegate.perform(futureEvents);
        process.scheduleNext(futureEvents);
    }
}
