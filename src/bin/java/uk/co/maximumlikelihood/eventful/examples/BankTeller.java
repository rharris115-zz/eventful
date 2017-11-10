package uk.co.maximumlikelihood.eventful.examples;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;
import uk.co.maximumlikelihood.eventful.process.SimpleRecurringEventProcess;
import uk.co.maximumlikelihood.eventful.queue.CapacitatedEntityQueueServer;
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
            System.out.printf("%s arriving at queue @t=%s\n", this, time);
        }

        @Override
        public void notifyEnterringQueue(EntityQueue<Customer, LocalDateTime> queue, LocalDateTime time) {
            System.out.printf("%s enterring queue @t=%s\n", this, time);
        }

        @Override
        public void notifyServiceStart(EntityQueue<Customer, LocalDateTime> queue, LocalDateTime time) {
            System.out.printf("%s started being serviced @t=%s\n", this, time);
        }

        @Override
        public void notifyServiceFinish(EntityQueue<Customer, LocalDateTime> queue, LocalDateTime time) {
            System.out.printf("%s finished being serviced at queue @t=%s\n", this, time);
        }
    }


    public static void main(String[] args) {
        final FutureEventsQueue<LocalDateTime> futureEvents = FutureEventsQueue.starting(LocalDateTime.now());


        final CapacitatedEntityQueueServer<Customer, LocalDateTime> queueConsumer
                = new CapacitatedEntityQueueServer<>(ElapsedTimeFactory.withElapsedTimeSupplierInUnits(new ExponentialDistribution(20.0)::sample, ChronoUnit.SECONDS),
                () -> fe -> {
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
            futureEvents.next();
        }
    }
}
