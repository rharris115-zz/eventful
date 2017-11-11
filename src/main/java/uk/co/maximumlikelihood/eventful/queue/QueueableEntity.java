package uk.co.maximumlikelihood.eventful.queue;

public interface QueueableEntity<E extends QueueableEntity<E, T>, T extends Comparable<? super T>> {

    default void notifyQueueArrival(EntityQueue<E, T> queue, T time) {
    }

    default void notifyEnteringQueue(EntityQueue<E, T> queue, T time) {
    }

    default void notifyQueuePostProcessingStart(EntityQueue<E, T> queue, T time) {
    }

    default void notifyQueuePostProcessingFinish(EntityQueue<E, T> queue, T time) {
    }
}
