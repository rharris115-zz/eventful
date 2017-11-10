package uk.co.maximumlikelihood.eventful.event;

public interface EventTask<T extends Comparable<? super T>> {
    void perform(FutureEventsQueue<T> futureEvents);

    default EventTask<T> andThen(EventTask<T> additionalTask) {
        return futureEvents -> {
            perform(futureEvents);
            additionalTask.perform(futureEvents);
        };
    }
}
