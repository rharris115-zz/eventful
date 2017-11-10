package uk.co.maximumlikelihood.eventful.queue;

public interface QueueableEntity<E extends QueueableEntity<E, T>, T extends Comparable<? super T>> {

    void notifyArrival(EntityQueue<E, T> queue, T time);

    void notifyConsumptionStart(EntityQueue<E, T> queue, T time);

    void notifyConsumptionFinish(EntityQueue<E, T> queue, T time);

    default void notifyArrivalAndConsumptionStart(EntityQueue<E, T> queue, T time) {
        notifyArrival(queue, time);
        notifyConsumptionStart(queue, time);
    }
}
