package uk.co.maximumlikelihood.eventful.examples;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;
import uk.co.maximumlikelihood.eventful.process.SimpleRecurringEventProcess;
import uk.co.maximumlikelihood.eventful.queue.CapacitatedEntityQueueConsumer;
import uk.co.maximumlikelihood.eventful.queue.EntityQueue;
import uk.co.maximumlikelihood.eventful.queue.QueueArrivalTask;
import uk.co.maximumlikelihood.eventful.queue.QueueableEntity;
import uk.co.maximumlikelihood.eventful.util.ElapsedTimeFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class BankTeller {

    private static class Customer implements QueueableEntity<Customer, LocalDateTime> {

        private static int count = 0;

        private int id = ++count;

        @Override
        public String toString() {
            return String.format("Customer: %d", id);
        }

        @Override
        public void notifyArrival(EntityQueue<Customer, LocalDateTime> queue, LocalDateTime time) {

        }

        @Override
        public void notifyConsumptionStart(EntityQueue<Customer, LocalDateTime> queue, LocalDateTime time) {

        }
    }


    public static void main(String[] args) {
        final FutureEventsQueue<LocalDateTime> futureEvents = FutureEventsQueue.starting(LocalDateTime.now());


        final CapacitatedEntityQueueConsumer<Customer, LocalDateTime> queueConsumer
                = new CapacitatedEntityQueueConsumer<>(ElapsedTimeFactory.withElapsedTimeSupplierInUnits(new ExponentialDistribution(20.0)::sample, ChronoUnit.SECONDS),
                () -> fe -> {
                    System.out.println("Done!");
                },
                2);

        final EntityQueue<Customer, LocalDateTime> queue = new EntityQueue<>(queueConsumer);

        final SimpleRecurringEventProcess<QueueArrivalTask<Customer, LocalDateTime>, LocalDateTime> customerArrivalProcess
                = new SimpleRecurringEventProcess<>(
                () -> {
                    return new QueueArrivalTask<>(new Customer(), queue);
                },
                ElapsedTimeFactory.withElapsedTimeSupplierInUnits(new ExponentialDistribution(10.0)::sample,
                        ChronoUnit.SECONDS));

        final LocalDateTime end = LocalDateTime.now().plusDays(1);

        customerArrivalProcess.scheduleNext(futureEvents);

        while (futureEvents.hasNext() && futureEvents.getCurrentTime().isBefore(end)) {
            System.out.printf(" at t=%s\n", futureEvents.next());
        }
    }
}
