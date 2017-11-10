package uk.co.maximumlikelihood.eventful.process;

import uk.co.maximumlikelihood.eventful.event.EventTask;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;

public class RecurringEventTask<E extends EventTask<T>, T extends Comparable<? super T>> implements EventTask<T> {

    private RecurringEventProcess<E, T> process;
    private E delegate;

    public RecurringEventTask(RecurringEventProcess<E, T> process, E delegate) {
        this.process = process;
        this.delegate = delegate;
    }

    @Override
    public void perform(FutureEventsQueue<T> queue) {
        delegate.perform(queue);
        process.scheduleNext(queue);
    }
}
