package uk.co.maximumlikelihood.eventful.queue;

public interface QueueableEntity<E extends QueueableEntity<E, T>, T extends Comparable<? super T>> {

    void notifyArrival(EntityQueue<E, T> queue, T time);

    void notifyEnterringQueue(EntityQueue<E, T> queue, T time);

    void notifyServiceStart(EntityQueue<E, T> queue, T time);

    void notifyServiceFinish(EntityQueue<E, T> queue, T time);
}
