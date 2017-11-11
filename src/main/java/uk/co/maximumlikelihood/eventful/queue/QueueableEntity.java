package uk.co.maximumlikelihood.eventful.queue;

public interface QueueableEntity<E extends QueueableEntity<E, T>, T extends Comparable<? super T>> {

    void notifyQueueArrival(EntityQueue<E, T> queue, T time);

    void notifyEnteringQueue(EntityQueue<E, T> queue, T time);

    void notifyQueuePostProcessingStart(EntityQueue<E, T> queue, T time);

    void notifyQueuePostProcessingFinish(EntityQueue<E, T> queue, T time);
}
