package uk.co.maximumlikelihood.eventful.examples;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import uk.co.maximumlikelihood.eventful.event.EventTask;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;
import uk.co.maximumlikelihood.eventful.process.RecurringEventProcess;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

public class BankTeller {

    private static class CustomerArrival implements EventTask<LocalDateTime> {
        @Override
        public void perform(FutureEventsQueue<LocalDateTime> queue) {
            System.out.println("Arriving customer.");
        }
    }

    private static class CustomerArrivalProcess implements RecurringEventProcess<CustomerArrival, LocalDateTime> {

        private ExponentialDistribution d = new ExponentialDistribution(1.0);


        @Override
        public boolean hasMoreEvents(FutureEventsQueue<LocalDateTime> queue) {
            return true;
        }

        @Override
        public LocalDateTime nextEventTime(FutureEventsQueue<LocalDateTime> queue) {
            return queue.getCurrentTime().plus(Duration.ofNanos((int) Math.ceil(d.sample() * 1_000_000_000)));
        }

        @Override
        public CustomerArrival nextDelegateTask(FutureEventsQueue<LocalDateTime> queue) {
            return new CustomerArrival();
        }
    }

    public static final void main(String[] args) {
        final FutureEventsQueue<LocalDateTime> queue = FutureEventsQueue.starting(LocalDateTime.now());
        final RecurringEventProcess<CustomerArrival, LocalDateTime> customerArrivalLocalDateTimeProcess = new CustomerArrivalProcess();
        final LocalDateTime end = LocalDateTime.now().plusDays(1);

        customerArrivalLocalDateTimeProcess.scheduleNext(queue);

        while (queue.hasNext() && queue.getCurrentTime().isBefore(end)) {
            System.out.printf("%s: ", queue.next());
        }
    }
}
