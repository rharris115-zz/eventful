package uk.co.maximumlikelihood.eventful.examples;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import uk.co.maximumlikelihood.eventful.event.EventTask;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;
import uk.co.maximumlikelihood.eventful.process.NextTimeFactory;
import uk.co.maximumlikelihood.eventful.process.SimpleRecurringEventProcess;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class BankTeller {

    private static class Customer {

        private static int count = 0;

        private int id = ++count;

        @Override
        public String toString() {
            return String.format("Customer: %d", id);
        }
    }

    private static class CustomerArrival implements EventTask<LocalDateTime> {

        private final Customer customer = new Customer();

        @Override
        public void perform(FutureEventsQueue<LocalDateTime> futureEvents) {
            System.out.printf("New arrival %s\n", customer);
        }
    }

    public static final void main(String[] args) {
        final FutureEventsQueue<LocalDateTime> futureEvents = FutureEventsQueue.starting(LocalDateTime.now());

        final ExponentialDistribution d = new ExponentialDistribution(10.0);

        final SimpleRecurringEventProcess<CustomerArrival, LocalDateTime> customerArrivalProcess
                = new SimpleRecurringEventProcess<>(CustomerArrival::new, NextTimeFactory.inUnits(() -> d.sample(), ChronoUnit.SECONDS));

        final LocalDateTime end = LocalDateTime.now().plusDays(1);

        customerArrivalProcess.scheduleNext(futureEvents);

        while (futureEvents.hasNext() && futureEvents.getCurrentTime().isBefore(end)) {
            System.out.printf("%s: ", futureEvents.next());
        }
    }
}
