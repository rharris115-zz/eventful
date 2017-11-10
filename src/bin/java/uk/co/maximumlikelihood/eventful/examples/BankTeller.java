package uk.co.maximumlikelihood.eventful.examples;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import uk.co.maximumlikelihood.eventful.event.EventTask;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;
import uk.co.maximumlikelihood.eventful.process.SimpleRecurringEventProcess;

import java.time.LocalDateTime;

public class BankTeller {


    private static class CustomerArrival implements EventTask<LocalDateTime> {
        @Override
        public void perform(FutureEventsQueue<LocalDateTime> queue) {
            System.out.println("New arrival");
        }
    }

    public static final void main(String[] args) {
        final FutureEventsQueue<LocalDateTime> queue = FutureEventsQueue.starting(LocalDateTime.now());

        final ExponentialDistribution d = new ExponentialDistribution(10.0);

        final SimpleRecurringEventProcess<CustomerArrival, LocalDateTime> customerArrivalProcess
                = new SimpleRecurringEventProcess<>(CustomerArrival::new,
                t -> t.plusNanos((long) Math.ceil(d.sample() * 1_000_000_000)));

        final LocalDateTime end = LocalDateTime.now().plusDays(1);

        customerArrivalProcess.scheduleNext(queue);

        while (queue.hasNext() && queue.getCurrentTime().isBefore(end)) {
            System.out.printf("%s: ", queue.next());
        }
    }
}
