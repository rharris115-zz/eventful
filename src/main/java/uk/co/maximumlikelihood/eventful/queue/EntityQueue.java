package uk.co.maximumlikelihood.eventful.queue;

import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;

import java.util.LinkedList;
import java.util.Queue;

import static java.util.Objects.requireNonNull;

public final class EntityQueue<E extends QueueableEntity<E, T>, T extends Comparable<? super T>> {

    private final Queue<E> queue = new LinkedList<>();
    private final UnmodifiableQueue<E> unmodifiableQueue = new UnmodifiableQueue<>(queue);


    private final EntityQueueServer<E, T> server;

    public EntityQueue(EntityQueueServer<E, T> server) {
        this.server = requireNonNull(server, "server");
    }

    public void notifyArrival(E entity, FutureEventsQueue<T> futureEvents) {
        entity.notifyArrival(this, futureEvents.getCurrentTime());
        if (server.canServe(entity)) {
            server.startServing(entity, this, futureEvents);
        } else {
            queue.add(entity);
            entity.notifyEnterringQueue(this, futureEvents.getCurrentTime());
        }
    }

    public void notifyCanServe(FutureEventsQueue<T> futureEvents) {
        if (queue.isEmpty()) {
            return;
        }
        if (!server.canServe(queue.peek())) {
            throw new IllegalStateException("Server cannot start serving an entity despite notification that it can.");
        }
        E entity = queue.poll();
        server.startServing(entity, this, futureEvents);
    }

    public Queue<E> getQueue() {
        return unmodifiableQueue;
    }

    public EntityQueueServer<E, T> getServer() {
        return server;
    }
}
