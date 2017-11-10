package uk.co.maximumlikelihood.eventful.event;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.Objects.requireNonNull;

public final class FutureEventsQueue<T extends Comparable<? super T>> implements Iterator<T> {

    private class EventWrapper implements Comparable<EventWrapper> {

        private final T time;

        private final EventTask<T> task;

        private EventWrapper(T time, EventTask<T> task) {
            this.time = time;
            this.task = task;
        }

        @Override
        public int compareTo(EventWrapper o) {
            //We've checked elsewhere that these times will not be null.
            return time.compareTo(o.time);
        }

        T getTime() {
            return time;
        }

        EventTask<T> getTask() {
            return task;
        }
    }

    private T currentTime;
    private Queue<EventWrapper> events = new PriorityQueue<>();


    private FutureEventsQueue(T currentTime) {
        this.currentTime = currentTime;
    }

    public static <T extends Comparable<? super T>> FutureEventsQueue<T> starting(T initialTime) {
        return new FutureEventsQueue<>(requireNonNull(initialTime, "initialTime"));
    }

    public T getCurrentTime() {
        return currentTime;
    }

    public void schedule(T futureTime, EventTask<T> task) {
        requireNonNull(futureTime, "futureTime");
        if (currentTime.compareTo(futureTime) >= 0) {
            throw new IllegalArgumentException(String.format("Can't schedule events in the past or present. currentTime=%s, futureTime=%s", currentTime, futureTime));
        }
        events.add(new EventWrapper(futureTime, task));
    }

    @Override
    public boolean hasNext() {
        return !events.isEmpty();
    }

    @Override
    public T next() {
        if (events.isEmpty()) {
            throw new NoSuchElementException("No more events.");
        }
        final EventWrapper eventWrapper = events.poll();
        currentTime = eventWrapper.getTime();
        eventWrapper.getTask().perform(this);
        return eventWrapper.getTime();
    }
}
