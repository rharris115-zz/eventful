package uk.co.maximumlikelihood.eventful.event;

public interface EventTask<T extends Comparable<? super T>> {
    void perform(FutureEventsQueue<T> futureEvents);
}
